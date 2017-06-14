package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommandShow extends Command implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7511897858207993007L;
	
	private String sceneName;
	private String stepName;
	private String stepTypeName;
	private String runPositionId;
	private boolean disabled ;
//	private String statusName;
//	private String dispatchDate;
//	private String startDate;
//	private String endDate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getDispatchDate() {
		if(dispatchTime!=0)
			return sdf.format(new Date(dispatchTime));
		return "";
	}
	public String getStartDate() {
		if(startTime!=0)
			return sdf.format(new Date(startTime));
		return "";
	}
	public String getEndDate() {
		if(endTime!=0)
			return sdf.format(new Date(endTime));
		return "";
	}
	public String getStatusName(){
		if(this.status==CommandConstants.STATE_EXCEPTION)
			return "异常停止";
		if(this.status==CommandConstants.STATE_USER_CANCEL)
			return "用户请求停止";
		if(this.status==CommandConstants.STATE_PROCESS_DEAD)
			return "进程僵死";
		if(this.status==CommandConstants.STATE_TIMEOUT)
			return "执行超时";
		if(this.status==CommandConstants.STATE_IGNORE)
			return "已忽略";
		if(this.status==CommandConstants.STATE_SUCCESS)
			return "运行成功";
		if(this.status==CommandConstants.STATE_RUN)
			return "正在运行";
		return "";
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getStepTypeName() {
		return stepTypeName;
	}
	public void setStepTypeName(String stepTypeName) {
		this.stepTypeName = stepTypeName;
	}
	public String getRunPositionId() {
		return runPositionId;
	}
	public void setRunPositionId(String runPositionId) {
		this.runPositionId = runPositionId;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
