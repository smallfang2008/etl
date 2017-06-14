package com.zbiti.etl.core.smo.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbiti.common.StringUtil;
import com.zbiti.etl.core.smo.IFileTransferClient;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.vo.Resource;

@Service
public class FileTransferServiceImpl implements IFileTransferService{
	
	private Log logger=LogFactory.getLog(FileTransferServiceImpl.class);
	
	@Autowired(required=true)
	IResourceService resourceService;
	
	@Override
	public IFileTransferClient getClient(String serverName) {
		Resource resource=resourceService.getByNameCache(serverName);
		AFileTransferClient fileTransferClient=null;
		if(resource==null){
			return null;
		}
		String resTypeName=StringUtil.objectToStr(resource.getResourceType().getResourceTypeName());
		logger.info("resTypeName:"+resTypeName);
		if("FTP".equals(resTypeName)){
			fileTransferClient=new FtpTransferClient();
		}else if("SFTP".equals(resTypeName)){
			fileTransferClient=new SftpTranferClient();
		}else if("SCP".equals(resTypeName)){
//			fileTransferClient=new SCPTranferClient();
		}
		if(fileTransferClient!=null){
			fileTransferClient.setServer(resource.getHostName());
			fileTransferClient.setUser(resource.getUserName());
			fileTransferClient.setPassword(resource.getPassword());
			fileTransferClient.setPort(Integer.parseInt(resource.getPort()));
			fileTransferClient.setFtpModel(resource.getResourceMode());
			fileTransferClient.setResourceEncoding(resource.getResourceEncoding());
		}
		return fileTransferClient;
	}

	@Override
	public IFileTransferClient getClient(Resource resource) {
		AFileTransferClient fileTransferClient=null;
		if(resource==null){
			return null;
		}
		String resTypeName=StringUtil.objectToStr(resource.getResourceType().getResourceTypeName());
		logger.info("resTypeName:"+resTypeName);
		if("FTP".equals(resTypeName)){
			fileTransferClient=new FtpTransferClient();
		}else if("SFTP".equals(resTypeName)){
			fileTransferClient=new SftpTranferClient();
		}else if("SCP".equals(resTypeName)){
//			fileTransferClient=new SCPTranferClient();
		}
		if(fileTransferClient!=null){
			fileTransferClient.setServer(resource.getHostName());
			fileTransferClient.setUser(resource.getUserName());
			fileTransferClient.setPassword(resource.getPassword());
			fileTransferClient.setPort(Integer.parseInt(resource.getPort()));
			fileTransferClient.setFtpModel(resource.getResourceMode());
			fileTransferClient.setResourceEncoding(resource.getResourceEncoding());
		}
		return fileTransferClient;
	}

}
