package com.zbiti.etl.extend.executer;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.DBCommon;
import com.zbiti.common.FileUtil;
import com.zbiti.common.RunShellUtil;
import com.zbiti.common.StringUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IFileTransferClient;
import com.zbiti.etl.core.smo.IFileTransferService;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.IQLoadStep;

public class IQLoadStepExecuter implements ICommandExecuter<Boolean>{

	private Log logger = LogFactory.getLog(IQLoadStepExecuter.class);

	IStepService stepService;
	IResourceService resourceService;
	IFileTransferService fileTransferService;
	private String tmpFtp="loginst@132.228.165.146";
	private String iq_ossne_data_path = "/ossne_data/etl_tmp_data/";
	private String iqLoadUri="jdbc:sybase:Tds:132.228.165.134:2638/ossne";
	private String iqLoadDriver="com.sybase.jdbc3.jdbc.SybDriver";
	private String iqLoadUsername="dba";
	private String iqLoadPassword="sql";
	
	IFileTransferClient fileTransferClient = null;
	Resource resourceTmp=null;
	
	@Override
	public Boolean execute(ApplicationContext ctx, Node node, Step step,
			Command command, IFileDescQueue fileDescQueue) throws Exception {
		logger.info("SybaseIQ加载步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		logger.info("获取队列");
		@SuppressWarnings("unchecked")
		List<MorphDynaBean> fileQueue=(List<MorphDynaBean>) command.getParam().get("FILE_QUEUE");
		if(fileQueue==null||fileQueue.isEmpty()){
			logger.info("队列为空，退出");
			return false;
		}
		logger.info("加载IQLoad配置");
		IQLoadStep iqLoadStep=stepService.getIQLoadStepCache(step.getStepId());
		logger.info("获取资源："+iqLoadStep.getServerName());
		Resource resource=resourceService.getByNameCache(iqLoadStep.getServerName());
		
		if(iqLoadStep.getPrepareSql()!=null&&!iqLoadStep.getPrepareSql().trim().equals("")){
			DBCommon dbCommon=new DBCommon(3, 3);
			logger.info("执行预处理SQL");
			Connection connection=dbCommon.getConnection(resource.getDriver(), resource.getUri(), resource.getUserName(), resource.getPassword());
			dbCommon.execSqls(iqLoadStep.getPrepareSql(), resource.getDriver(), connection);
		}
		logger.info("队列文件入库");
		
		try{
			resourceTmp=resourceService.getByNameCache(tmpFtp);
			fileTransferClient=fileTransferService.getClient(resourceTmp);
			fileTransferClient.login();
			for(MorphDynaBean bean:fileQueue){
				FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
				logger.info("file["+fileDesc.getFileName()+"]开始加载！");
				doLoad(iqLoadStep,fileDesc, resource);
				logger.info("file["+fileDesc.getFileName()+"]加载结束！");
				logger.info("传递文件队列");
				fileDesc.setServerName(node.getServerName());
				fileDescQueue.push(fileDesc);
			}
		}catch (Exception e) {
			throw e;
		}finally{
			if(fileTransferClient!=null)
				fileTransferClient.disconnectFtpClient();
		}
		if(iqLoadStep.getPostSql()!=null&&!iqLoadStep.getPostSql().trim().equals("")){
			DBCommon dbCommon=new DBCommon(3, 3);
			logger.info("执行后处理SQL");
			Connection connection=dbCommon.getConnection(resource.getDriver(), resource.getUri(), resource.getUserName(), resource.getPassword());
			dbCommon.execSqls(iqLoadStep.getPostSql(), resource.getDriver(), connection);
		}
		return null;
	}

	private void doLoad(IQLoadStep iqLoadStep,FileDesc fileDesc,Resource resource) throws Exception{
		String fileEncode=iqLoadStep.getEncoding();
		if(fileEncode==null||"".equals(fileEncode)){
			fileEncode="utf-8";
		}
		logger.info("将文件转码("+fileEncode+"到GB18030)");
		String codeFileName=fileDesc.getFileName() + ".code" ;
		logger.info("执行脚本：iconv -s -f UTF-8 -t GB18030 " + fileDesc.getFileName() + " > "+ codeFileName);
		String[] shells={
				"/bin/sh",
				"-c",
				"iconv -s -f UTF-8 -t GB18030 " + fileDesc.getFileName() + " > "+ codeFileName
				};		
		RunShellUtil.runShell(shells);
		if(!new File(codeFileName).exists()){
			throw new Exception("编码转换后的文件不存在");
		}
		logger.info("将文件上传至"+tmpFtp);
		String topath=iq_ossne_data_path+StringUtil.getFileNameByDirectory(codeFileName);
		fileTransferClient.upload(codeFileName, topath);
		logger.info("为上传上去的文件赋权");
		RunShellUtil.cmdExcute("chmod 777 "+topath, resourceTmp.getHostName(), resourceTmp.getUserName(), resourceTmp.getPassword());
		logger.info("数据入库");
		fileInIq(iqLoadStep, topath);
		logger.info("删除转码文件");
		FileUtil.doDeleteFile(codeFileName);
		logger.info("删除上传至"+tmpFtp+"上的文件");
		RunShellUtil.cmdExcute("rm -f "+topath, resourceTmp.getHostName(), resourceTmp.getUserName(), resourceTmp.getPassword());
		
	}
	
	
	private void fileInIq(IQLoadStep iqLoadStep,String filepath) throws Exception{
		DBCommon db=new DBCommon(3,3);
		Connection connection=db.getConnection(iqLoadDriver, iqLoadUri, iqLoadUsername, iqLoadPassword);
		String sql = "LOAD table  "
				+ getNewTableName(iqLoadStep.getTableName(), filepath) + "\n"
				+ " ( " + iqLoadStep.getColumns() + ") from '" + filepath
				+ "' \n QUOTES OFF  \n " + " ESCAPES OFF" + "\n"
				+ " WITH CHECKPOINT OFF \n ";
		db.execSql(sql, iqLoadDriver, connection);
	}
	
	/**
	 * 根据表名的后缀，然后根据文件名自动获取表名
	 * TABLE_201510 月表
	 * TABLE_20151025 日表
	 * 根据文件名，查找  2015或 2016 或 2017 关键字然后在根据是日，向后查几个字符，获取日或月
	 * @param tableName
	 * @return
	 */
	private String getNewTableName(String tableName,String filePathName) {
		tableName=tableName.toUpperCase().replace("DW.", "");
		tableName="DW."+tableName;
		int yyyymmOffset=tableName.lastIndexOf("YYYYMM");
		int yyyymmddOffset=tableName.lastIndexOf("YYYYMMDD");
		 if(yyyymmOffset>0 || yyyymmddOffset>0) {
			 int tmp=filePathName.lastIndexOf("/");
			 String fileNanme=filePathName;
			 if(tmp>0){
				 fileNanme=filePathName.substring(tmp, filePathName.length());
			 }
			 String year= this.getYear(fileNanme);
			 if("".equals(year)){
				 return tableName;
			 }
			 int yearoffset=fileNanme.indexOf(year);
			 if(yearoffset>0){
				 int offset=0;
				 String dateStr="";
				 if(yyyymmddOffset>0){
					 offset=8;
					 dateStr= fileNanme.substring(yearoffset, yearoffset+offset);
					 tableName=tableName.replace("YYYYMMDD", dateStr);					 
				 }else if(yyyymmOffset>0){
					 offset=6;
					 dateStr= fileNanme.substring(yearoffset, yearoffset+offset);
					 tableName=tableName.replace("YYYYMM", dateStr);
				 }
			 }
		 }
		return tableName;
	}
	
	/**
	 * 获取文件名中year字符 ，年份
	 * @param fileNanme
	 * @return
	 */
	private String getYear(String fileNanme) {
		int nowYear=2016;
		for (int i=0;i<100;i++){
			nowYear=2016+i;
			int iss=fileNanme.indexOf(""+nowYear);
			if(iss>0){
				return ""+nowYear;
			}
		}
		return "";
	}

	public IStepService getStepService() {
		return stepService;
	}

	public void setStepService(IStepService stepService) {
		this.stepService = stepService;
	}

	public IResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public IFileTransferService getFileTransferService() {
		return fileTransferService;
	}

	public void setFileTransferService(IFileTransferService fileTransferService) {
		this.fileTransferService = fileTransferService;
	}

	
}
