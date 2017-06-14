package com.zbiti.etl.core.executer;

import org.springframework.context.ApplicationContext;

import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;

public interface ICommandExecuter<T> {
	
	public T execute(ApplicationContext ctx,Node node,Step step,Command command,IFileDescQueue fileDescQueue) throws Exception ;
	
}
