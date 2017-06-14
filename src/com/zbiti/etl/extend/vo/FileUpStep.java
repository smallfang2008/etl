package com.zbiti.etl.extend.vo;

import com.zbiti.etl.core.vo.Step;

public class FileUpStep extends Step{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2761220694378832238L;
	private String resourceName;
	private String upPath;
	private int upPathOffset;
	
	private int isGz;//1是0否
	private int isAppendTime;//文件名是否追加日期1、不追加 2、文件名前 3、后缀前4、文件名后
	private String appendTimePattern;//文件名追加日期格式
	private int appendTimeOffset;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getUpPath() {
		return upPath;
	}
	public void setUpPath(String upPath) {
		this.upPath = upPath;
	}
	public int getUpPathOffset() {
		return upPathOffset;
	}
	public void setUpPathOffset(int upPathOffset) {
		this.upPathOffset = upPathOffset;
	}
	public int getIsGz() {
		return isGz;
	}
	public void setIsGz(int isGz) {
		this.isGz = isGz;
	}
	public int getIsAppendTime() {
		return isAppendTime;
	}
	public void setIsAppendTime(int isAppendTime) {
		this.isAppendTime = isAppendTime;
	}
	public String getAppendTimePattern() {
		return appendTimePattern;
	}
	public void setAppendTimePattern(String appendTimePattern) {
		this.appendTimePattern = appendTimePattern;
	}
	public int getAppendTimeOffset() {
		return appendTimeOffset;
	}
	public void setAppendTimeOffset(int appendTimeOffset) {
		this.appendTimeOffset = appendTimeOffset;
	}
	
}
