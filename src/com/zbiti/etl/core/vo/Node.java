package com.zbiti.etl.core.vo;

import java.io.Serializable;

public class Node implements Serializable{

	private static final long serialVersionUID = -2061005541625689899L;
	String nodeCode;
	String ipAddress;
	String serverName;//该服务器对应的资源名称
	String logPath;//日志目录
	int maxTasks=60;//该节点最大任务数
//	String serverClusterId;//服务器集群
	ServerCluster serverCluster;
	String isGetClusterTask;	//是否获取集群任务
	String oldNodeCode;//更改前的节点编码
	public String getNodeCode() {
		return nodeCode;
	}
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public int getMaxTasks() {
		return maxTasks;
	}
	public void setMaxTasks(int maxTasks) {
		this.maxTasks = maxTasks;
	}
	public ServerCluster getServerCluster() {
		return serverCluster;
	}
	public void setServerCluster(ServerCluster serverCluster) {
		this.serverCluster = serverCluster;
	}
	public String getIsGetClusterTask() {
		return isGetClusterTask;
	}
	public void setIsGetClusterTask(String isGetClusterTask) {
		this.isGetClusterTask = isGetClusterTask;
	}
	public String getOldNodeCode() {
		return oldNodeCode;
	}
	public void setOldNodeCode(String oldNodeCode) {
		this.oldNodeCode = oldNodeCode;
	}
	
}
