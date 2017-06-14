package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IStepService;

/**
 * 
 * @author 严海平
 *
 */
@Service("fileUpStepExecuterFactory")
public class FileUpStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired(required=true)
	IStepService stepService;
	@Autowired(required=true)
	IFileTransferService fileTransferService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		FileUpStepExecuter fileUpStepExecuter=new FileUpStepExecuter();
		fileUpStepExecuter.setStepService(stepService);
		fileUpStepExecuter.setFileTransferService(fileTransferService);
		return fileUpStepExecuter;
	}

}
