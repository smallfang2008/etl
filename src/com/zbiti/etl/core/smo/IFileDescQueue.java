package com.zbiti.etl.core.smo;

import org.apache.zookeeper.KeeperException;

import com.zbiti.etl.core.vo.FileDesc;

/**
 * 文件描述输出队列
 * @author 吴昌政
 *
 */
public interface IFileDescQueue {
	/**
	 * 向队列中推送文件描述
	 * @param fileDesc
	 */
	public void push(FileDesc fileDesc);
	
	/**
	 * 将队列中剩余的文件清空,推送到下一节点中
	 */
	public boolean clear()  throws KeeperException, InterruptedException;
}
