package com.zbiti.etl.extend.vo;

import java.io.Serializable;
import java.util.Date;

public class DownloadLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7499447525447872575L;
	private String stepSeries;
	private String serverName;
	private String filePath;
	private long fileSize;
	private Date insertTime;
	public String getStepSeries() {
		return stepSeries;
	}
	public void setStepSeries(String stepSeries) {
		this.stepSeries = stepSeries;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
}
