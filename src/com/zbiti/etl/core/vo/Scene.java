package com.zbiti.etl.core.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yhp
 * 
 */
public class Scene implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3816420747468747346L;

	String sceneId;
	// String busiTypeId;
	BusiType busiType;
	// String serverClusterId;
	ServerCluster serverCluster;
	String name;
	String notes;
	String sceneStatus;
	String croneExpression;
	// String busiPlatformId;
	BusiPlatform busiPlatform;
	// String busiSysId;
	BusiSys busiSys;
	String linkman;// 对接人
	String linkSource;//对接部门或系统
	String creater;
	Date createTime;
	
	String startStatus;
	String startLog;

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public BusiType getBusiType() {
		return busiType;
	}

	public void setBusiType(BusiType busiType) {
		this.busiType = busiType;
	}

	public ServerCluster getServerCluster() {
		return serverCluster;
	}

	public void setServerCluster(ServerCluster serverCluster) {
		this.serverCluster = serverCluster;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSceneStatus() {
		return sceneStatus;
	}

	public void setSceneStatus(String sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

	public String getCroneExpression() {
		return croneExpression;
	}

	public void setCroneExpression(String croneExpression) {
		this.croneExpression = croneExpression;
	}

	public BusiPlatform getBusiPlatform() {
		return busiPlatform;
	}

	public void setBusiPlatform(BusiPlatform busiPlatform) {
		this.busiPlatform = busiPlatform;
	}

	public BusiSys getBusiSys() {
		return busiSys;
	}

	public void setBusiSys(BusiSys busiSys) {
		this.busiSys = busiSys;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public String getStartLog() {
		return startLog;
	}

	public void setStartLog(String startLog) {
		this.startLog = startLog;
	}

	public String getLinkSource() {
		return linkSource;
	}

	public void setLinkSource(String linkSource) {
		this.linkSource = linkSource;
	}
	
}
