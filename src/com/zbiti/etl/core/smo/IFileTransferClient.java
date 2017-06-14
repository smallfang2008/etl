package com.zbiti.etl.core.smo;

import java.util.Calendar;
import java.util.List;

import com.zbiti.etl.core.vo.FileDesc;

/**
 * 
 * @author 严海平
 *
 */
public interface IFileTransferClient {
	
	public void login() throws Exception;
	
	public List<FileDesc> listPath(String path) throws Exception;
	public List<FileDesc> listPath(String path,String pathPattern) throws Exception;
	
	public List<FileDesc> listFile(String path,String pattern) throws Exception;
	public List<FileDesc> listFile(String path,String pattern,Calendar lastMaxModifyDate) throws Exception;
	
	public void download(FileDesc desc,String localPath) throws Exception;
	public void download(FileDesc desc,String localPath,long localFileSize) throws Exception;
	public void disconnectFtpClient() throws Exception;
	public long upload(String fromPath,String toPath) throws Exception;
	public String rename(String fromName,String toName)throws Exception;

}
