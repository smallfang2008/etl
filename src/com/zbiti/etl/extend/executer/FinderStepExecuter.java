package com.zbiti.etl.extend.executer;
 
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IFileTransferClient;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.FinderStep;
import com.zbiti.etl.extend.vo.SourceFile;
import com.zbiti.etl.extend.vo.SourceFileDir;

/**
 * 发现（扫描）步骤执行器
 * 根据配置的资源及文件目录、文件匹配表达式等信息遍历目录遍历文件
 * 获取需要处理的文件加入队列
 * @author 严海平
 * 
 */
public class FinderStepExecuter implements ICommandExecuter<Boolean> {
	private static final Log logger = LogFactory.getLog(FinderStepExecuter.class);
	
	IStepService stepService;
	IFileTransferService fileTransferService;
	
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step, Command command,
			IFileDescQueue fileDescQueue) throws Exception {
		logger.info("发现步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		FinderStep finderStep=stepService.getFinderStepByStepId(step.getStepId());
		List<SourceFileDir> sourceFileDirs=stepService.listSourceFileDirByStepId(step.getStepId());
		if(sourceFileDirs==null||sourceFileDirs.size()==0){
			logger.info("没有配置源文件目录，退出！");
			command.setExecInfo("没有配置源文件目录");
			return false;
		}
		for(SourceFileDir sourceFileDir:sourceFileDirs){
			IFileTransferClient fileTransferClient=null;
			try{
				logger.info("初始化资源："+sourceFileDir.getServerName());
				fileTransferClient=fileTransferService.getClient(sourceFileDir.getServerName());
				if(fileTransferClient==null){
//					logger.error("资源["+sourceFileDir.getServerName()+"]未找到");
					throw new Exception("资源["+sourceFileDir.getServerName()+"]未找到");
				}
				logger.info("资源["+sourceFileDir.getServerName()+"]登录");
				fileTransferClient.login();
				logger.info("根据源文件目录["+sourceFileDir.getFilePath()+"]和子目录通配符["+sourceFileDir.getFilePathPattern()+"]获取符合条件的目录");
				List<FileDesc> fileDescDirs = fileTransferClient.listPath(sourceFileDir.getFilePath(),sourceFileDir.getFilePathPattern());
				logger.info("匹配到的目录数量："+fileDescDirs.size());
				for(FileDesc fileDescDir:fileDescDirs){
					//此处fileDesc的fileName是目录
					logger.info("扫描"+fileDescDir.getFileName()+"下的文件");
					SourceFile sourceFile=stepService.getSourceFileByDirIdAndDir(sourceFileDir.getSourceFileDirId(), fileDescDir.getFileName());
					if(sourceFile==null||sourceFile.getModifyDate()==null){
						sourceFile=new SourceFile();
						sourceFile.setSourceFileDir(sourceFileDir);
						sourceFile.setDirectory(fileDescDir.getFileName());
						sourceFile.setModifyDate(sourceFileDir.getStartDate());
					}
					Calendar lastMaxModifyDate=Calendar.getInstance();
					lastMaxModifyDate.setTime(sourceFile.getModifyDate());
					List<FileDesc> fileDescFiles=fileTransferClient.listFile(fileDescDir.getFileName(), sourceFileDir.getFilePattern(), lastMaxModifyDate);
					
					if(fileDescFiles==null||fileDescFiles.isEmpty()){
						logger.info("没有符合条件的文件，next");
						continue;
					}
					long maxTime=sourceFile.getModifyDate().getTime();
					long maxSize=sourceFile.getMaxFileSize();
					logger.info("遍历符合条件的文件，取得最新文件记录入库，并推送队列");
					for(FileDesc fileDescFile:fileDescFiles){
						if(fileDescFile.getModifyDate()==maxTime&&(finderStep.getSourceType().equals("1")||maxSize==fileDescFile.getFileSize())){
							logger.info("文件["+fileDescFile.getFileName()+"]时间["+new Date(fileDescFile.getModifyDate())+"]和记录的最大文件修改时间（或者起始时间）相等，且文件不追加或者文件大小["+fileDescFile.getFileSize()+"]没变，pass");
							continue;
						}
						fileDescFile.setServerName(sourceFileDir.getServerName());
						fileDescFile.setCompressType(finderStep.getCompressPattern());
						fileDescFile.setSourceType(finderStep.getSourceType());
						fileDescFile.setSourceId(sourceFileDir.getSourceFileDirId());
						logger.info("加入文件队列："+fileDescFile.getFileName());
						fileDescQueue.push(fileDescFile);
						if(fileDescFile.getModifyDate()>sourceFile.getModifyDate().getTime()){
							sourceFile.setModifyDate(new Date(fileDescFile.getModifyDate()));
							sourceFile.setMaxFile(fileDescFile.getFileName());
							sourceFile.setMaxFileSize(fileDescFile.getFileSize());
						}
					}
					logger.info("保存最新文件记录");
					stepService.saveSourceFile(sourceFile);
					
				}
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}finally{
				if(fileTransferClient!=null)
					fileTransferClient.disconnectFtpClient();
			}
		}
		return true;
	}

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
