package com.zbiti.etl.runtime.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.common.StringUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.smo.IJobScheduleService;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.IStepInsService;
import com.zbiti.etl.core.smo.ITaskService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepIns;
import com.zbiti.etl.runtime.ICommandService;
import com.zbiti.etl.runtime.IMasterRuntimeService;

@Service
public class MasterRuntimeServiceImpl implements IMasterRuntimeService{

	@Autowired
	INodeService nodeService;
	@Autowired
	ICommandService commandService;
	@Autowired
	IStepInsService stepInsService;
	@Autowired
	IJobScheduleService jobScheduleService;
	@Autowired
	ITaskService taskService;
	boolean isMaster = false;//判断本节点是否是管理节点的标志位
//	boolean isStarted= false;//判断本节点是否已经启动
	CuratorFramework zk;
	Node node;
	protected static final Log logger = LogFactory.getLog(MasterRuntimeServiceImpl.class);
	//竞争主节点
	Thread competeMasterRunnable=new Thread(new CompeteMasterRunnable());
	//历史指令入库
	Thread hisCommands2DBRunnable=new Thread(new HisCommands2DBRunnable());
	//异常指令入库
	Thread errorCommands2DBRunnable=new Thread(new ErrorCommands2DBRunnable());
	//将运行异常以及运行超时的任务移到异常节点、异常待清理节点（由子节点去清理）
	Thread runningExcp2ErrorRunnable=new Thread(new RunningExcp2ErrorRunnable());
	//装载定时器
	Thread timerManagerRunnable=new Thread(new TimerManagerRunnable());
	private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	File file=new File(StringUtil.getFilePath("stop"));
	
	@Override
	public void join() {
		try {
			competeMasterRunnable.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
        node=nodeService.getByCode(nodeService.getLocationNode());
        zk=ZooKeeperUtil.getZookeeperClient(node.getServerCluster().getZookeeperCluster().getZookeeperString());

		threadPool.setCorePoolSize(4);
		threadPool.setMaximumPoolSize(4);
        competeMasterRunnable.start();
	}

	class CompeteMasterRunnable implements Runnable{

		@Override
		public void run() {
			while(true){
				if(file.exists()&&threadPool.getActiveCount()==0){
					logger.info("存在stop文件并且活跃线程数为0，退出主节点竞争");
					break;
				}
				competeMaster();
				
				if(isMaster&&threadPool.getActiveCount()==0){
					logger.info("开启主节点任务");
					threadPool.submit(hisCommands2DBRunnable);
					threadPool.submit(errorCommands2DBRunnable);
					threadPool.submit(runningExcp2ErrorRunnable);
					threadPool.submit(timerManagerRunnable);
//					hisCommands2DBRunnable.start();
//					errorCommands2DBRunnable.start();
//					runningExcp2ErrorRunnable.start();
//					timerManagerRunnable.start();
				}
				
				try {
					logger.info("30秒之后扫描主节点...");
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
			}
		}
		
	}
	void competeMaster(){
		try {
			String master=ZooKeeperUtil.getData("/schedule/master", zk);
			if(master==null||"".equals(master)){
				logger.info("当前没有主节点，竞争主节点");
				ZooKeeperUtil.createTempNode("/schedule/master", nodeService.getLocationNode(), zk);
				isMaster = true;
				logger.info("竞争成功，我是主节点，我是"+nodeService.getLocationNode());
			}else{
				if(master.equals(nodeService.getLocationNode())){
					logger.info("我是主节点，我是"+nodeService.getLocationNode());
					isMaster=true;
				}else{
					logger.info("当前主节点："+master);
					isMaster=false;
				}
			}
		} catch (KeeperException e) {
			isMaster = false;
		} catch (InterruptedException e) {
			isMaster = false;
		} catch (Exception e) {
			isMaster = false;
		}
	}
	
	/**
	 * 历史to DB线程
	 * @author yhp
	 *
	 */
//	class HisCommands2DBRunnable implements Runnable{
//
//		@Override
//		public void run() {
//			while(true){
//				List<Command> cmdList=commandService.listHisCommands();
//				int size=cmdList!=null?cmdList.size():0;
//				logger.info("find his cmd:"+size+",begin insert ETL_STEP_INS...");
////				stepInsService.saveCmdList(cmdList);
//				if(size>0){
//					List<StepIns> stepInsList=new ArrayList<StepIns>();
//					for(int i=0;i<size;i++){
//						Command cmd=cmdList.get(i);
//						StepIns stepIns=new StepIns();
//						cmd2StepIns(cmd, stepIns);
//						stepIns.setStatus("3");
//						stepInsList.add(stepIns);
//						if((i!=0&&i%10==0)||i==size-1){
//							try{
//								stepInsService.saveIntoStepInsBatch(stepInsList);
//								for(StepIns stepIns_:stepInsList){
//									ZooKeeperUtil.deleteNode("/schedule/his/"+ stepIns_.getStepSeries(),zk);
////											+ stepIns_.getRunPosition() + "/"
////											+ stepIns_.getStep().getStepId()
//											
//								}
//							}catch (Exception e) {
//								logger.error(e.getMessage());
//							}
//							stepInsList=new ArrayList<StepIns>();
//							
//						}
//					}
//				}
//				logger.info(size+" item cmd insert ETL_STEP_INS over,sleep 30s...");
//				try {
//					Thread.sleep(30000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//	}
	
	class HisCommands2DBRunnable implements Runnable{

		@Override
		public void run() {
			while(true){
				if(file.exists()||!isMaster){
					logger.info("存在stop文件或非主节点，历史指令入库线程退出");
					break;
				}
				try {
					List<String> cmdStrs=ZooKeeperUtil.getChildren("/schedule/his",zk);
					int size=cmdStrs!=null?cmdStrs.size():0;
					logger.info("find his cmd:"+size+",begin insert ETL_STEP_INS...");
					if(size>0){
						List<StepIns> stepInsList=new ArrayList<StepIns>();
						for(int i=0;i<size;i++){
							String cmdData=ZooKeeperUtil.getData("/schedule/his/"+cmdStrs.get(i), zk);
							Command cmd=JSONUtil.parse(cmdData,Command.class);
							StepIns stepIns=new StepIns();
							cmd2StepIns(cmd, stepIns);
							stepInsList.add(stepIns);
							if((i!=0&&i%10==0)||i==size-1){
								try{
									stepInsService.saveIntoStepInsBatch(stepInsList);
									for(StepIns stepIns_:stepInsList){
										ZooKeeperUtil.deleteNodeTenTimes("/schedule/his/"+stepIns_.getSceneIns().getTaskSeries()+"_"+ stepIns_.getStepSeries(),zk);
									}
								}catch (Exception e) {
									logger.error(e.getMessage());
								}
								stepInsList=new ArrayList<StepIns>();
								
							}
						}
					}
					logger.info(size+" item cmd insert ETL_STEP_INS over,sleep 30s...");
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
//					e.printStackTrace();
				}
				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	void cmd2StepIns(Command cmd,StepIns stepIns){
		if(cmd.getDispatchTime()>0)
			stepIns.setDispatchTime(new Date(cmd.getDispatchTime()));
		if(cmd.getEndTime()>0)
			stepIns.setEndTime(new Date(cmd.getEndTime()));
		stepIns.setExecInfo(cmd.getExecInfo());
		stepIns.setInnerTasks(cmd.getInnerTasks());
		stepIns.setPrevStepSeries(cmd.getPreCmdId());
		stepIns.setRunPosition(cmd.getRunPosition());
		SceneIns sceneIns=new SceneIns();
		sceneIns.setTaskSeries(cmd.getTaskSeries());
		stepIns.setSceneIns(sceneIns);
		if(cmd.getStartTime()>0)
			stepIns.setStartTime(new Date(cmd.getStartTime()));
		stepIns.setStepSeries(cmd.getCmdId());
		Step step=new Step();
		step.setStepId(cmd.getStepId());
		stepIns.setStep(step);
		stepIns.setStatus(String.valueOf(cmd.getStatus()));
	}
	/**
	 * 运行异常的到异常节点，并写入异常终止节点，由从节点扫描到把垃圾进程清理掉
	 * 包含2种情况1、运行节点下无内容，2、运行时间超过阀值
	 * @author yhp
	 *
	 */
	class RunningExcp2ErrorRunnable implements Runnable{

		@Override
		public void run() {
			while(true){
				if(file.exists()||!isMaster){
					logger.info("存在stop文件或非主节点，扫描运行异常指令线程退出");
					break;
				}
				try {
					logger.info("start to scan running exception task to error!");
					commandService.runningExcp2Error();
				} catch (Exception e) {
					e.printStackTrace();
				}

				logger.info("end to scan running exception task to error!sleep 5 minites!");
				try {
					Thread.sleep(1*60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * 异常指令入库，并移植到已入库异常指令节点下，为了能够重新运行、忽略等操作
	 * @author yhp
	 *
	 */
	class ErrorCommands2DBRunnable implements Runnable{
		@Override
		public void run(){
			while(true){
				if(file.exists()||!isMaster){
					logger.info("存在stop文件或非主节点，异常指令入库线程退出");
					break;
				}
				try {
					List<String> cmdStrs=ZooKeeperUtil.getChildren("/schedule/error",zk);
					int size=cmdStrs!=null?cmdStrs.size():0;
					logger.info("find error cmd:"+size+",begin insert ETL_STEP_INS...");
					if(size>0){
						List<StepIns> stepInsList=new ArrayList<StepIns>();
						List<Command> cmdList=new ArrayList<Command>();
						for(int i=0;i<size;i++){
							String cmdData=ZooKeeperUtil.getData("/schedule/error/"+cmdStrs.get(i), zk);
							Command cmd=JSONUtil.parse(cmdData,Command.class);
							cmdList.add(cmd);
							StepIns stepIns=new StepIns();
							cmd2StepIns(cmd, stepIns);
							stepInsList.add(stepIns);
							if((i!=0&&i%10==0)||i==size-1){
								try{
									stepInsService.saveIntoStepInsBatch(stepInsList);
									for(Command cmd_:cmdList){
										ZooKeeperUtil.createNode("/schedule/hiserror/"+cmd_.getTaskSeries()+"_"+cmd_.getCmdId(), JSONUtil.toJsonString(cmd_), zk);
										ZooKeeperUtil.deleteNodeTenTimes("/schedule/error/"+cmd_.getTaskSeries()+"_"+cmd_.getCmdId(),zk);
									}
								}catch (Exception e) {
									logger.error(e.getMessage());
								}
								stepInsList=new ArrayList<StepIns>();
								cmdList=new ArrayList<Command>();
							}
						}
					}
					logger.info(size+" item cmd insert ETL_STEP_INS over,sleep 30s...");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class TimerManagerRunnable implements Runnable{
		@Override
		public void run(){
			if(file.exists()||!isMaster){
				return;
			}
			try{
				List<Command> runningTimerList=commandService.listRunningTimer();
				if(runningTimerList!=null&&runningTimerList.size()>0){
					//将运行中的指令加入定时器
					for(Command runningCmd:runningTimerList){
						try{
							jobScheduleService.loadingTimer(runningCmd);
						}catch (Exception e) {
							logger.error(e.getMessage(),e);
							continue;
						}

//						addStopNodeWatch(runningCmd);
					}
				}
			}catch (Exception e) {
				logger.error(e.getMessage());
			}
			//间歇性扫描等待运行的定时任务
			while(true){
				if(file.exists()||!isMaster){
					logger.info("存在stop文件或非主节点，定时装载线程退出");

					logger.info("线程退出需移除已装载的定时");
					removeTimerFromMemory();
					break;
				}
				//扫描正在运行的定时是否有停止动作
				logger.info("扫描正在运行的定时是否有停止动作");
				scanTimerRunningStop();
				//装载等待运行的定时
				logger.info("装载等待的定时");
				loadWaitTimer();
				try {
					logger.info("扫描等待运行的定时任务完成，休眠10s");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void scanTimerRunningStop(){
		try{
			List<Command> runningTimerList=commandService.listRunningTimer();
			if(runningTimerList!=null&&runningTimerList.size()>0){
				for(Command runningCmd:runningTimerList){
					//如果用户停止
					if(ZooKeeperUtil.isNodeExists("/schedule/timer/doing/"+runningCmd.getSceneId()+"/stop", zk)){
						logger.info("场景["+runningCmd.getSceneId()+"]检测到停止指令，移除定时");
						jobScheduleService.deleteJob(runningCmd);
						commandService.removeRunningTimer(runningCmd);
					}

				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void loadWaitTimer(){
		try{
			List<Command> waitTimerList=commandService.listWaitTimer();
			if(waitTimerList!=null&&!waitTimerList.isEmpty()){
				for(Command cmd:waitTimerList){
					if(jobScheduleService.checkJob(cmd)){
						//如果存在则发出停止指令，等待下一次装载
						logger.info("场景["+cmd.getSceneId()+"]已有定时运行，执行移除操作...");
//						commandService.stopTimer(cmd.getSceneId());
						jobScheduleService.deleteJob(cmd);
						commandService.removeRunningTimer(cmd);
						continue;
					}
					logger.info("装载场景["+cmd.getSceneId()+"]定时指令："+cmd.getCmdId());
					Scene scene=new Scene();
					scene.setSceneId(cmd.getSceneId());
					try{
						jobScheduleService.loadingTimer(cmd);
					}catch (Exception e) {
						logger.error("装载场景["+cmd.getSceneId()+"]定时指令["+cmd.getCmdId()+"]报错，移除",e);
						ZooKeeperUtil.deleteNodeTenTimes("/schedule/timer/wait/"+cmd.getCmdId(),zk);
						scene.setStartStatus("3");//启动失败
						scene.setStartLog(e.getMessage());
						taskService.update(scene);
						continue;
					}
					ZooKeeperUtil.createNode("/schedule/timer/doing/"+cmd.getSceneId(),null,zk);
					ZooKeeperUtil.createNode("/schedule/timer/doing/"+cmd.getSceneId()+"/"+cmd.getCmdId(), JSONUtil.toJsonString(cmd), zk);
					ZooKeeperUtil.deleteNodeTenTimes("/schedule/timer/wait/"+cmd.getCmdId(),zk);
					//注释原因：减少zookeeper监听。
//					addStopNodeWatch(cmd);
					
					scene.setStartStatus("4");//已启动
					taskService.update(scene);
				}
			}
		}catch (Exception e) {
			logger.error("扫描等待运行任务报错！",e);
		}
	}
	
	private void removeTimerFromMemory(){
		try{
			logger.info("开始移除定时！");
			List<Command> runningTimerList=commandService.listRunningTimer();
			if(runningTimerList!=null&&runningTimerList.size()>0){
				
				for(Command runningCmd:runningTimerList){
					try{
						logger.info("移除定时："+runningCmd.getSceneId());
						jobScheduleService.deleteJob(runningCmd);
					}catch (Exception e) {
						logger.error(e.getMessage(),e);
						continue;
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void removeStopNodeWatch(final Command command){
		final String commandPath ="/schedule/timer/doing/"+command.getSceneId();
		final PathChildrenCache childrenCache = new PathChildrenCache(zk,
				commandPath, true);
		childrenCache.getListenable().clear();
	}
	private void addStopNodeWatch(final Command command){
		final String commandPath ="/schedule/timer/doing/"+command.getSceneId();
		final PathChildrenCache childrenCache = new PathChildrenCache(zk,
				commandPath, true);
		try {
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		childrenCache.getListenable().addListener(
			new PathChildrenCacheListener() {
				@Override
				public void childEvent(CuratorFramework zk,
						PathChildrenCacheEvent event) throws Exception {
					if (event.getType() == Type.CHILD_ADDED
							&& (commandPath + "/stop").equals(event
									.getData().getPath())) {
						logger.info("场景["+command.getSceneId()+"]检测到停止指令，移除定时");
						
						jobScheduleService.deleteJob(command);;
						commandService.removeRunningTimer(command);
						childrenCache.getListenable().removeListener(this);
					}
				}
			}
		);
	}
}
