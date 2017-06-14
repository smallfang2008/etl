package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.util.Map;

public class Command implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 231196317377773906L;

	String cmdId;//指令id
	String preCmdId;//前置指令id
	String taskSeries;//任务序列
	String stepId;
	String sceneId;
	int memMax;
	int memMin;
	/*
	 * Date类型转成json之后会是如下格式，太过冗余
	 * "dispatchTime":{"date":24,"day":3,"hours":11,"minutes":38,"month":7,"seconds":48,"time":1472009928265,"timezoneOffset":-480,"year":116}
	 */
//	String dispatchTime;
//	String startTime;
//	String endTime;
	//先尝试用long表示吧
	long dispatchTime;
	long startTime;
	long endTime;
	String runPosition;
	int innerTasks;
	int status;
	String execInfo;
	Map<String, Object> param;
	public String getCmdId() {
		return cmdId;
	}
	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}
	public String getPreCmdId() {
		return preCmdId;
	}
	public void setPreCmdId(String preCmdId) {
		this.preCmdId = preCmdId;
	}
	public String getTaskSeries() {
		return taskSeries;
	}
	public void setTaskSeries(String taskSeries) {
		this.taskSeries = taskSeries;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public int getMemMax() {
		return memMax;
	}
	public void setMemMax(int memMax) {
		this.memMax = memMax;
	}
	public int getMemMin() {
		return memMin;
	}
	public void setMemMin(int memMin) {
		this.memMin = memMin;
	}
	
	public long getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(long dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getRunPosition() {
		return runPosition;
	}
	public void setRunPosition(String runPosition) {
		this.runPosition = runPosition;
	}
	public int getInnerTasks() {
		return innerTasks;
	}
	public void setInnerTasks(int innerTasks) {
		this.innerTasks = innerTasks;
	}
	public String getExecInfo() {
		return execInfo;
	}
	public void setExecInfo(String execInfo) {
		this.execInfo = execInfo;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	public Map<String, Object> getParam() {
		return param;
	}
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
