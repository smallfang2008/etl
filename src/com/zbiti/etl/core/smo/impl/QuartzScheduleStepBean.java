package com.zbiti.etl.core.smo.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zbiti.etl.core.smo.IJobScheduleService;
import com.zbiti.etl.core.vo.Scene;

public class QuartzScheduleStepBean extends QuartzJobBean implements StatefulJob{
	
	Log logger= LogFactory.getLog(QuartzScheduleStepBean.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		try{
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			IJobScheduleService jobScheduleService = (IJobScheduleService)dataMap.get("jobScheduleService");
			Scene scene=(Scene)dataMap.get("scene");
			jobScheduleService.scheduleTask(scene);
		}catch (Exception e) {
			System.out.println("定时调用时有异常抛出");
			e.printStackTrace();
		}
	}
}
