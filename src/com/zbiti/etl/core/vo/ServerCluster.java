package com.zbiti.etl.core.vo;

import java.io.Serializable;

/**
 * 服务器集群
 * @author yhp
 *
 */
public class ServerCluster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268552154857136400L;
	String serverClusterId;
	String serverClusterName;
//	String zookeeperCode;
	ZookeeperCluster zookeeperCluster;
	String rootPath;//根目录，存储数据文件等。
	String nodeNumber;//节点数量
	
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getServerClusterId() {
		return serverClusterId;
	}
	public void setServerClusterId(String serverClusterId) {
		this.serverClusterId = serverClusterId;
	}
	public String getServerClusterName() {
		return serverClusterName;
	}
	public void setServerClusterName(String serverClusterName) {
		this.serverClusterName = serverClusterName;
	}
	public ZookeeperCluster getZookeeperCluster() {
		return zookeeperCluster;
	}
	public void setZookeeperCluster(ZookeeperCluster zookeeperCluster) {
		this.zookeeperCluster = zookeeperCluster;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}
	
}
