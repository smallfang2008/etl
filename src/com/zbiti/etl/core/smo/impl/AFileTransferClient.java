package com.zbiti.etl.core.smo.impl;

import org.apache.commons.net.PrintCommandListener;

import com.zbiti.etl.core.smo.IFileTransferClient;

public abstract class AFileTransferClient implements IFileTransferClient{

	private String server;
	private int port;
	private String user;
	private String password;
	private String resourceEncoding;
	private int maxConnTimes=2;
	private int maxDownloadTimes=2;
	private long waitTime=10*1000l;
	private PrintCommandListener listener;
	private String ftpModel;
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMaxConnTimes() {
		return maxConnTimes;
	}
	public void setMaxConnTimes(int maxConnTimes) {
		this.maxConnTimes = maxConnTimes;
	}
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	public PrintCommandListener getListener() {
		return listener;
	}
	public void setListener(PrintCommandListener listener) {
		this.listener = listener;
	}
	public String getFtpModel() {
		return ftpModel;
	}
	public void setFtpModel(String ftpModel) {
		this.ftpModel = ftpModel;
	}
	public int getMaxDownloadTimes() {
		return maxDownloadTimes;
	}
	public void setMaxDownloadTimes(int maxDownloadTimes) {
		this.maxDownloadTimes = maxDownloadTimes;
	}
	public String getResourceEncoding() {
		if(resourceEncoding==null||"".equals(resourceEncoding))
			resourceEncoding="gbk";
		return resourceEncoding;
	}
	public void setResourceEncoding(String resourceEncoding) {
		this.resourceEncoding = resourceEncoding;
	}
	 
}
