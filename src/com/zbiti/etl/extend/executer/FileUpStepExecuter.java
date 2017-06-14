package com.zbiti.etl.extend.executer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

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
import com.zbiti.etl.extend.vo.FileUpStep;

/**
 * 
 * @author 严海平
 *
 */
public class FileUpStepExecuter implements ICommandExecuter<Boolean>{

	protected static final Log logger=LogFactory.getLog(SqlStepExecuter.class);
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step,Command command,IFileDescQueue fileDescQueue) throws Exception {
		logger.info("文件上传步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		@SuppressWarnings("unchecked")
		List<MorphDynaBean> fileQueue=(List<MorphDynaBean>) command.getParam().get("FILE_QUEUE");
		if(fileQueue==null||fileQueue.isEmpty()){
			logger.info("队列为空，退出");
			return false;
		}
		FileUpStep fileUpStep=stepService.getFileUpStepByStepId(step.getStepId());
		IFileTransferClient fileTransferClient = null;
		try{
			fileTransferClient=fileTransferService.getClient(fileUpStep.getResourceName());
			fileTransferClient.login();
			for(MorphDynaBean bean:fileQueue){
				FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
				String fromPath=fileDesc.getFileName();
				if(new File(fromPath).exists()&&new File(fromPath).length()==0){//必须文件存在才判断文件大小，文件不存在则往下走抛出异常
					logger.info("文件["+fromPath+"]字节数为0，continue");
					continue;
				}
				String toFileName=getToPathFile(fileUpStep, fromPath);
				if(fileUpStep.getIsGz()==1){//压缩
					toFileName+=".gz";
					logger.info("文件需要压缩，压缩文件");
					GZip.compressFile(fromPath, fromPath.substring(0,fromPath.lastIndexOf("/")+1)+toFileName);
					fromPath=fromPath.substring(0,fromPath.lastIndexOf("/")+1)+toFileName;
				}
				String toPath=StringUtil.dealFilePath(fileUpStep.getUpPath(),fileUpStep.getUpPathOffset())+"/"+toFileName;
				fileTransferClient.upload(fromPath, toPath+".tmp");
				fileTransferClient.rename(toPath+".tmp", toPath);
				fileDescQueue.push(fileDesc);
			}
		}catch (Exception e) {
			logger.error("文件上传出错：",e);
			throw e;
		}finally{
			if(fileTransferClient!=null)
				fileTransferClient.disconnectFtpClient();
		}
		return true;
	}

	private String getToPathFile(FileUpStep fileUpStep,String fromPath){
		String resultFilePath=fromPath.substring(fromPath.lastIndexOf("/")+1);
		if(fileUpStep.getIsAppendTime()!=0&&fileUpStep.getIsAppendTime()!=1){//1表示不追加
			SimpleDateFormat sdf=new SimpleDateFormat(fileUpStep.getAppendTimePattern().trim());
			String timeid=sdf.format(new Date(System.currentTimeMillis()+fileUpStep.getAppendTimeOffset()*24*60*60*1000));
			String fromPathFile=fromPath.substring(fromPath.lastIndexOf("/")+1);
			if(fileUpStep.getIsAppendTime()==2){//文件前
				resultFilePath=timeid+"_"+fromPathFile;
			}else if (fileUpStep.getIsAppendTime()==3){//后缀前
				if(fromPathFile.indexOf(".")!=-1){
					resultFilePath=fromPathFile.substring(0,fromPathFile.indexOf("."))+"_"+timeid+fromPathFile.substring(fromPathFile.indexOf("."));
				}else{
					resultFilePath=fromPathFile+"_"+timeid;
				}
			}else if (fileUpStep.getIsAppendTime()==4){//文件名后
				resultFilePath=fromPathFile+"_"+timeid;
			}
		}
		return resultFilePath;
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
