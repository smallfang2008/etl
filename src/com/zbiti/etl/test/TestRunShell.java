package com.zbiti.etl.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.zbiti.common.RunShellUtil;

public class TestRunShell {
	protected static final Log logger = LogFactory.getLog(TestRunShell.class);
	public static void main(String[] args) {
//		RunShellUtil.cmdExcute("chmod 777 /ossne_data/etldata/2_dhcp_device_online_201610130855.txt_5gPy6.csv.code", "132.228.165.146", "loginst", "loginst_123");
		
//		Object a=false;
//		if((Boolean)a){
//			System.out.println(1);
//		}else{
//			System.out.println(2);
//		}

		logger.info("前置打印");
		Appender appender = Logger.getRootLogger().getAppender("File"); 
		if(appender instanceof FileAppender){
			FileAppender fappender = (FileAppender) appender; 
			fappender.setFile("D:/test.log.20161027");
			fappender.activateOptions();
			logger.info("设置成功");
		}
		logger.info("后续打印");
	}
}
