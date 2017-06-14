package com.zbiti.etl.core.vo;

import java.io.Serializable;

public class Resource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 493636179299882047L;

	String resourceId;
	
	ResourceType resourceType;
	
	String resourceName;
	String uri;
	String port;
	String serviceName;
	String password;
	String driver;
	String userName;
	String hostName;
	String maxActive;
	String maxIdle;
	String maxWait;
	String createOp;
	String modifyOp;
	String createDate;
	String modifyDate;
	String version;
	String dbSid;
	String resourceMode;
	String resourceEncoding;
	public String getResourceMode() {
		return resourceMode;
	}
	public void setResourceMode(String resourceMode) {
		this.resourceMode = resourceMode;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public ResourceType getResourceType() {
		return resourceType;
	}
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}
	public String getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}
	public String getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}
	public String getCreateOp() {
		return createOp;
	}
	public void setCreateOp(String createOp) {
		this.createOp = createOp;
	}
	public String getModifyOp() {
		return modifyOp;
	}
	public void setModifyOp(String modifyOp) {
		this.modifyOp = modifyOp;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDbSid() {
		return dbSid;
	}
	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}

	public String getResourceEncoding() {
		return resourceEncoding;
	}
	public void setResourceEncoding(String resourceEncoding) {
		this.resourceEncoding = resourceEncoding;
	}
	
	
}
