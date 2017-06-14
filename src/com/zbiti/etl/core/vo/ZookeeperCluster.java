package com.zbiti.etl.core.vo;

import java.io.Serializable;

/**
 * zookeeper集群
 * @author yhp
 *
 */
public class ZookeeperCluster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268552154857136400L;

	String zookeeperCode;
	String zookeeperName;
	String zookeeperString;
	public String getZookeeperCode() {
		return zookeeperCode;
	}
	public void setZookeeperCode(String zookeeperCode) {
		this.zookeeperCode = zookeeperCode;
	}
	public String getZookeeperName() {
		return zookeeperName;
	}
	public void setZookeeperName(String zookeeperName) {
		this.zookeeperName = zookeeperName;
	}
	public String getZookeeperString() {
		return zookeeperString;
	}
	public void setZookeeperString(String zookeeperString) {
		this.zookeeperString = zookeeperString;
	}
	
}
