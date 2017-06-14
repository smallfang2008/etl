package com.zbiti.etl.runtime;


/**
 * 从节点职责：
	1.刷新配置
	2.获取本机上异常终止的指令，移除并清理进程
	当线程池空闲：
	3.优先获取本机上的等待任务调度
	4.获取本机所在集群上的等待任务调度
 * @author yhp
 *
 */
public interface INodeRuntimeService {

	void start();
	void join();
}
