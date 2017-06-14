package com.zbiti.etl.runtime.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.common.RunShellUtil;
import com.zbiti.common.StringUtil;
import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;
import com.zbiti.etl.runtime.INodeRuntimeService;

@Service
public class NodeRuntimeServiceImpl implements INodeRuntimeService{


	private static final Log logger = LogFactory.getLog(NodeRuntimeServiceImpl.class);
	@Autowired
	private INodeService nodeService;
	@Autowired
	private IStepService stepService;
	@Autowired
	private ICommandService commandService;
	private Node node;
	private CuratorFramework zk;
	private Thread cleanLocalExcpProcessRunnable=new Thread(new CleanLocalExcpProcessRunnable());
	private Thread scheduleCommandRunnable=new Thread(new ScheduleCommandRunnable());
	//线程池-节点有最大任务数限制
	private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	File file=new File(StringUtil.getFilePath("stop"));
	
	@Override
	public void join() {

        try {
			cleanLocalExcpProcessRunnable.join();
	        scheduleCommandRunnable.join();
		} catch (InterruptedException e) {
			logger.error("nodeRuntimeService thread join failure!", e);
		}
	}

	@Override
	public void start() {
		refreshConfig();
        cleanLocalExcpProcessRunnable.start();
        scheduleCommandRunnable.start();
	}
	
	void refreshConfig(){

		refreshNode();
		threadPool.setCorePoolSize(node.getMaxTasks());
		threadPool.setMaximumPoolSize(node.getMaxTasks());
		logger.info("获取zk对象");
        zk=ZooKeeperUtil.getZookeeperClient(node.getServerCluster().getZookeeperCluster().getZookeeperString());
	}
	
	void refreshNode() {
		node = nodeService.getByCode(nodeService.getLocationNode());
		logger.info("节点信息获取成功");
	}
	
	class CleanLocalExcpProcessRunnable implements Runnable{

		@Override
		public void run() {
			while(true){
				if(file.exists()){
					logger.info("存在stop文件，退出本地异常进程清理线程");
					break;
				}
				try{
					List<String> excpCmds=ZooKeeperUtil.getChildren("/schedule/clean/"+nodeService.getLocationNode(), zk);
					logger.info("find "+(excpCmds!=null?excpCmds.size():0)+" excp commands"+excpCmds+",clean!");
					for(String excpCmd:excpCmds){
						RunShellUtil.killThread(excpCmd);
						ZooKeeperUtil.deleteNodeTenTimes("/schedule/clean/"+nodeService.getLocationNode()+"/"+excpCmd, zk);
					}
				}catch (Exception e) {
					logger.error("clean excp process error", e);
				}
				
				try {
					logger.info("clean thread sleep 30s");
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					logger.error("sleep failure", e);
				}
			}
		}
		
	}
	
	class ScheduleCommandRunnable implements Runnable{
		@Override
		public void run(){
			while(true){
				if(file.exists()){
					logger.info("存在stop文件，退出调度指令线程");
					break;
				}
				try{
					logger.info("获取本机步骤！");
					List<Step> localStepList = stepService.listStepByNodeCache(nodeService.getLocationNode());
					
					logger.info("获取本机步骤指令");
					// 获取本节点上的步骤执行
					getStepCmd2Start(localStepList);
					//如果获取集群任务
					if(node.getIsGetClusterTask()!=null&&"1".equals(node.getIsGetClusterTask())){
						logger.info("允许本机获取集群步骤");
						List<Step> clusterStepList = stepService.listStepByCluster(node.getServerCluster()
										.getServerClusterId());
						// 获取本节点对应集群上的步骤执行
						logger.info("获取集群步骤指令");
						getStepCmd2Start(clusterStepList);
					}

				}catch (Exception e) {
					logger.error("get wait cmd error!", e);
				}
				logger.info("The end of a cycle,sleep 10s");
				try {
					Thread.sleep(10000);
					logger.info("刷新配置");
					refreshConfig();
					logger.info("成功刷新配置");
				} catch (Exception e) {
					logger.error("刷新配置失败!", e);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param stepList
	 * @return
	 * @throws Exception
	 */
	void getStepCmd2Start(List<Step> stepList) throws Exception{
		if(stepList!=null&&!stepList.isEmpty()){
			for(Step localStep:stepList){
				while(threadPool.getActiveCount()>=node.getMaxTasks()){
					logger.info("thread pool full,active count:"+threadPool.getActiveCount()+",sleep 10s");
					Thread.sleep(10000);
				}
				
				Command cmd=commandService.getWaitedCommand(nodeService.getLocationNode(), localStep);
				if(cmd==null)
					continue;
				logger.info("获取到场景["+cmd.getSceneId()+"]步骤["+cmd.getStepId()+"]的指令["+cmd.getCmdId()+"]");
				RunWaitedCommandRunnable runnable = new RunWaitedCommandRunnable(cmd);
				logger.info("提交指令");
				threadPool.submit(runnable);
				Thread.sleep(500);
				logger.info("当前线程数量："+threadPool.getActiveCount());
			}
		}
	}

	class RunWaitedCommandRunnable implements Runnable{
		private Command cmd;
		public RunWaitedCommandRunnable(Command cmd){
			this.cmd=cmd;
		}
		@Override
		public void run(){
			runByProcess(cmd);
		}
	}
	
	/**
	 * 通过进程执行任务
	 * @param cmd
	 */
	private void runByProcess(Command cmd) {
		Process process = null;
		long startTime = System.currentTimeMillis();
		try { 
//			final ILogger logger = new FileLogger(node.getLogPath(), cmd.getCmdId());
			String cp = System.getProperty("java.class.path");
			String extp = System.getProperty("java.ext.dirs");
			String webAppDir = System.getProperty("webapp.root");
			String fileSeparator = System.getProperty("file.separator");
			if (webAppDir != null) {
				cp = webAppDir + "WEB-INF" + fileSeparator + "classes";
				extp = webAppDir + "WEB-INF" + fileSeparator + "lib";
			}
			cp = cp.replace(" ", ""); 
			
			String jvmParam = "";
			if (cmd.getMemMin() > 0) {
				jvmParam = jvmParam + " -Xms" + cmd.getMemMin() + "m";
			}
			if (cmd.getMemMax() > 0 && cmd.getMemMax() >= cmd.getMemMin()) {
				jvmParam = jvmParam + " -Xmx" + cmd.getMemMax() + "m";
			}

			String jvmCmd = "java -Dfile.encoding=UTF-8 " + jvmParam
					+ " -Djava.ext.dirs=" + extp + " -cp " + cp
					+ "  com.zbiti.etl.runtime.main.RunCommand "
					+ "/schedule/doing/"+cmd.getStepId()+"/"+cmd.getTaskSeries()+"_"+cmd.getCmdId()+" "+node.getLogPath();//传路径
			logger.info("执行命令:" + jvmCmd);
			
			process = Runtime.getRuntime().exec(jvmCmd);			
//			ExecutorService serviceNomal = Executors
//					.newSingleThreadExecutor();
//			ExecutorService serviceError = Executors
//					.newSingleThreadExecutor();
//			final BufferedReader bfNomal = new BufferedReader(
//					new InputStreamReader(process.getInputStream(), "UTF-8"));
//			final BufferedReader bfError = new BufferedReader(
//					new InputStreamReader(process.getErrorStream(), "UTF-8"));
//			serviceNomal.submit(new Callable<Object>() {
//				@Override
//				public Object call() throws Exception {
//					String s2 = null;
//					while ((s2 = bfNomal.readLine()) != null) {
//						logger.info(s2);
//					}
//					return null;
//				}
//
//			});
//
//			serviceError.submit(new Callable<Object>() {
//				@Override
//				public Object call() throws Exception {
//
//					String s = null;
//					while ((s = bfError.readLine()) != null) {
//						logger.info(s);
//					}
//					// bfNomal.close();
//					return null;
//				}
//			});
			process.waitFor();
//			ZooKeeperUtil.deleteNodeTenTimes("/schedule/doing/" + cmd.getStepId() + "/" + cmd.getTaskSeries()+"_"+cmd.getCmdId() + "/" + "wait", zk);
			long endTime = System.currentTimeMillis();
//			logger.info("do cmd used time = " + (endTime - startTime)
//					/ 1000 + "(s)");
			logger.info("任务["+"/schedule/doing/"+cmd.getStepId()+"/"+cmd.getTaskSeries()+"_"+cmd.getCmdId()+"]执行完成，耗时"+(endTime - startTime)
					/ 1000+"秒");
		} catch (IOException e) {
			logger.error("IOException");
			logger.error(e.getMessage(), e);
			
		} catch (Exception e) {
			logger.error("Exception");
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
