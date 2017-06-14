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
import com.zbiti.common.TimeUtil;
import com.zbiti.common.json.JSONUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.executer.convert.ConvertUtil;
import com.zbiti.etl.extend.vo.OracleLoadStep;

public class OracleLoadStepExecuter implements ICommandExecuter<Boolean>{

	private Log logger=LogFactory.getLog(OracleLoadStepExecuter.class);
	IStepService stepService;
	IResourceService resourceService;
	
	@Override
	public Boolean execute(ApplicationContext ctx, Node node, Step step,
			Command command, IFileDescQueue fileDescQueue) throws Exception {
		logger.info("Oracle加载步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		logger.info("获取队列");
		List<MorphDynaBean> fileQueue=(List<MorphDynaBean>) command.getParam().get("FILE_QUEUE");
		if(fileQueue==null||fileQueue.isEmpty()){
			logger.info("队列为空，退出");
			return false;
		}
		logger.info("获取oracle加载步骤配置");
		OracleLoadStep oracleLoadStep=stepService.getOracleLoadStepCache(step.getStepId());
		logger.info("获取资源："+oracleLoadStep.getServerName());
		Resource resource=resourceService.getByNameCache(oracleLoadStep.getServerName());
		if(oracleLoadStep.getPrepareSql()!=null&&!oracleLoadStep.getPrepareSql().trim().equals("")){
			DBCommon dbCommon=new DBCommon(3, 3);
			logger.info("执行预处理SQL");
			Connection connection=dbCommon.getConnection(resource.getDriver(), resource.getUri(), resource.getUserName(), resource.getPassword());
			dbCommon.execSqls(oracleLoadStep.getPrepareSql(), resource.getDriver(), connection);
		}
		logger.info("队列文件入库");
		for(MorphDynaBean bean:fileQueue){
			FileDesc fileDesc=JSONUtil.parse(JSONUtil.toJsonString(bean), FileDesc.class);
			logger.info("file["+fileDesc.getFileName()+"]开始加载！");
			doLoad(oracleLoadStep,fileDesc, resource);
			logger.info("file["+fileDesc.getFileName()+"]加载结束！");
			logger.info("传递文件队列");
			fileDesc.setServerName(node.getServerName());
			fileDescQueue.push(fileDesc);
		}
		if(oracleLoadStep.getPostSql()!=null&&!oracleLoadStep.getPostSql().trim().equals("")){
			DBCommon dbCommon=new DBCommon(3, 3);
			logger.info("执行后处理SQL");
			Connection connection=dbCommon.getConnection(resource.getDriver(), resource.getUri(), resource.getUserName(), resource.getPassword());
			dbCommon.execSqls(oracleLoadStep.getPostSql(), resource.getDriver(), connection);
		}
		return true;
	}
	
	private void doLoad(OracleLoadStep oracleLoadStep,FileDesc fileDesc,Resource resource) throws Exception{
		logger.info("创建ctl文件！");
		String ctlFileName=createCtlFile(oracleLoadStep, fileDesc);
		logger.info("创建sh文件");
		String shFileName=createShFile(fileDesc, resource, ctlFileName);
		logger.info("执行sh文件");
		FileUtil.doRunCmd(shFileName);
	}

	
	private String createCtlFile(OracleLoadStep oracleLoadStep,FileDesc fileDesc) throws Exception{
		String ctlFileName=fileDesc.getFileName()+"."+TimeUtil.getNowDateTime()+".ctl";
		String dataSplit=oracleLoadStep.getDataSplit();
		if(dataSplit==null||"".equals(dataSplit))
			dataSplit=ConvertUtil.SPIT_SIGN;
		String encode=oracleLoadStep.getEncoding();
		if(encode==null||"".equals(encode)){
			encode="AL32UTF8";
		}
		String ctlContent = "load data  CHARACTERSET "+encode+" infile '"
				+ fileDesc.getFileName() + "'  append into  " + " table  "
				+ oracleLoadStep.getTableName() + "  fields   terminated by '"
				+ dataSplit + "' TRAILING nullcols " + " ( "
				+ oracleLoadStep.getColumns() + " )";
		FileUtil.writeToFile(ctlFileName, ctlContent);
		return ctlFileName;
	}
	
	private String createShFile(FileDesc fileDesc,Resource resource,String ctlFileName) throws Exception{
		String shFileName=fileDesc.getFileName()+".sh";
		new File(shFileName).delete();
		String dbStr=resource.getUserName()+"/"+resource.getPassword()+"@"+resource.getServiceName();
		String shContent = "su - root -c \" sqlldr  userid='" + dbStr
				+ "' rows=50000 readsize=20971520 bindsize=20971520 control='" + ctlFileName + "' log='" + ctlFileName+ ".log' \" ";//direct=true 
		FileUtil.writeToFile(shFileName, shContent);
		return shFileName;
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
	
}
