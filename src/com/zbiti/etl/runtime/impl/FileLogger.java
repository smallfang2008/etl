package com.zbiti.etl.runtime.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zbiti.etl.runtime.ILogger;

public class FileLogger implements ILogger{
	
	protected static final Log logger = LogFactory.getLog(FileLogger.class);
	
	String logPath;
	
	String commandId;
	File f;
	
	public FileLogger(String logPath,String commandId){
		this.logPath = logPath;
		this.commandId = commandId;
		createFile();
	}
	
	void createFile(){
		File dir = new File(logPath);
		dir.mkdirs();
		f = new File(dir.getPath()+"/"+commandId+".log");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void info(String info) {
//		createFile();
//		logger.info(info);
		FileOutputStream fos =null;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream(f,true);
			pw = new PrintWriter(fos);
			pw.println(info);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
		}finally{
			pw.close();
			try {
				fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
			
		}
	}

	public void info(String info, Exception exception) {
//		createFile();
//		logger.info(info,exception);
		FileOutputStream fos =null;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream(f,true);
			
			pw = new PrintWriter(fos);
			
			pw.println(info);
			exception.printStackTrace(pw);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
		}finally{
			pw.close();
			try {
				fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
			
		}
	}
	
}

