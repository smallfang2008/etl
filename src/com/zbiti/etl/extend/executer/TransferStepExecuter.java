package com.zbiti.etl.extend.executer;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.FileUtil;
import com.zbiti.common.GZip;
import com.zbiti.common.StringUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IFileTransferClient;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.DownloadLog;

/**
 * 
 * @author 严海平
 * 
 */
public class TransferStepExecuter implements ICommandExecuter<Boolean> {
	private static final Log logger = LogFactory.getLog(TransferStepExecuter.class);
	
	
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step, Command command,
			IFileDescQueue fileDescQueue) throws Exception {
		logger.info("传输步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		logger.info("获取队列");
		@SuppressWarnings("unchecked")
		List<MorphDynaBean> fileQueue=(List<MorphDynaBean>) command.getParam().get("FILE_QUEUE");
		if(fileQueue==null||fileQueue.isEmpty()){
			logger.info("队列为空，退出");
			return false;
		}
		for(MorphDynaBean bean:fileQueue){
			FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
			String toPathFileName=getToPathFileName(node,step, fileDesc);
			if(toPathFileName==null)
				continue;
			IFileTransferClient fileTransferClient=null;
			try{
				fileTransferClient=fileTransferService.getClient(fileDesc.getServerName());
				fileTransferClient.login();
				fileTransferClient.download(fileDesc, toPathFileName);
			}catch (Exception e) {
				logger.error("下载异常！",e);
				throw e;
			}finally{
				if(fileTransferClient!=null)
					fileTransferClient.disconnectFtpClient();
			}
			if(!fileDesc.isChange()){
				logger.info("文件["+fileDesc.getFileName()+"]无变化，继续下一个！");
				continue;
			}
			try{
				DownloadLog downloadLog=new DownloadLog();
				downloadLog.setStepSeries(command.getCmdId());
				downloadLog.setFilePath(fileDesc.getFileName());
				downloadLog.setServerName(fileDesc.getServerName());
				downloadLog.setFileSize(fileDesc.getFileSize());
				stepService.saveDownloadLog(downloadLog);
			}catch (Exception e) {
				logger.error("保存下载日志出错",e);
			}
			if(fileDesc.getCompressType()==null||fileDesc.getCompressType().equals(FileUtil.COMPRESS_NO)){
				logger.info("文件["+fileDesc.getFileName()+"]无压缩直接推送");
				fileDesc.setFileName(toPathFileName);
				fileDesc.setServerName(node.getServerName());
				fileDescQueue.push(fileDesc);
				continue;
			}
			if(fileDesc.getCompressType().equals(FileUtil.COMPRESS_TAR_GZ)){
				logger.info("文件["+fileDesc.getFileName()+"]按照tar.gz解压");
				GZip.unTargzFile(fileDesc.getFileName(), fileDesc.getFileName()+".datDir");//解压
				List<String> filePaths=new ArrayList<String>();
				FileUtil.getSonFilePathByDir(fileDesc.getFileName()+".datDir", filePaths);
				for(String filePath:filePaths){
					File localFile =new File(filePath);
					if(localFile.exists()){
						logger.info("推送解压后的文件["+filePath+"]");
						FileDesc fd=new FileDesc();
						fd.setCompressType(FileUtil.COMPRESS_NO);
						fd.setChange(true);
						fd.setFileName(filePath);
						fd.setFileSize(localFile.length());
						fd.setModifyDate(localFile.lastModified());
						fd.setServerName(node.getServerName());
						fd.setSourceId(fileDesc.getSourceId());
						fd.setSourceType(fileDesc.getSourceType());
												
						fileDescQueue.push(fd);
					}
				}
				continue;
			}
			if(fileDesc.getCompressType().equals(FileUtil.COMPRESS_GZ)){
				logger.info("文件["+fileDesc.getFileName()+"]按照gz解压");
				GZip.gzip(toPathFileName, toPathFileName+".txt");
				File localFile =new File(toPathFileName+".txt");
				if(localFile.exists()){
					logger.info("推送解压后的文件["+toPathFileName+".txt]");
					fileDesc.setCompressType(FileUtil.COMPRESS_NO);
					fileDesc.setFileName(toPathFileName+".txt");
					fileDesc.setFileSize(localFile.length());
					fileDesc.setModifyDate(localFile.lastModified());
					fileDescQueue.push(fileDesc);
				}
			}
			
		}
		return true;
	}
	
	/**
	 * 下载后文件目录规范
	 * 根目录/场景编码/download/文件名 
	 * sourceId  数据源id
	 * @param sceneCode
	 * @param fileName
	 * @return
	 */
	private String getToPathFileName(Node node,Step step,FileDesc fileDesc) {
		if(fileDesc.getFileName()==null || "".equals(fileDesc.getFileName())){
			return null;
		}
		String rootPath=node.getServerCluster().getRootPath();
		String toPath=rootPath+"/"+step.getScene().getName()+"/download";
		File tpath=new File(toPath);
		if(!tpath.exists()){
			tpath.mkdirs();
		}
		String toPathFileName=toPath+"/"+fileDesc.getSourceId()+"_"+StringUtil.getFileNameByDirectory(fileDesc.getFileName());
		return toPathFileName;		
	}

	

	IStepService stepService;
	IFileTransferService fileTransferService;
	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}

	public IFileTransferService getFileTransferService() {
		return fileTransferService;
	}

	public void setFileTransferService(IFileTransferService fileTransferService) {
		this.fileTransferService = fileTransferService;
	}

}
