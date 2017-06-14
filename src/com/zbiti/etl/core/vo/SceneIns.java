package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 场景实例-由定时器定时产生
 * @author yhp
 *
 */
public class SceneIns implements Serializable{

	private static final long serialVersionUID = -5100957043227989890L;
	String taskSeries;
	Scene scene;
	Date dispatchTime;
	Date endTime;
	String taskStatus;//默认调度中，子任务都结束触发状态修改
	String taskResult;
	public String getTaskSeries() {
		return taskSeries;
	}
	public void setTaskSeries(String taskSeries) {
		this.taskSeries = taskSeries;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}
	
}
