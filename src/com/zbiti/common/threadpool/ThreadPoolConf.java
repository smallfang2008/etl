package com.zbiti.common.threadpool;

import java.io.IOException;
import java.util.Properties;

import com.zbiti.common.jedis.JedisConf;

public class ThreadPoolConf {

	private int corePoolSize;
	private int maxPoolSize;
	private int blockQueSize;
	private int keepAliveTime;
	private int threadSleepWork;
	private int threadSleepGetNews;
	
	private int dbThreadStartCount;
	private int dbThreadCount;
	
	public int getDbThreadStartCount() {
		return dbThreadStartCount;
	}

	public void setDbThreadStartCount(int dbThreadStartCount) {
		this.dbThreadStartCount = dbThreadStartCount;
	}

	public int getDbThreadCount() {
		return dbThreadCount;
	}

	public void setDbThreadCount(int dbThreadCount) {
		this.dbThreadCount = dbThreadCount;
	}

	public ThreadPoolConf(){
		readConf();
	}
	
	void readConf(){
		Properties prop = new Properties();
		try {
			prop.load(JedisConf.class.getResourceAsStream("/threadpool.properties"));
			corePoolSize=Integer.parseInt(prop.get("core.pool.size").toString());
			maxPoolSize=Integer.parseInt(prop.get("max.pool.size").toString());
			blockQueSize=Integer.parseInt(prop.get("block.que.size").toString());
			keepAliveTime=Integer.parseInt(prop.get("keep.alive.time").toString());
			threadSleepWork=Integer.parseInt(prop.get("thread.sleep.work").toString());
			threadSleepGetNews=Integer.parseInt(prop.get("thread.sleep.get.news").toString());

			dbThreadStartCount=Integer.parseInt(prop.get("dbThreadStartCount").toString());
			dbThreadCount=Integer.parseInt(prop.get("dbThreadCount").toString());
		} catch (IOException e) {
			System.out.println("/threadpool.properties load failure!");
			e.printStackTrace();
		}
	}
	
	public int getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public int getBlockQueSize() {
		return blockQueSize;
	}
	public void setBlockQueSize(int blockQueSize) {
		this.blockQueSize = blockQueSize;
	}
	public int getKeepAliveTime() {
		return keepAliveTime;
	}
	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	public int getThreadSleepWork() {
		return threadSleepWork;
	}
	public void setThreadSleepWork(int threadSleepWork) {
		this.threadSleepWork = threadSleepWork;
	}
	public int getThreadSleepGetNews() {
		return threadSleepGetNews;
	}
	public void setThreadSleepGetNews(int threadSleepGetNews) {
		this.threadSleepGetNews = threadSleepGetNews;
	}
}
