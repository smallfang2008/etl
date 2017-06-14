package com.zbiti.etl.core.smo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.dao.SceneInsDao;
import com.zbiti.etl.core.smo.IJobScheduleService;
import com.zbiti.etl.core.smo.ISceneInsService;
import com.zbiti.etl.core.smo.ISceneService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;

@Service
public class JobScheduleServiceImpl implements IJobScheduleService{
	
	Log logger=LogFactory.getLog(JobScheduleServiceImpl.class);
	@Autowired
	ISceneService sceneService;
	@Autowired
	IStepService stepService;
	@Autowired
	ISceneInsService sceneInsService;

	@Autowired
	ICommandService commandService;
	@Autowired
	Scheduler jobSchedule;
	
	@Override
	public void loadingTimer(Command cmd) throws Exception{
		Scene scene=sceneService.getSceneById(cmd.getSceneId());
		addJob(scene);
	}
	@Override
	public void addJob(Scene scene) throws Exception{
		JobDetail jobDetail = new JobDetail(scene.getSceneId(), JOB_GROUP_NAME,QuartzScheduleStepBean.class);
		jobDetail.getJobDataMap().put("jobScheduleService", this);
		jobDetail.getJobDataMap().put("scene", scene);
		CronTrigger trigger = new CronTrigger(scene.getSceneId(),TRIGGER_GROUP_NAME);
		try {
			trigger.setCronExpression(scene.getCroneExpression());
			jobSchedule.scheduleJob(jobDetail, trigger);
			if (!jobSchedule.isShutdown())
				jobSchedule.start();
		} catch (Exception e) {
			logger.error("scene "+scene.getName()+"表达式:" + scene.getCroneExpression() + "不正确");
			throw new Exception("表达式:" + scene.getCroneExpression() + "不正确");
		}
	}
	@Override
	public void scheduleTask(Scene scene) {
		logger.info("场景"+scene.getName()+"["+scene.getSceneId()+"]生成任务！");
		List<Step> steps=stepService.listFirstStepBySceneId(scene.getSceneId());
		if(steps==null||steps.size()==0){
			logger.info("场景"+scene.getName()+"["+scene.getSceneId()+"]无步骤....");
			return;
		}
		//先产生任务实例
		SceneIns sceneIns=new SceneIns();
		sceneIns.setDispatchTime(new Date());
		sceneIns.setScene(scene);
		sceneIns.setTaskSeries(commandService.getCommandId());
		sceneIns.setTaskStatus("0");//在运行
		sceneInsService.saveSceneIns(sceneIns);
		
		for(Step step:steps)
			scheduleStep(sceneIns.getTaskSeries(),step);
	}
	@Override
	public void scheduleStep(String taskSeries,Step step) {
		try {
			commandService.createNodeForWait(taskSeries,"-1", step, new HashMap<String, Object>());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public void deleteJob(Command cmd) throws Exception {
		jobSchedule.pauseTrigger(cmd.getSceneId(), JOB_GROUP_NAME);// 停止触发器
		jobSchedule.unscheduleJob(cmd.getSceneId(), TRIGGER_GROUP_NAME);// 移除触发器
		jobSchedule.deleteJob(cmd.getSceneId(), JOB_GROUP_NAME);// 删除任务
	}
	
	@Override
	public boolean checkJob(Command command) {
		try {
			JobDetail jobDetail = jobSchedule.getJobDetail(command.getSceneId(), JOB_GROUP_NAME);
			if(jobDetail!=null){
				return true;
			}
		} catch (SchedulerException e) {			 
			e.printStackTrace();
		}
		return false;
	}

}
