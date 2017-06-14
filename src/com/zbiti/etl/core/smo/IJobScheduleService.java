package com.zbiti.etl.core.smo;

import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;

public interface IJobScheduleService {

	public static final String JOB_GROUP_NAME="ETL_JOB";
	public static final String TRIGGER_GROUP_NAME = "ETL_JOB"; 
	/**
	 * 装载的定时指令
	 * @param runningCmd
	 */
	void loadingTimer(Command cmd) throws Exception;
	/**
	 * 添加定时
	 * @param scene
	 * @throws Exception 
	 */
	void addJob(Scene scene) throws Exception;
	/**
	 * 调度任务
	 * @param scene
	 */
	void scheduleTask(Scene scene);
	/**
	 * 调度步骤
	 * @param step
	 */
	void scheduleStep(String taskSeries,Step step);
	
	boolean checkJob(Command command);
	void deleteJob(Command cmd) throws Exception;
}
