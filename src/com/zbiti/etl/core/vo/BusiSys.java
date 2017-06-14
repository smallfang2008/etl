package com.zbiti.etl.core.vo;

import java.io.Serializable;

/**
 * 业务类别
 * @author yhp
 *
 */
public class BusiSys implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7692229905692327598L;
	String busiSysId;
	String busiSysName;
	String busiSysNotes;
	public String getBusiSysId() {
		return busiSysId;
	}
	public void setBusiSysId(String busiSysId) {
		this.busiSysId = busiSysId;
	}
	public String getBusiSysName() {
		return busiSysName;
	}
	public void setBusiSysName(String busiSysName) {
		this.busiSysName = busiSysName;
	}
	public String getBusiSysNotes() {
		return busiSysNotes;
	}
	public void setBusiSysNotes(String busiSysNotes) {
		this.busiSysNotes = busiSysNotes;
	}
}
