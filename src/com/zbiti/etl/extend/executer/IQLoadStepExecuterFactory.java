
package com.zbiti.etl.extend.executer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.executer.ICommandExecuterFactory;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;

@Service("IQLoadStepExecuterFactory")
public class IQLoadStepExecuterFactory implements ICommandExecuterFactory<Boolean>{

	@Autowired
	IStepService stepService;
	@Autowired
	IResourceService resourceService;
	@Autowired
	IFileTransferService fileTransferService;
	@Override
	public ICommandExecuter<Boolean> createExecuter() {
		IQLoadStepExecuter iqLoadStepExecuter=new IQLoadStepExecuter();
		iqLoadStepExecuter.setFileTransferService(fileTransferService);
		iqLoadStepExecuter.setResourceService(resourceService);
		iqLoadStepExecuter.setStepService(stepService);
		return iqLoadStepExecuter;
	}

}
