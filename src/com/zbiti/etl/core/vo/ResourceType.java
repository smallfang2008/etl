package com.zbiti.etl.core.vo;

import java.io.Serializable;

public class ResourceType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1902407922488373853L;
	String resourceTypeId;
	String resourceTypeName;
	public String getResourceTypeId() {
		return resourceTypeId;
	}
	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}
}
