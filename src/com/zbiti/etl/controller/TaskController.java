package com.zbiti.etl.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zbiti.common.FileUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.common.vo.DataVO;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.smo.IFileTransferClient;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IJobScheduleService;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.ITaskService;
import com.zbiti.etl.core.vo.CommandConstants;
import com.zbiti.etl.core.vo.CommandShow;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepIns;
import com.zbiti.etl.runtime.ICommandService;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/etl/task")
public class TaskController {

	private Log log = LogFactory.getLog(TaskController.class);

	@Autowired(required = true)
	private ITaskService taskService;

	@Autowired(required = true)
	private ICommandService iCommandService;

	@Autowired(required = true)
	private IJobScheduleService iJobScheduleService;
	@Autowired
	private INodeService nodeService;
	@Autowired
	private IFileTransferService fileTransferService;

	/**
	 * 任务列表查询
	 */
	@RequestMapping(value="/queryTaskByPage")
	@ResponseBody
	public PageQueryResult<SceneIns> queryTaskByPage(
			DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "serverCluster", required = false) String serverCluster,
			@RequestParam(value = "sceneState", required = false) String sceneState,
			@RequestParam(value = "notes", required = false) String notes,
			@RequestParam(value = "taskStatus", required = false) String taskStatus,
			@RequestParam(value = "taskResult", required = false) String taskResult,
			@RequestParam(value = "beginTime", required = false) String beginTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "sceneId", required = false) String sceneId,
			@RequestParam(value = "startStatus", required = false) String startStatus)
			throws IOException {
		log.debug("查询任务");
		Page page = new Page();
		page.setCurrentPage(pageIndex + 1);
		page.setShowCount(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("serverCluster", serverCluster);
		map.put("sceneState", sceneState);
		map.put("sceneState", sceneState);
		map.put("notes", new String(notes.getBytes("iso8859-1"),"utf-8"));
		map.put("taskStatus", taskStatus);
		map.put("taskResult", taskResult);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("sceneId", sceneId);
		map.put("startStatus", startStatus);
		PageQueryResult<SceneIns> queryResult = taskService.selectTaksListPage(
				map, page);
		return queryResult;
	}

	@RequestMapping("/toEditTask")
	public ModelAndView toEditTask(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		Scene scene = taskService.getTaskInfo(sceneId);
		ModelAndView mav = new ModelAndView("/etl/task_manager/editTask");
		mav.addObject("editObj", scene);
		return mav;
	}

	/**
	 * 编辑任务
	 * 
	 * @param scene
	 * @return
	 */
	@RequestMapping("/editTask")
	@ResponseBody
	public String editTask(Scene scene) {
		try {
			taskService.update(scene);
			return "1";
		} catch (Exception e) {
			log.error(e);
		}
		return "0";
	}

	@RequestMapping("/addTask")
	@ResponseBody
	public String addTask(Scene scene) {
		try {
			String sceneId = taskService.getSceneSeq();
			scene.setSceneId(sceneId);
			scene.setSceneStatus("1");
			taskService.save(scene);
			return sceneId;
		} catch (Exception e) {
			log.error(e);
		}
		return "-1";
	}

	/**
	 * 跳转任务历史
	 */
	@RequestMapping("/toqueryTaskHistory")
	public ModelAndView toqueryTaskHistory(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		Scene scene = taskService.getTaskInfo(sceneId);
		ModelAndView mav = new ModelAndView("/etl/task_manager/taskHistory");
		mav.addObject("scene", scene);
		return mav;
	}

	/**
	 * 任务历史查询
	 */
	@RequestMapping("/queryTaskHistory")
	@ResponseBody
	public PageQueryResult<SceneIns> queryTaskHistory(
			DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "sceneId", required = false) String sceneId)
			throws IOException {
		log.debug("任务历史查询");
		Page page = new Page();
		page.setCurrentPage(pageIndex + 1);
		page.setShowCount(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sceneId", sceneId);
		PageQueryResult<SceneIns> queryResult = taskService
				.selectTaskHistoryListPage(map, page);
		return queryResult;
	}
	
	/**
	 * 跳转步骤历史
	 */
	@RequestMapping("/toqueryStepHistory")
	public ModelAndView toqueryStepHistory(
			@RequestParam(value = "taskSeries", required = false) String taskSeries) {
		SceneIns sceneIns = taskService.querySceneIns(taskSeries);
		ModelAndView mav = new ModelAndView("/etl/task_manager/stepHistory");
		mav.addObject("sceneIns", sceneIns);
		return mav;
	}

	/**
	 * 步骤历史查询
	 */
	@RequestMapping("/queryStepHistory")
	@ResponseBody
	public PageQueryResult<StepIns> queryStepHistory(
			DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "taskSeries", required = false) String taskSeries)
			throws IOException {
		log.debug("任务历史查询");
		Page page = new Page();
		page.setCurrentPage(pageIndex + 1);
		page.setShowCount(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskSeries", taskSeries);
		PageQueryResult<StepIns> queryResult = taskService
				.selectStepHistoryListPage(map, page);
		return queryResult;
	}

	/**
	 * 任务删除
	 */
	@RequestMapping("/deleteTask")
	@ResponseBody
	public String deleteTask(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		return taskService.deleteTask(sceneId);
	}

	/**
	 * 单次运行
	 */
	@RequestMapping("/singleRun")
	@ResponseBody
	public String singleRun(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		try {
			iJobScheduleService.scheduleTask(taskService.getTaskInfo(sceneId));
			return "1";
		} catch (Exception e) {
			return "-1";
		}
	}

	/**
	 * 定时运行
	 */
	@RequestMapping("/timedRun")
	@ResponseBody
	public String timedRun(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		try {
			iCommandService.createWaitTimer(taskService.getTaskInfo(sceneId));

			Scene scene = new Scene();
			scene.setSceneId(sceneId);
			// 更新启动状态为：启动中
			scene.setStartStatus("2");
			taskService.update(scene);
			return "1";
		} catch (Exception e) {
			return "-1";
		}
	}
	
	
	/**
	 * 正在运行任务查询
	 */
	@RequestMapping("/taskDoingPageQuery")
	@ResponseBody
	public PageQueryResult<CommandShow> taskDoingPageQuery(
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "taskName", required = false) String taskName,
			@RequestParam(value = "stepName", required = false) String stepName,
			@RequestParam(value = "stepType", required = false) String stepType,
			@RequestParam(value = "taskSeries", required = false) String taskSeries,
			@RequestParam(value = "stepSeries", required = false) String stepSeries,
			@RequestParam(value = "prevStepSeries", required = false) String prevStepSeries,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("rows", String.valueOf(rows));
		map.put("pageIndex", String.valueOf(pageIndex));
		map.put("taskName", new String(taskName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepName", new String(stepName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepType", stepType);
		map.put("taskSeries", taskSeries);
		map.put("stepSeries", stepSeries);
		map.put("prevStepSeries", prevStepSeries);
		map.put("serverClusterId", serverClusterId);
		map.put("nodeCode", nodeCode);
		log.info("先查询符合条件的步骤");
		List<Step> stepList=taskService.listStepByCondition(map);
		log.info("查询步骤下面的符合条件的正在运行的指令");
		PageQueryResult<CommandShow> queryResult = iCommandService.selectTaskDoingListPage(stepList,map);
		log.info("查询完毕");
		return queryResult;
	}
	
	/**
	 * 等待任务列表查询
	 */
	@RequestMapping("/taskWaitPageQuery")
	@ResponseBody
	public PageQueryResult<CommandShow> taskWaitPageQuery(
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "taskName", required = false) String taskName,
			@RequestParam(value = "stepName", required = false) String stepName,
			@RequestParam(value = "stepType", required = false) String stepType,
			@RequestParam(value = "taskSeries", required = false) String taskSeries,
			@RequestParam(value = "stepSeries", required = false) String stepSeries,
			@RequestParam(value = "prevStepSeries", required = false) String prevStepSeries,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode)
			throws IOException {
		if((serverClusterId==null||"".equals(serverClusterId))&&(nodeCode==null||"".equals(nodeCode)))
			return new PageQueryResult<CommandShow>(new ArrayList<CommandShow>(), 0);;
		Map<String, String> map = new HashMap<String, String>();
		map.put("rows", String.valueOf(rows));
		map.put("pageIndex", String.valueOf(pageIndex));
		map.put("taskName", new String(taskName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepName", new String(stepName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepType", stepType);
		map.put("taskSeries", taskSeries);
		map.put("stepSeries", stepSeries);
		map.put("prevStepSeries", prevStepSeries);
		if(nodeCode==null||"".equals(nodeCode.trim()))
			map.put("serverClusterId", serverClusterId);
		map.put("nodeCode", nodeCode);
		log.info("先查询符合条件的步骤");
		List<Step> stepList=taskService.listStepByCondition(map);
		log.info("查询步骤下面的符合条件的等待任务的指令");
		PageQueryResult<CommandShow> queryResult = iCommandService.selectTaskWaitListPage(stepList,map);
		return queryResult;
	}
	
	/**
	 * 异常任务列表查询
	 */
	@RequestMapping("/taskErrorPageQuery")
	@ResponseBody
	public PageQueryResult<CommandShow> taskErrorPageQuery(
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "taskName", required = false) String taskName,
			@RequestParam(value = "stepName", required = false) String stepName,
			@RequestParam(value = "stepType", required = false) String stepType,
			@RequestParam(value = "taskSeries", required = false) String taskSeries,
			@RequestParam(value = "stepSeries", required = false) String stepSeries,
			@RequestParam(value = "prevStepSeries", required = false) String prevStepSeries,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode,
			@RequestParam(value = "status", required = false) String status,
			HttpServletRequest request)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", new String(taskName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepName", new String(stepName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepType", stepType);
		map.put("taskSeries", taskSeries);
		map.put("stepSeries", stepSeries);
		map.put("prevStepSeries", prevStepSeries);
		map.put("serverClusterId", serverClusterId);
		map.put("nodeCode", nodeCode);
		if(status==null||"".equals(status))
			status=CommandConstants.STATE_EXCEPTION+","+CommandConstants.STATE_PROCESS_DEAD+","+CommandConstants.STATE_USER_CANCEL+","+CommandConstants.STATE_TIMEOUT+",";
		//+CommandConstants.STATE_IGNORE
		map.put("status", status);

		Page page = new Page();
		page.setCurrentPage(pageIndex + 1);
		page.setShowCount(rows);
		PageQueryResult<StepIns> stepInsResult = taskService
				.selectStepInsHisListPage(map, page);
		List<CommandShow> cmdList=new ArrayList<CommandShow>();
		for(StepIns stepIns:stepInsResult.getRows()){
			CommandShow cmd=new CommandShow();
			cmd.setCmdId(stepIns.getStepSeries());
			cmd.setTaskSeries(stepIns.getSceneIns().getTaskSeries());
			cmd.setDispatchTime(stepIns.getDispatchTime()!=null?stepIns.getDispatchTime().getTime():0);
			cmd.setStartTime(stepIns.getStartTime()!=null?stepIns.getStartTime().getTime():0);
			cmd.setEndTime(stepIns.getEndTime()!=null?stepIns.getEndTime().getTime():0);
			cmd.setPreCmdId(stepIns.getPrevStepSeries());
			cmd.setRunPosition(stepIns.getRunPosition());
			cmd.setExecInfo(stepIns.getExecInfo());
			cmd.setStepName(stepIns.getStep().getStepName());
			if(stepIns.getStep().getScene()!=null)
				cmd.setSceneName(stepIns.getStep().getScene().getName());
			if(stepIns.getStep().getStepType()!=null)
				cmd.setStepTypeName(stepIns.getStep().getStepType().getStepTypeName());
			cmd.setStatus(Integer.parseInt(stepIns.getStatus()));
//			log.info(stepIns.getStatus());
			if(stepIns.getStatus().equals(String.valueOf(CommandConstants.STATE_IGNORE))){
				cmd.setDisabled(true);
			}
			cmdList.add(cmd);
		}
		PageQueryResult<CommandShow> queryResult=new PageQueryResult<CommandShow>(cmdList, page.getTotalResult());
//		log.info(JSONUtil.toJsonString(queryResult));
		return queryResult;
	}
	
	/**
	 * 异常任务列表查询
	 */
	@RequestMapping("/taskHisPageQuery")
	@ResponseBody
	public PageQueryResult<CommandShow> taskHisPageQuery(
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "taskName", required = false) String taskName,
			@RequestParam(value = "stepName", required = false) String stepName,
			@RequestParam(value = "stepType", required = false) String stepType,
			@RequestParam(value = "taskSeries", required = false) String taskSeries,
			@RequestParam(value = "stepSeries", required = false) String stepSeries,
			@RequestParam(value = "prevStepSeries", required = false) String prevStepSeries,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId,
			@RequestParam(value = "nodeCode", required = false) String nodeCode,
			@RequestParam(value = "status", required = false) String status)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", new String(taskName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepName", new String(stepName.getBytes("iso8859-1"),"utf-8"));
		map.put("stepType", stepType);
		map.put("taskSeries", taskSeries);
		map.put("stepSeries", stepSeries);
		map.put("prevStepSeries", prevStepSeries);
		map.put("serverClusterId", serverClusterId);
		map.put("nodeCode", nodeCode);
		if(status==null||"".equals(status))
			status=String.valueOf(CommandConstants.STATE_SUCCESS);
		//+CommandConstants.STATE_IGNORE
		map.put("status", status);

		Page page = new Page();
		page.setCurrentPage(pageIndex + 1);
		page.setShowCount(rows);
		PageQueryResult<StepIns> stepInsResult = taskService
				.selectStepInsHisListPage(map, page);
		List<CommandShow> cmdList=new ArrayList<CommandShow>();
		for(StepIns stepIns:stepInsResult.getRows()){
			CommandShow cmd=new CommandShow();
			cmd.setCmdId(stepIns.getStepSeries());
			cmd.setTaskSeries(stepIns.getSceneIns().getTaskSeries());
			cmd.setDispatchTime(stepIns.getDispatchTime()!=null?stepIns.getDispatchTime().getTime():0);
			cmd.setStartTime(stepIns.getStartTime()!=null?stepIns.getStartTime().getTime():0);
			cmd.setEndTime(stepIns.getEndTime()!=null?stepIns.getEndTime().getTime():0);
			cmd.setPreCmdId(stepIns.getPrevStepSeries());
			cmd.setRunPosition(stepIns.getRunPosition());
			cmd.setExecInfo(stepIns.getExecInfo());
			cmd.setStepName(stepIns.getStep().getStepName());
			if(stepIns.getStep().getScene()!=null)
				cmd.setSceneName(stepIns.getStep().getScene().getName());
			if(stepIns.getStep().getStepType()!=null)
				cmd.setStepTypeName(stepIns.getStep().getStepType().getStepTypeName());
			cmd.setStatus(Integer.parseInt(stepIns.getStatus()));
//			log.info(stepIns.getStatus());
			if(stepIns.getStatus().equals(String.valueOf(CommandConstants.STATE_IGNORE))){
				cmd.setDisabled(true);
			}
			cmdList.add(cmd);
		}
		PageQueryResult<CommandShow> queryResult=new PageQueryResult<CommandShow>(cmdList, page.getTotalResult());
//		log.info(JSONUtil.toJsonString(queryResult));
		return queryResult;
	}
	
	/**
	 * 日志查询
	 */
	@RequestMapping(value="/loadLog",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String loadLog(
			@RequestParam(value = "stepSeries", required = false) String stepSeries,
			@RequestParam(value = "size", required = false) int size,
			@RequestParam(value = "encoding", required = false) String encoding,
			@RequestParam(value = "nodeCode", required = false) String nodeCode)
			throws IOException {
		if(nodeCode==null||nodeCode.equals("")||stepSeries==null||stepSeries.equals(""))
			return "";
		Node node=nodeService.getByCode(nodeCode);
		if(node==null)
			return "";
		IFileTransferClient fileTransferClient=fileTransferService.getClient(node.getServerName());
		try {
			fileTransferClient.login();
			String tomcatPath = System.getProperty("catalina.home");
			String localPath=tomcatPath+"/zklog/";
			File localPathFile = new File(localPath);
			if (!localPathFile.exists()){
				localPathFile.mkdir();
			}
			String filename=stepSeries+".log";
			FileDesc fileDesc=new FileDesc();
			fileDesc.setSourceType("1");//不追加
			fileDesc.setFileName(node.getLogPath()+"/"+filename);
			fileTransferClient.download(fileDesc, localPath+filename);
			String log=FileUtil.readTail(localPath+filename, size, encoding);
			return log;
		} catch (Exception e) {
			log.error(e);
		}
		return "";
	}
	
	/**
	 * 任务停止
	 */
	@RequestMapping("/stopDoingTask")
	@ResponseBody
	public String stopTask(
			@RequestParam(value = "taskPath", required = false) String taskPath) {
		if(taskPath==null||taskPath.equals(""))
			return "1";
		try{
			String[] taskPathArray=taskPath.split(",");
			for(String stepTaskPath:taskPathArray){
				iCommandService.stopStepTask("/schedule/doing/"+stepTaskPath);
			}
			return "1";
		}catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	/**
	 * 任务删除
	 */
	@RequestMapping("/deleteWaitTask")
	@ResponseBody
	public String deleteWaitTask(
			@RequestParam(value = "taskPath", required = false) String taskPath) {
		if(taskPath==null||taskPath.equals(""))
			return "1";
		try{
			String[] taskPathArray=taskPath.split(",");
			for(String stepTaskPath:taskPathArray){
				iCommandService.deleteNode("/schedule/wait/"+stepTaskPath);
			}
			return "1";
		}catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	/**
	 * 忽略异常任务
	 */
	@RequestMapping("/ignoreErrorTask")
	@ResponseBody
	public String ignoreErrorTask(
			@RequestParam(value = "taskPath", required = false) String taskPath) {
		if(taskPath==null||taskPath.equals(""))
			return "1";
		try{
			String[] taskPathArray=taskPath.split(",");
			StepIns stepIns=new StepIns();
			stepIns.setStatus("6");
			for(String stepTaskPath:taskPathArray){
				iCommandService.deleteNode("/schedule/hiserror/"+stepTaskPath);
				stepIns.setStepSeries(stepTaskPath.split("_")[1]);
				taskService.updateStepIns(stepIns);
			}
			return "1";
		}catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	
	/**
	 * 重新运行异常任务
	 */
	@RequestMapping("/reRunErrorTask")
	@ResponseBody
	public String reRunErrorTask(
			@RequestParam(value = "taskPath", required = false) String taskPath) {
		if(taskPath==null||taskPath.equals(""))
			return "1";
		try{
			String[] taskPathArray=taskPath.split(",");
			for(String stepTaskPath:taskPathArray){
				iCommandService.restartErrorCommand(stepTaskPath);
				taskService.deleteStepIns(stepTaskPath.split("_")[1]);
			}
			return "1";
		}catch (Exception e) {
			log.error(e);
		}
		return "0";
	}
	/**
	 * 终止
	 */
	@RequestMapping("/stop")
	@ResponseBody
	public String stop(
			@RequestParam(value = "sceneId", required = false) String sceneId) {
		try {
			iCommandService.stopTimer(sceneId);

			Scene scene = new Scene();
			scene.setSceneId(sceneId);
			// 更新启动状态为：未启动
			scene.setStartStatus("1");
			taskService.update(scene);
			return "1";
		} catch (Exception e) {
			return "-1";
		}
	}
}
