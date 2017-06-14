package com.zbiti.etl.runtime.impl;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.ISceneInsService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.smo.impl.FileDescQueue;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.CommandConstants;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;
import com.zbiti.etl.runtime.IProcess;

/**
 * 指令执行进程实现
 * 先在zookeeer目录下创建临时点,指令执行完成,再将临时点删除
 * @author yhp
 *
 */
@Service
public class CommandRunProcessImpl implements IProcess{

	private Log logger=LogFactory.getLog(CommandRunProcessImpl.class);
	@Autowired
	private ICommandService commandService;
	@Autowired
	IStepService stepService;
	@Autowired
	ISceneInsService sceneInsService;
	@Autowired
	INodeService nodeService;
	private Command command;
	private CuratorFramework zk;

	Future<Object> future;
	Object result = null;
	ExecutorService service;
	ICommandExecuter<Object> executer;
	ApplicationContext context;
	IFileDescQueue fileDescQueue;
	private Step step;
	private Node node;
	
	@Override
	public void cancel() {

		command.setStatus(CommandConstants.STATE_USER_CANCEL);
		if (future != null) {
			logger.info("取消应用程序");
			future.cancel(true);// 停止正在运行的程序
		} else {
			realease();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		this.context=ctx;
	}

	@Override
	public void start(Command cmd) {
		//获取zookeeper连接
		logger.info("获取zookeeper连接");
		node= nodeService.getByCode(nodeService.getLocationNode());
		zk=ZooKeeperUtil.getZookeeperClient(node.getServerCluster().getZookeeperCluster().getZookeeperString());
		
		this.command=cmd;
		if (command == null) {
			result = true;
			return;
		}
		//添加监听
		logger.info("添加用户行为监听");
		addStopNodeWatch();
		try {
			logger.info("运行任务前创建正在执行指令");
			commandService.createNodeForStart(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		//执行器
		service = Executors.newSingleThreadExecutor();
		logger.info("实例化执行执行Caller");
		CommandRunCaller caller = new CommandRunCaller();
		logger.info("提交caller");
		future = service.submit(caller);
	}

	private class CommandRunCaller implements Callable<Object> {
		@SuppressWarnings("unchecked")
		public Object call() throws Exception {

			try {
				String stepId = command.getStepId();
				logger.info("获取步骤信息！");
				step = stepService.getStepContainsNextById(stepId);
				logger.info("nextStep:"+step.getNextStep());
				String factoryClassName = step.getStepType().getFactoryClassName();
				logger.info("获取指令执行工厂");
				ICommandExecuterFactory factory = (ICommandExecuterFactory) context
						.getBean(factoryClassName);
				logger.info("创建指令执行器");
				executer = factory.createExecuter();
				logger.info("初始化队列");
				fileDescQueue=new FileDescQueue(step,commandService,command);
				
				logger.info("执行组件");
				logger.info("步骤["+step.getStepType().getStepTypeName()+"]开始执行");
				result = executer.execute(context,node,step,command,fileDescQueue);
				logger.info("步骤["+step.getStepType().getStepTypeName()+"]执行结束");
			} catch (Exception ex) {
				logger.error(ex);
				if(command.getExecInfo()==null||"".equals(command.getExecInfo()))
					command.setExecInfo(step.getStepType().getStepTypeName()+"执行异常："+ex.getMessage());
				throw ex;
			}
			return result;
		}
	}
	
	private void addStopNodeWatch(){
		final String commandPath = "/schedule/doing/" + command.getStepId()
				+ "/" + command.getTaskSeries()+"_"+command.getCmdId();
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
						logger.info("检测到用户发出停止指令，停止应用");
						cancel();
						childrenCache.getListenable().removeListener(this);
					}
				}
			}
		);
	}

	@Override
	public Object getResult() throws Exception {
		logger.info("获取结果");
		if (result != null) {
			return result;
		}
		try {
			if (future != null) {
				result = future.get();
				if (result == null)
					result = true;
			}
			logger.info("执行成功");
			command.setExecInfo("执行成功");
			command.setStatus(CommandConstants.STATE_SUCCESS);
		} catch (InterruptedException e) {
			command.setExecInfo("用户请求停止:"+e.getMessage());
			command.setStatus(CommandConstants.STATE_USER_CANCEL);
			logger.error("用户请求停止"+e.getMessage());
			throw e;
		} catch (ExecutionException e) {
			command.setStatus(CommandConstants.STATE_EXCEPTION);
			logger.error("异常终止！"+e.getMessage());
			throw e;
		} catch (CancellationException e) {
			command.setExecInfo("用户请求停止:"+e.getMessage());
			command.setStatus(CommandConstants.STATE_USER_CANCEL);
			logger.error("用户请求停止"+e.getMessage());
			throw e;
		} finally {
			realease();
		}
		return result;
	}
	

	
	public void realease() {
		logger.info("停止应用程序");
		if (executer != null) {
//			executer.onStop();
			try {
				if (CommandConstants.STATE_SUCCESS==command.getStatus()&&fileDescQueue != null){
					boolean isNext=fileDescQueue.clear();
					if(!isNext&&commandService.isTaskInsOver(step, command)){//如果没有推送下一步了,并且任务实例结束了
						//更改任务实例状态
						SceneIns si=new SceneIns();
						si.setTaskSeries(command.getTaskSeries());
						si.setEndTime(new Date());
						si.setTaskStatus(String.valueOf(command.getStatus()));
						si.setTaskResult(command.getExecInfo());
						sceneInsService.updateSceneIns(si);
					}
				}
			} catch (KeeperException e) {
				logger.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			executer = null;
		}
		if (service != null) {
			service.shutdown();
		}
		command.setEndTime(System.currentTimeMillis());
		try {
			
			commandService.deleteNodeForEnd(command);
		} catch (Exception e) {
			logger.error(command.getStepId()+"/"+command.getCmdId()+"结束删除节点失败！",e);
		}
		ZooKeeperUtil.realeaseZookeeper(zk);
		
	}
}
