package com.zbiti.etl.runtime;


/**
 * 主节点服务职能简介：
	1、不停的尝试写/shedule/master临时节点，随时准备接管主节点
	2、获取运行历史的指令写入DB
	3、扫描异常终止的指令写入ERROR节点，并写入异常终止节点（由从节点清除进程）
	4、获取异常指令写入DB
	5、装载定时器，为防止源主节点掉线，先接管原主节点的定时任务，获取正在运行的定时器，刷新内存中的job，获取等待的定时器，刷新内存中的job
 * @author yhp
 *
 */
public interface IMasterRuntimeService {
	
	public void start();
	
	public void join();
	
}
