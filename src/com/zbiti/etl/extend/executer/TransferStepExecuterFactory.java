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
@Service("transferStepExecuterFactory")
public class TransferStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	@Autowired
	IFileTransferService fileTransferService;
	
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		TransferStepExecuter transferStepExecuter=new TransferStepExecuter();
		transferStepExecuter.setFileTransferService(fileTransferService);
		transferStepExecuter.setStepService(stepService);
		return transferStepExecuter;
	}

}
