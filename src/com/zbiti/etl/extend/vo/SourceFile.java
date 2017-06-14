package com.zbiti.etl.extend.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 源文件记录
 * @author yhp
 *
 */
public class SourceFile implements Serializable{

	private static final long serialVersionUID = -4918819305645091736L;
	String sourceFileId;
//	String sourceFileDirId;
	SourceFileDir sourceFileDir;
	String directory;
	String maxFile;
	long maxFileSize;
	Date modifyDate;
	public String getSourceFileId() {
		return sourceFileId;
	}
	public void setSourceFileId(String sourceFileId) {
		this.sourceFileId = sourceFileId;
	}
	public SourceFileDir getSourceFileDir() {
		return sourceFileDir;
	}
	public void setSourceFileDir(SourceFileDir sourceFileDir) {
		this.sourceFileDir = sourceFileDir;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getMaxFile() {
		return maxFile;
	}
	public void setMaxFile(String maxFile) {
		this.maxFile = maxFile;
	}
	
	public long getMaxFileSize() {
		return maxFileSize;
	}
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
