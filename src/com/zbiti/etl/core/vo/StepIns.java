package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.util.Date;

public class StepIns implements Serializable{

	private static final long serialVersionUID = 7956076819657686097L;

	String stepSeries;
	String prevStepSeries;
	SceneIns sceneIns;
	Step step;
	Date dispatchTime;
	Date startTime;
	Date endTime;
	String runPosition;
	int innerTasks;
	String status;
	String execInfo;
	public String getStepSeries() {
		return stepSeries;
	}
	public void setStepSeries(String stepSeries) {
		this.stepSeries = stepSeries;
	}
	public String getPrevStepSeries() {
		return prevStepSeries;
	}
	public void setPrevStepSeries(String prevStepSeries) {
		this.prevStepSeries = prevStepSeries;
	}
	public SceneIns getSceneIns() {
		return sceneIns;
	}
	public void setSceneIns(SceneIns sceneIns) {
		this.sceneIns = sceneIns;
	}
	public Step getStep() {
		return step;
	}
	public void setStep(Step step) {
		this.step = step;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExecInfo() {
		return execInfo;
	}
	public void setExecInfo(String execInfo) {
		this.execInfo = execInfo;
	}
}
