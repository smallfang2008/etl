package com.zbiti.etl.extend.executer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zbiti.common.FileUtil;
import com.zbiti.common.StringUtil;
import com.zbiti.etl.core.executer.ICommandExecuter;
import com.zbiti.etl.core.smo.IFileDescQueue;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.FileDesc;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.extend.vo.OracleExpStep;

/**
 * 
 * @author 严海平
 *
 */
public class OracleExpStepExecuter implements ICommandExecuter<Boolean>{

	protected static final Log logger=LogFactory.getLog(SqlStepExecuter.class);
	Connection connection = null;
	@Override
	public Boolean execute(ApplicationContext ctx,Node node,Step step,Command command,IFileDescQueue fileDescQueue) throws Exception {
		logger.info("oracle数据导出步骤-"+step.getStepName()+"["+step.getStepId()+"]开始执行!");
		try{
			logger.info("获取oracle数据导出步骤配置");
			OracleExpStep oracleExpStep=stepService.getOracleExpStepCache(step.getStepId());
			logger.info("获取远程连接["+oracleExpStep.getResourceName()+"]资源");
			Resource resource=resourceService.getByNameCache(oracleExpStep.getResourceName());
			
			String sqls=oracleExpStep.getSqls();
			logger.info("----------------------------EXP SQL START------------------------------\n"+sqls);
			logger.info("----------------------------EXP SQL END------------------------------");
			
			String filename=StringUtil.dealFilePath(oracleExpStep.getFilename(),oracleExpStep.getFileTimeOffset());
			String filenamePath=node.getServerCluster().getRootPath()+"/"+step.getScene().getName()+"/oracleExp/"+filename;
			String shFilePath=filenamePath+".sh";
//			String sqlFilePath=filenamePath+".sql";
//			logger.info("创建sql文件");
//			createSqlFile(filenamePath, sqlFilePath, sqls);
			logger.info("创建sh文件："+shFilePath);
			createShFile(filenamePath, shFilePath, sqls, resource);

			logger.info("执行sh文件");
			doRunCmd(shFilePath);//+" >> /dev/null"
			logger.info("推送文件队列");
			FileDesc fileDesc=new FileDesc();
			fileDesc.setFileName(filenamePath);
			fileDesc.setServerName(node.getServerName());
			fileDesc.setSourceType("1");
			fileDesc.setCompressType(FileUtil.COMPRESS_NO);
			fileDescQueue.push(fileDesc);
			
		}catch(Exception ex){
			command.setExecInfo("ORACLE数据导出失败！"+ex.getMessage());
			throw ex;
		}

		return true;
	}
	
	
	private void createShFile(String filenamePath,String shFilePath,String sqls,Resource resource) throws Exception{
		StringBuilder shContent=new StringBuilder();
		String dbStr=resource.getUserName()+"/"+resource.getPassword()+"@"+resource.getServiceName();
		shContent.append("rm -f "+filenamePath+"\n");
		shContent.append("touch "+filenamePath+"\n");
		shContent.append("sqlplus -S "+dbStr+"<<!\n");
		shContent.append("SET LINESIZE 10000;\n");
		shContent.append("SET PAGESIZE 0;\n");
		shContent.append("SET TRIMSPOOL ON;\n");
		shContent.append("SET ECHO OFF;\n");
		shContent.append("SET FEEDBACK OFF;\n");
		shContent.append("SPOOL "+filenamePath+";\n");
		shContent.append(sqls+";\n");
		shContent.append("SPOOL OFF;\n");
		shContent.append("quit;\n");
		shContent.append("!");
		new File(shFilePath).delete();
		FileUtil.writeToFile(shFilePath, shContent.toString());
	}
	
//	private void createSqlFile(String filenamePath,String sqlFilePath,String sqls) throws Exception{
//		StringBuilder shContent=new StringBuilder();
//		shContent.append("SET LINESIZE 10000;\n");
//		shContent.append("SET PAGESIZE 0;\n");
//		shContent.append("SET TRIMSPOOL ON;\n");
//		shContent.append("SET ECHO OFF;\n");
//		shContent.append("SET FEEDBACK OFF;\n");
//		shContent.append("SPOOL "+filenamePath+";\n");
//		shContent.append(sqls+";\n");
//		shContent.append("SPOOL OFF;\n");
//		new File(sqlFilePath).delete();
//		FileUtil.writeToFile(sqlFilePath, shContent.toString());
//	}
//	
//	private void createShFile(String filenamePath,String shFilePath,String sqlFilePath,Resource resource) throws Exception{
//		StringBuilder shContent=new StringBuilder();
//		String dbStr=resource.getUserName()+"/"+resource.getPassword()+"@"+resource.getServiceName();
//		shContent.append("rm -f "+filenamePath+"\n");
//		shContent.append("sqlplus -S "+dbStr+"<<!\n");
//		shContent.append("@"+sqlFilePath+"\n");
//		shContent.append("quit;\n");
//		shContent.append("!");
//		new File(shFilePath).delete();
//		FileUtil.writeToFile(shFilePath, shContent.toString());
//	}
	
//	String dealFilename(String filename,int timeoffset){
//		Pattern pattern = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
//        Matcher matcher = pattern.matcher(filename);
//        while(matcher.find()){
//        	filename=filename.replace("${"+matcher.group()+"}", new SimpleDateFormat(matcher.group()).format(new Date(System.currentTimeMillis()+timeoffset*24*60*60*1000)));
//        }
//        return filename;
//	}
	
	/**
	 * FileUtil中的公有方法，因spool无论如何此方法都会将查询结果输出到日志中，故此在此类中私有化此方法，不打印正常日志，错误日志正常输出
	 * @param shFilePathName
	 * @throws Exception
	 */
	private void doRunCmd(String shFilePathName) throws Exception {
		Process process = null;
		Future<Boolean> futureNormal=null;
		Future<Boolean> future=null;
		try {
			logger.info("run the command = " + "sh " + shFilePathName);
			process = Runtime.getRuntime().exec("sh " + shFilePathName);
			
			ExecutorService serviceNomal = Executors.newSingleThreadExecutor();
			ExecutorService serviceError = Executors.newSingleThreadExecutor();
			final BufferedReader bfNomal = new BufferedReader(
					new InputStreamReader(process.getInputStream(), "UTF-8"));
			final BufferedReader bfError = new BufferedReader(
					new InputStreamReader(process.getErrorStream(), "UTF-8"));
			futureNormal=serviceNomal.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					String s2 = null;
					boolean isError=false;
					while ((s2 = bfNomal.readLine()) != null) {
//						logger.info(s2);
						if(s2.contains("ERROR")){
							isError=true;//奇葩的ERROR打到正常里
						}
						if(isError){
							logger.error(s2);
						}
						
					}
					return isError;
				}

			});

			future=serviceError.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {

					String s = null;
					boolean isError=false;
					while ((s = bfError.readLine()) != null) {
						isError=true;
						logger.info(s);
					}
					// bfNomal.close();
					return isError;
				}
			});
			
			process.waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (process != null) {
				process.destroy();
			}
			if(futureNormal.get()||future.get()){
				throw new Exception("shell "+shFilePathName+"执行异常");
			}
		}
	}
	
	private IStepService stepService;
	private IResourceService resourceService;
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
