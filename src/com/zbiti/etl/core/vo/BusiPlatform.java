package com.zbiti.etl.core.vo;

import java.io.Serializable;

/**
 * 业务平台
 * @author yhp
 *
 */
public class BusiPlatform implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1816243346524574444L;

	String busiPlatformId;
	String busiPlatformName;
	String busiPlatformNotes;
	public String getBusiPlatformId() {
		return busiPlatformId;
	}
	public void setBusiPlatformId(String busiPlatformId) {
		this.busiPlatformId = busiPlatformId;
	}
	public String getBusiPlatformName() {
		return busiPlatformName;
	}
	public void setBusiPlatformName(String busiPlatformName) {
		this.busiPlatformName = busiPlatformName;
	}
	public String getBusiPlatformNotes() {
		return busiPlatformNotes;
	}
	public void setBusiPlatformNotes(String busiPlatformNotes) {
		this.busiPlatformNotes = busiPlatformNotes;
	}
}
