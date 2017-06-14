package com.zbiti.etl.core.vo;

import java.io.Serializable;

/**
 * 业务类别
 * @author yhp
 *
 */
public class BusiType implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7692229905692327598L;
	String busiTypeId;
	String busiTypeName;
	String busiTypeNotes;
	public String getBusiTypeId() {
		return busiTypeId;
	}
	public void setBusiTypeId(String busiTypeId) {
		this.busiTypeId = busiTypeId;
	}
	public String getBusiTypeName() {
		return busiTypeName;
	}
	public void setBusiTypeName(String busiTypeName) {
		this.busiTypeName = busiTypeName;
	}
	public String getBusiTypeNotes() {
		return busiTypeNotes;
	}
	public void setBusiTypeNotes(String busiTypeNotes) {
		this.busiTypeNotes = busiTypeNotes;
	}
}
