package com.zbiti.etl.extend.vo;

import java.io.Serializable;
import java.util.Date;

import com.zbiti.etl.core.vo.Step;

/**
 * 源文件目录配置
 * @author yhp
 *
 */
public class SourceFileDir implements Serializable{

	private static final long serialVersionUID = -7402350868172947619L;
	String sourceFileDirId;
	Step step;
	String serverName;//对应资源名称
	String filePath;//根目录
	String filePathPattern;//子目录通配符
	Date startDate;//开始时间
	String filePattern;//文件通配符
	Date createDate;
	
	public String getSourceFileDirId() {
		return sourceFileDirId;
	}
	public void setSourceFileDirId(String sourceFileDirId) {
		this.sourceFileDirId = sourceFileDirId;
	}
	public Step getStep() {
		return step;
	}
	public void setStep(Step step) {
		this.step = step;
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
	public String getFilePathPattern() {
		return filePathPattern;
	}
	public void setFilePathPattern(String filePathPattern) {
		this.filePathPattern = filePathPattern;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getFilePattern() {
		return filePattern;
	}
	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
