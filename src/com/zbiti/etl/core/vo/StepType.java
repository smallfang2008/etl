package com.zbiti.etl.core.vo;

import java.io.Serializable;
/**
 * 步骤类型
 * @author yhp
 *
 */
public class StepType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2209730554499650888L;

	String stepTypeId;
	String stepTypeName;
	String factoryClassName;
	String controllerMapping;
	String pageConfigTag;
	public String getStepTypeId() {
		return stepTypeId;
	}
	public void setStepTypeId(String stepTypeId) {
		this.stepTypeId = stepTypeId;
	}
	public String getStepTypeName() {
		return stepTypeName;
	}
	public void setStepTypeName(String stepTypeName) {
		this.stepTypeName = stepTypeName;
	}
	
	public String getFactoryClassName() {
		return factoryClassName;
	}
	public void setFactoryClassName(String factoryClassName) {
		this.factoryClassName = factoryClassName;
	}
	public String getControllerMapping() {
		return controllerMapping;
	}
	public void setControllerMapping(String controllerMapping) {
		this.controllerMapping = controllerMapping;
	}
	public String getPageConfigTag() {
		return pageConfigTag;
	}
	public void setPageConfigTag(String pageConfigTag) {
		this.pageConfigTag = pageConfigTag;
	}
	
	
}
