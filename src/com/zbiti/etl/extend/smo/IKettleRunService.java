package com.zbiti.etl.extend.smo;

import org.pentaho.di.core.exception.KettleException;


/**
 * @author yhp
 * kettle运行时服务类
 */
public interface IKettleRunService {
	
	/**
	 * 执行转换
	 * @throws KettleException 
	 */
	public void executeConvert(String fileName,byte[] fileStream) throws Exception;
	
	/**
	 * 执行job
	 * @throws KettleException
	 */
	public void executeJob(String fileName,byte[] fileStream) throws Exception;
	
}
