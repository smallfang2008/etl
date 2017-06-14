package com.zbiti.etl.extend.vo;

import java.io.Serializable;

import com.zbiti.etl.core.vo.Step;

public class OracleExpStep extends Step implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5613166688467821799L;
	String resourceName;//对应资源名称
	String sqls;
	String filename;
	int fileTimeOffset;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getSqls() {
		return sqls;
	}
	public void setSqls(String sqls) {
		this.sqls = sqls;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getFileTimeOffset() {
		return fileTimeOffset;
	}
	public void setFileTimeOffset(int fileTimeOffset) {
		this.fileTimeOffset = fileTimeOffset;
	}
	
}
