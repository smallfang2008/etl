package com.zbiti.etl.runtime;

import org.springframework.context.ApplicationContext;

import com.zbiti.etl.core.vo.Command;

public interface IProcess {
	void start(Command cmd);
	void cancel();
	
	Object getResult() throws Exception;
	void setApplicationContext(ApplicationContext ctx);
}
