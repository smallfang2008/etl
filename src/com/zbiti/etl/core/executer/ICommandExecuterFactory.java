package com.zbiti.etl.core.executer;


public interface ICommandExecuterFactory<T> {
	
	public ICommandExecuter<T> createExecuter();
	
}
