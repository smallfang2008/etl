package com.zbiti.etl.runtime.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.common.TimeUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.CommandConstants;
import com.zbiti.etl.core.vo.CommandShow;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;

import edu.emory.mathcs.backport.java.util.Collections;

@Service
public class CommandServiceImpl implements ICommandService,InitializingBean{

	private Log logger=LogFactory.getLog(CommandServiceImpl.class);
	@Autowired
	IStepService stepService;
	@Autowired
	INodeService nodeService;
	CuratorFramework zk;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Node node=nodeService.getByCode(nodeService.getLocationNode());
		zk=ZooKeeperUtil.getZookeeperClient(node.getServerCluster().getZookeeperCluster().getZookeeperString());
	}
	@Override
	public List<Command> listRunningTimer() throws Exception{
		List<Command> cmdList = new ArrayList<Command>();
		String runningTimerPath="/schedule/timer/doing";
		if (!ZooKeeperUtil.isNodeExists(runningTimerPath, zk))
			return cmdList;
		List<String> sceneIdList = ZooKeeperUtil.getChildren(runningTimerPath,zk);
		if(sceneIdList==null||sceneIdList.isEmpty())
			return cmdList;
		for(String sceneId:sceneIdList){
			List<String> commands = ZooKeeperUtil.getChildren(runningTimerPath+"/"+sceneId, zk);
			if(commands==null||commands.size()==0)
				continue;
			for (String cmdId : commands) {
				String cmdStr = ZooKeeperUtil.getData(runningTimerPath+"/"+sceneId+"/" + cmdId, zk);
				if (cmdStr != null) {
					Command cmd = JSONUtil.parse(cmdStr, Command.class);
					if(cmd!=null){
						cmdList.add(cmd);
						break;
					}
				}
			}
		}
		return cmdList;
	}


	@Override
	public List<Command> listWaitTimer() throws Exception{
		List<Command> cmdList = new ArrayList<Command>();
		String runningTimerPath="/schedule/timer/wait";
		if (!ZooKeeperUtil.isNodeExists(runningTimerPath, zk))
			return cmdList;
		List<String> waitList = ZooKeeperUtil.getChildren(runningTimerPath,zk);
		if(waitList==null||waitList.isEmpty())
			return cmdList;
		for(String waitCmd:waitList){
			String cmdStr = ZooKeeperUtil.getData(runningTimerPath+"/" + waitCmd, zk);
			if (cmdStr != null) {
				Command cmd = JSONUtil.parse(cmdStr, Command.class);
				if(cmd!=null){
					cmdList.add(cmd);
				}
			}
		}
		return cmdList;
	}
	@Override
	public List<Command> listHisCommands() throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runningExcp2Error() throws Exception{
		List<String> stepIds = ZooKeeperUtil.getChildren("/schedule/doing",zk);
		for(String stepId:stepIds){
			List<String> cmdIds = ZooKeeperUtil.getChildren("/schedule/doing/"+stepId, zk);
			for(String cmdId:cmdIds){
				moveDoingNodeToError(cmdId, stepId);
			}
		}
	}
	
	public void moveDoingNodeToError(String cmdId, String stepId) throws Exception{
		String cmdPath="/schedule/doing/" + stepId + "/" + cmdId;
		
		
		
		String commandStr = ZooKeeperUtil.getData(cmdPath,zk);
		if(commandStr==null)
			return;

//		int cmdChildLength = ZooKeeperUtil.getChildLength(cmdPath, zk);
//		logger.info(commandStr);
		Command cmd = JSONUtil.parse(commandStr, Command.class);
		boolean flag=false;
//		if(cmdChildLength>0){//子节点不为空则正在运行，正在运行检查运行时间
			
			// 如果指令开始时间与当前时间比较超过2小时，移到异常
			if (cmd.getStartTime() != 0
					&& (System.currentTimeMillis() - cmd.getStartTime()) / 1000 > 12 * 60 * 60) {
				cmd.setExecInfo("执行时间超过阀值");
				cmd.setStatus(CommandConstants.STATE_TIMEOUT);
				flag=true;
			}else{
				String cmdStop=cmdPath+"/stop";
				Stat stat=ZooKeeperUtil.getNodeStat(cmdStop, zk);
				if(stat!=null){
					if(System.currentTimeMillis()-stat.getCtime()>5*60*1000){//如果stop写入超过5分钟
						cmd.setExecInfo("停止时间超过阀值");
						cmd.setStatus(CommandConstants.STATE_STOP_TIMEOUT);
						flag=true;
					}
				}
			}
			
			if(flag){

				//写入异常节点
				ZooKeeperUtil.createNode("/schedule/error/" + cmd.getTaskSeries()+"_"+cmd.getCmdId(),
						JSONUtil.toJsonString(cmd), zk);
				ZooKeeperUtil.createNode("/schedule/clean/"+cmd.getRunPosition(),null,zk);
				//写入异常终止节点
				ZooKeeperUtil.createNode("/schedule/clean/"+cmd.getRunPosition()+"/"+cmd.getTaskSeries()+"_"+cmd.getCmdId(), null, zk);
				//清除临时节点
				ZooKeeperUtil.deleteNodeTenTimes(cmdPath + "/" + "wait", zk);
				ZooKeeperUtil.deleteNodeTenTimes(cmdPath + "/" + "temp", zk);
				ZooKeeperUtil.deleteNodeTenTimes(cmdPath + "/" + "stop", zk);
				//清除指令节点
				ZooKeeperUtil.deleteNodeTenTimes(cmdPath, zk);
			}
//		}
//		else{
//			cmd.setExecInfo("指令运行进程卡死");
//			cmd.setStatus(CommandConstants.STATE_PROCESS_DEAD);
//			//写入异常节点
//			ZooKeeperUtil.createNode("/schedule/error/" + cmd.getTaskSeries()+"_"+cmd.getCmdId(),JSONUtil.toJsonString(cmd), zk);
//			//写入异常终止节点
//			ZooKeeperUtil.createNode("/schedule/clean/"+cmd.getRunPosition(),null,zk);
//			ZooKeeperUtil.createNode("/schedule/clean/"+cmd.getRunPosition()+"/"+cmd.getTaskSeries()+"_"+cmd.getCmdId(), null, zk);
//			//清除指令节点
//			ZooKeeperUtil.deleteNode(cmdPath, zk);
//		}
	}
//	@Override
//	public Command createCmdByStep(String taskSeries,Step step,Map<String,Object> param) {
//		Command cmd=new Command();
//		cmd.setCmdId(getCommandId());
//		cmd.setDispatchTime(System.currentTimeMillis());
//		cmd.setMemMax(step.getMemMax());
//		cmd.setMemMin(step.getMemMin());
//		cmd.setPreCmdId("-1");
//		cmd.setSceneId(step.getScene().getSceneId());
//		cmd.setStepId(step.getStepId());
//		cmd.setTaskSeries(taskSeries);
//		cmd.setParam(param);
//		return cmd;
//	}
	
	/**
	 * 获取消息节点的id，当前服务器时间 yyMMddHHmmss+4位随机数
	 * @return
	 */
	@Override
	public String getCommandId(){
		return TimeUtil.getNowDateTime("yyMMddHHmmss")+TimeUtil.getRandom(4);
	}
	@Override
	public void createNodeForWait(String taskSeries,String preCmdId,Step step,Map<String,Object> param) throws Exception{
		Command cmd=new Command();
		cmd.setCmdId(getCommandId());
		cmd.setDispatchTime(System.currentTimeMillis());
		cmd.setMemMax(step.getMemMax());
		cmd.setMemMin(step.getMemMin());
		cmd.setPreCmdId(preCmdId);
		cmd.setSceneId(step.getScene().getSceneId());
		cmd.setStepId(step.getStepId());
		cmd.setTaskSeries(taskSeries);
		cmd.setParam(param);
		String runPosition=step.getRunPositionType().equals("1")?step.getServerCluster().getServerClusterId():step.getNode().getNodeCode();
		String path="/schedule/wait/"+runPosition;//+"/"+step.getStepId()+"/"+cmd.getCmdId();
		ZooKeeperUtil.createNode(path, null, zk);
		ZooKeeperUtil.createNode(path+"/"+step.getStepId(), null, zk);
		ZooKeeperUtil.createNodeE(path+"/"+step.getStepId()+"/"+cmd.getTaskSeries()+"_"+cmd.getCmdId(),JSONUtil.toJsonString(cmd), zk);
	}
	@Override
	public boolean isNodeExists(String path) throws Exception{
		return ZooKeeperUtil.isNodeExists(path, zk);
	}
	@Override
	public List<String> getChildren(String path) throws Exception{
		return ZooKeeperUtil.getChildren(path, zk);
	}
	@Override
	public void removeRunningTimer(Command cmd) throws Exception{
		List<String> runningTimers=getChildren("/schedule/timer/doing/"+cmd.getSceneId());
		if(runningTimers!=null&&runningTimers.size()>0){
			for(String runningTimer:runningTimers){
				ZooKeeperUtil.deleteNodeTenTimes("/schedule/timer/doing/"+cmd.getSceneId()+"/"+runningTimer, zk);
			}
		}
	}
	@Override
	public Command getWaitedCommand(String nodeCode,Step step) throws Exception{
		String position=step.getRunPositionType().equals("1")?step.getServerCluster().getServerClusterId():step.getNode().getNodeCode();
		String waitPath="/schedule/wait/"+position+"/"+step.getStepId();
		boolean lock = ZooKeeperUtil.getLock(waitPath,nodeCode, zk);// 做之前加锁
		if (!lock)
			return null;
		try{
			String doingPath="/schedule/doing/"+step.getStepId();
			ZooKeeperUtil.createNode(doingPath,null, zk);
			
			//判断正在运行的任务是否达到阀值
			int runSize=ZooKeeperUtil.getChildLength(doingPath, zk);
			if(runSize>=step.getThreadNum())
				return null;
			//如果等待前置任务执行完成
			if(step.getIsWaitPre()==1){
				if(hasPreCommand(step)){
					return null;
				}
			}
			List<String> commands = ZooKeeperUtil.getChildren(waitPath, zk);
			if(commands==null||commands.isEmpty())
				return null;
			Collections.sort(commands);
			for(String cmdId:commands){
				String cmdStr=ZooKeeperUtil.getData(waitPath+ "/" + cmdId, zk);
				Command cmd = JSONUtil.parse(cmdStr, Command.class);
				cmd.setRunPosition(nodeCode);
				cmd.setStartTime(System.currentTimeMillis());
				ZooKeeperUtil.createNode(doingPath + "/" + cmdId, JSONUtil.toJsonString(cmd), zk);
				ZooKeeperUtil.createTempNode(doingPath + "/" + cmdId + "/" + "wait", null, zk);
				ZooKeeperUtil.deleteNodeTenTimes(waitPath + "/" + cmdId, zk);
				if(cmd!=null)
					return cmd;
			}
		}catch (Exception e) {
			throw e;
		}finally{
			ZooKeeperUtil.releaseLock(waitPath, zk);// 结束之后解锁
		}
		return null;
	}
	
	/**
	 * 判断逻辑：
	 * 前置步骤没有等待、正在运行的任务
	 * @param step
	 * @return
	 */
	public boolean hasPreCommand(Step step){
		List<Step> preStepList=stepService.listPreStep(step);
		if(preStepList==null||preStepList.size()==0)
			return false;
		for(Step preStep:preStepList){
			String position=preStep.getRunPositionType().equals("1")?preStep.getServerCluster().getServerClusterId():preStep.getNode().getNodeCode();
			String waitPath="/schedule/wait/"+position+"/"+preStep.getStepId();
			if(ZooKeeperUtil.getChildLength(waitPath, zk)>0)
				return true;
			String doingPath="/schedule/doing/"+preStep.getStepId();
			if(ZooKeeperUtil.getChildLength(doingPath, zk)>0){
				return true;
			}
		}
		return false;
	}
	public Command getCommandByPath(String path) throws Exception{
		return JSONUtil.parse(ZooKeeperUtil.getData(path, zk),Command.class);
	}
	
	public void createNodeForStart(Command cmd) throws Exception{
		logger.info("任务启动,开始创建ZooKeeper节点");
		try {
			ZooKeeperUtil.createTempNode("/schedule/doing/" + cmd.getStepId()
					+ "/" + cmd.getTaskSeries()+"_"+cmd.getCmdId() + "/" + "temp", null, zk);
		} catch (Exception e) {
			throw e;
		} 
		logger.info("任务启动,完成创建ZooKeeper节点");
	}
	@Override
	public void deleteNodeForEnd(Command cmd) throws Exception {
		logger.info(" cmd end ,start delete the zookeeper node ......");
		String path = null;
		if (cmd.getStatus() == CommandConstants.STATE_EXCEPTION) {
			path = "/schedule/error/";
		} else if (cmd.getStatus() == CommandConstants.STATE_USER_CANCEL) {
			path = "/schedule/error/";
		} else if (cmd.getStatus() == CommandConstants.STATE_SUCCESS) {
			path = "/schedule/his/";
		} else if (cmd.getStatus() == CommandConstants.STATE_RUN) {
			path = "/schedule/error/";
		}
		logger.info(" rund result write into :" + path);
		ZooKeeperUtil.createNode(path +cmd.getTaskSeries()+"_"+ cmd.getCmdId(), JSONUtil
				.toJsonString(cmd), zk);
		String doingPath="/schedule/doing/" + cmd.getStepId() + "/"+ cmd.getTaskSeries()+"_"+cmd.getCmdId();
		ZooKeeperUtil.deleteNodeTenTimes(doingPath + "/" + "wait", zk);
		ZooKeeperUtil.deleteNodeTenTimes(doingPath + "/" + "temp", zk);
		ZooKeeperUtil.deleteNodeTenTimes(doingPath +  "/" + "stop", zk);
		ZooKeeperUtil.deleteNodeTenTimes(doingPath, zk);
		logger.info("cmd finished delete the node ");
	}
	@Override
	public boolean isTaskInsOver(Step step, Command cmd) {
		List<Step> stepList=stepService.listStepBySceneIdCache(step.getScene().getSceneId());
		for(Step stepTmp:stepList){
			String position=stepTmp.getRunPositionType().equals("1")?stepTmp.getServerCluster().getServerClusterId():stepTmp.getNode().getNodeCode();
			String waitPath="/schedule/wait/"+position+"/"+stepTmp.getStepId();
			List<String> stepWaitList=ZooKeeperUtil.getChildren(waitPath, zk);
			if(stepWaitList!=null&&stepWaitList.size()>0){
				for(String stepWait:stepWaitList){
					if(stepWait.contains(cmd.getTaskSeries())){
						return false;
					}
				}
			}
			String doingPath="/schedule/doing/"+stepTmp.getStepId();
			List<String> stepDoingList=ZooKeeperUtil.getChildren(doingPath, zk);
			if(stepDoingList!=null&&stepDoingList.size()>0){
				for(String stepDoing:stepDoingList){
					if(!stepDoing.contains(cmd.getCmdId())&&stepDoing.contains(cmd.getTaskSeries())){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void createWaitTimer(Scene scene){
		Command cmd=new Command();
		cmd.setCmdId(getCommandId());
		cmd.setDispatchTime(System.currentTimeMillis());
		cmd.setSceneId(scene.getSceneId());
		String cmdStr=JSONUtil.toJsonString(cmd);
		String waitTimerPath="/schedule/timer/wait/"+cmd.getCmdId();
		ZooKeeperUtil.createNode(waitTimerPath, cmdStr, zk);
	}
	@Override
	public void stopTimer(String sceneId) {
		ZooKeeperUtil.createNode("/schedule/timer/doing/"+sceneId+"/stop", null, zk);
	}
	
	@Override
	public String getMasterNode(){
		return ZooKeeperUtil.getData("/schedule/master", zk);
	}
	
	@Override
	public PageQueryResult<CommandShow> selectTaskDoingListPage(List<Step> stepList,Map<String, String> param){
		List<CommandShow> commandShowList=new ArrayList<CommandShow>();
		String rootPath="/schedule/doing";
		for(Step step:stepList){
			List<String> commandList=ZooKeeperUtil.getChildren(rootPath+"/"+step.getStepId(), zk);
			if(commandList!=null&&commandList.size()>0){
				for(String cmdId:commandList){
					String[] cmdArray=cmdId.split("_");
					if(cmdArray.length<2)
						continue;
					if(!cmdArray[0].contains(param.get("taskSeries")))
						continue;
					if(!cmdArray[1].contains(param.get("stepSeries")))
						continue;
					String cmdStr=ZooKeeperUtil.getData(rootPath+"/"+step.getStepId()+"/"+cmdId, zk);
//					logger.info(cmdStr);
					CommandShow cmd=JSONUtil.parse(cmdStr, CommandShow.class);
					if(!cmd.getPreCmdId().contains(param.get("prevStepSeries")))
						continue;
					if(!cmd.getRunPosition().contains(param.get("nodeCode")))
						continue;
					cmd.setParam(new HashMap<String, Object>());
					cmd.setStepName(step.getStepName());
					cmd.setStepTypeName(step.getStepType().getStepTypeName());
					cmd.setSceneName(step.getScene().getName());
					commandShowList.add(cmd);
				}
			}
		}
		//倒叙排序
		Collections.sort(commandShowList,new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
				return -o1.getCmdId().compareTo(o2.getCmdId());
			}
		});
		int start=Integer.parseInt(param.get("pageIndex"))*Integer.parseInt(param.get("rows"));
		int end=(Integer.parseInt(param.get("pageIndex"))+1)*Integer.parseInt(param.get("rows"));
		if(end>commandShowList.size())
			end=commandShowList.size();
		List<CommandShow> resultList=new ArrayList<CommandShow>();
		for(int i=start;i<end;i++){
			resultList.add(commandShowList.get(i));
		}
		
		return new PageQueryResult<CommandShow>(resultList, commandShowList.size());
	}
	
	@Override
	public PageQueryResult<CommandShow> selectTaskWaitListPage(List<Step> stepList,Map<String, String> param){
		List<CommandShow> commandShowList=new ArrayList<CommandShow>();
		String rootPath="/schedule/wait/";
		if(param.get("nodeCode")!=null&&!"".equals(param.get("nodeCode"))){
			rootPath+=param.get("nodeCode");
		}else{
			rootPath+=param.get("serverClusterId");
		}
		for(Step step:stepList){
			List<String> commandList=ZooKeeperUtil.getChildren(rootPath+"/"+step.getStepId(), zk);
			if(commandList!=null&&commandList.size()>0){
				for(String cmdId:commandList){
					String[] cmdArray=cmdId.split("_");
					if(cmdArray.length<2)
						continue;
					if(!cmdArray[0].contains(param.get("taskSeries")))
						continue;
					String cmdStr=ZooKeeperUtil.getData(rootPath+"/"+step.getStepId()+"/"+cmdId, zk);
//					logger.info(cmdStr);
					if(cmdStr==null||"".equals(cmdStr))
						continue;
					CommandShow cmd=JSONUtil.parse(cmdStr, CommandShow.class);
					cmd.setParam(null);
					cmd.setStepName(step.getStepName());
					cmd.setStepTypeName(step.getStepType().getStepTypeName());
					cmd.setSceneName(step.getScene().getName());
					cmd.setRunPosition(step.getRunPositionType().equals("1")?step.getServerCluster().getServerClusterName():step.getNode().getNodeCode());
					cmd.setRunPositionId(step.getRunPositionType().equals("1")?step.getServerCluster().getServerClusterId():step.getNode().getNodeCode());
					commandShowList.add(cmd);
				}
			}
		}
		//倒叙排序
		Collections.sort(commandShowList,new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
				return -o1.getCmdId().compareTo(o2.getCmdId());
			}
		});
		int start=Integer.parseInt(param.get("pageIndex"))*Integer.parseInt(param.get("rows"));
		int end=(Integer.parseInt(param.get("pageIndex"))+1)*Integer.parseInt(param.get("rows"));
		if(end>commandShowList.size())
			end=commandShowList.size();
		List<CommandShow> resultList=new ArrayList<CommandShow>();
		for(int i=start;i<end;i++){
			resultList.add(commandShowList.get(i));
		}
		
		return new PageQueryResult<CommandShow>(resultList, commandShowList.size());
	}
	
	@Override
	public PageQueryResult<CommandShow> selectTaskErrorListPage(List<Step> stepList,Map<String, String> param){
		List<CommandShow> commandShowList=new ArrayList<CommandShow>();
		String rootPath="/schedule/hiserror";
		for(Step step:stepList){
			List<String> commandList=ZooKeeperUtil.getChildren(rootPath, zk);
			if(commandList!=null&&commandList.size()>0){
				for(String cmdId:commandList){
					String[] cmdArray=cmdId.split("_");
					if(cmdArray.length<2)
						continue;
					if(!cmdArray[0].contains(param.get("taskSeries")))
						continue;
					if(!cmdArray[1].contains(param.get("stepSeries")))
						continue;
					CommandShow cmd=JSONUtil.parse(ZooKeeperUtil.getData(rootPath+"/"+step.getStepId()+"/"+cmdId, zk), CommandShow.class);
					if(!cmd.getPreCmdId().contains(param.get("prevStepSeries")))
						continue;
					if(!cmd.getRunPosition().contains(param.get("nodeCode")))
						continue;
					cmd.setStepName(step.getStepName());
					cmd.setStepTypeName(step.getStepType().getStepTypeName());
					cmd.setSceneName(step.getScene().getName());
					commandShowList.add(cmd);
				}
			}
		}
		//倒叙排序
		Collections.sort(commandShowList,new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
				return -o1.getCmdId().compareTo(o2.getCmdId());
			}
		});
		int start=Integer.parseInt(param.get("pageIndex"))*Integer.parseInt(param.get("rows"));
		int end=(Integer.parseInt(param.get("pageIndex"))+1)*Integer.parseInt(param.get("rows"));
		if(end>commandShowList.size())
			end=commandShowList.size();
		List<CommandShow> resultList=new ArrayList<CommandShow>();
		for(int i=start;i<end;i++){
			resultList.add(commandShowList.get(i));
		}
		
		return new PageQueryResult<CommandShow>(commandShowList, commandShowList.size());
	}
	
	public static void main(String[] args) {
		String cmdStr="{\"cmdId\":\"1610200940208c8b\",\"dispatchTime\":1476927620514,\"endTime\":0,\"execInfo\":\"\",\"innerTasks\":0,\"memMax\":128,\"memMin\":64,\"param\":{\"FILE_QUEUE\":[{\"change\":true,\"compressType\":\"0\",\"fileName\":\"/mnt/disk1/etl_data//SR_DHCP/download/2_dhcp_device_online_201610200940.txt\",\"fileSize\":31251,\"modifyDate\":1476927636000,\"serverName\":\"upload@132.228.241.94_17421\",\"sourceId\":\"2\",\"sourceType\":\"2\"}]},\"preCmdId\":\"16102009401569fH\",\"runPosition\":\"NODE_192.168.31.176\",\"sceneId\":\"3\",\"startTime\":1476927637744,\"status\":0,\"stepId\":\"12\",\"taskSeries\":\"161020094001T764\"}";
		CommandShow cmd=JSONUtil.parse(cmdStr, CommandShow.class);
		System.out.println(cmd);
	}
	
	public void stopStepTask(String path){
		ZooKeeperUtil.createNode(path+"/stop", null, zk);
	}
	@Override
	public void deleteNode(String path) {
		ZooKeeperUtil.deleteNodeTenTimes(path, zk);
	}
	
	@Override
	public void restartErrorCommand(String command) throws Exception{
		logger.info("重运行异常指令："+command);
		String errorCmdStr=ZooKeeperUtil.getData("/schedule/hiserror/"+command, zk);
		if(errorCmdStr==null||"".equals(errorCmdStr))
			throw new Exception("获取到的指令内容为空");
		Command errorCmd=JSONUtil.parse(errorCmdStr, CommandShow.class);
		Step step=stepService.getStep(errorCmd.getStepId());
		if(step==null)
			throw new Exception("获取到的步骤内容为空");
		errorCmd.setDispatchTime(System.currentTimeMillis());
		errorCmd.setStartTime(0);
		errorCmd.setEndTime(0);
		String runPosition=step.getRunPositionType().equals("1")?step.getServerCluster().getServerClusterId():step.getNode().getNodeCode();
		String path="/schedule/wait/"+runPosition;//+"/"+step.getStepId()+"/"+cmd.getCmdId();
		ZooKeeperUtil.createNode(path, null, zk);
		ZooKeeperUtil.createNode(path+"/"+errorCmd.getStepId(), null, zk);
		ZooKeeperUtil.createNodeE(path+"/"+errorCmd.getStepId()+"/"+command,JSONUtil.toJsonString(errorCmd), zk);
		ZooKeeperUtil.deleteNodeTenTimes("/schedule/hiserror/"+command, zk);
	}
}
