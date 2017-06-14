package com.zbiti.etl.core.vo;

public   class CommandConstants {
	public static final int STATE_RUN=0;//正在运行状态
	public static final int STATE_USER_CANCEL=1;//用户请求停止状态
	public static final int STATE_EXCEPTION=2;//异常停止状态
	public static final int STATE_SUCCESS=3;//运行成功状态
	public static final int STATE_PROCESS_DEAD=4;//进程僵死
	public static final int STATE_TIMEOUT=5;//执行超时
	public static final int STATE_IGNORE=6;//已忽略的异常
	public static final int STATE_STOP_TIMEOUT=7;//执行超时
}
