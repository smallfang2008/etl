package com.zbiti.etl.extend.smo.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogWriter;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobEntryLoader;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.StepLoader;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.stereotype.Service;

import com.zbiti.common.FileUtil;
import com.zbiti.etl.extend.smo.IKettleRunService;

@Service
public class KettleRunServiceImpl implements IKettleRunService{
	protected static final Log logger = LogFactory.getLog(KettleRunServiceImpl.class);

	@Override
	public void executeConvert(String fileName,byte[] fileStream) throws Exception {
		//初始化kettle
		EnvUtil.environmentInit();
		try {
			StepLoader.init();
		} catch (KettleException e1) {
			logger.error(e1.getMessage(),e1);
		}

		File file = null;
		FileOutputStream fos=null;
		try {
			file=new File(new File(".").getCanonicalPath()+"/kettleConvert/"+fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(file.exists())
				file.delete();
			fos= new FileOutputStream(file);
			fos.write(fileStream);
		}catch (Exception e) {
			logger.error("转换文件写入失败！",e);
			throw e;
		}finally{
			if(fos!=null)
				fos.close();
		}
		
		TransMeta transMeta = new TransMeta(file.getAbsolutePath());
		Trans trans = new Trans(transMeta);
		trans.execute(null);
		trans.waitUntilFinished();
		if (trans.getErrors() > 0) {
			throw new RuntimeException("There were errors during transformation execution");
		}
	}

	@Override
	public void executeJob(String fileName,byte[] fileStream) throws Exception {
		EnvUtil.environmentInit();
		try {
			StepLoader.init();
		} catch (KettleException e1) {
			logger.error(e1.getMessage(),e1);
		}
		File file = null;
		FileOutputStream fos=null;
		try {
			file=new File(new File(".").getCanonicalPath()+"/kettleJob/"+fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(file.exists())
				file.delete();
			fos= new FileOutputStream(file);
			fos.write(fileStream);
		}catch (Exception e) {
			logger.error("Job文件写入失败！",e);
			throw e;
		}finally{
			if(fos!=null)
				fos.close();
		}
		//执行路径-目录名称截取掉kettle文件名后缀
		String execPath=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("."));
		//如果执行过，先删除上一次的目录
		FileUtil.deleteDir(execPath);
		//解压文件到执行目录
		FileUtil.unzipFile(file, execPath);
		for(String subFileName:new File(execPath).list()){
			if(subFileName.endsWith("001.kjb")){
				logger.info("开始执行文件："+execPath+"/"+subFileName);
				//执行
				this.callNativeJob(execPath+"/"+subFileName);
				logger.info("结束执行文件："+execPath+"/"+subFileName);
			}
		}
	}

	/**
	 * 执行本地的任务文件
	 * 
	 * @Description:
	 * @param jobFileName
	 * @throws KettleException
	 * @author yhp
	 * @since：2017-1-11 11:17:01
	 */
	public void callNativeJob(String jobFileName) throws KettleException {
		// 初始化
		EnvUtil.environmentInit();
		JobEntryLoader.init();
		StepLoader.init();
		// 日志
		LogWriter log = LogWriter.getInstance("TransTest.log", true,
				LogWriter.LOG_LEVEL_ERROR);
		// job元对象
		JobMeta jobMeta = new JobMeta(log, jobFileName, null);
		// job
		Job job = new Job(log, StepLoader.getInstance(), null, jobMeta);
		jobMeta.setInternalKettleVariables(job);
		// 执行job
		job.execute();
		// 等待job执行结束
		job.waitUntilFinished();
		
	}
}
