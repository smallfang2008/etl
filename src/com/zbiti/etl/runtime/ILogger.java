package com.zbiti.etl.runtime;

public interface ILogger {
	
	
	public void info(String info);
	
	public void info(String info,Exception exception);
	
}
