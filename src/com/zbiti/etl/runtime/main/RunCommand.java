package com.zbiti.etl.runtime.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.runtime.ICommandService;
import com.zbiti.etl.runtime.IProcess;

public class RunCommand {

	protected static final Log logger = LogFactory.getLog(RunCommand.class);
	
	public static void main(String[] args) {
		String commandPath=args[0];
//		String commandPath="/schedule/doing/3/1609221644015M2X_160922164401m0x4";
//		String commandPath="/schedule/doing/3/161009164001wq0o_161009164001DuhZ";
		if(args.length>1){
			String logPath=args[1]+"/"+commandPath.substring(commandPath.lastIndexOf("_")+1)+".log";
			Appender appender = Logger.getRootLogger().getAppender("File"); 
			if(appender instanceof FileAppender){
				FileAppender fappender = (FileAppender) appender; 
				fappender.setFile(logPath);
				fappender.activateOptions();
			}
		}
		logger.info("执行指令:"+args[0]);
		long startTime = System.currentTimeMillis();
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"config/applicationContext-common.xml"});
		ICommandService commandService=ctx.getBean(ICommandService.class);
		IProcess process=ctx.getBean(IProcess.class);
		try {
			process.setApplicationContext(ctx);
			Command cmd=commandService.getCommandByPath(commandPath);
			process.start(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		Object result = null;
		try {
			result = process.getResult();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			logger.info("执行结束："+result+",耗时"+(System.currentTimeMillis()-startTime)/1000+"秒");
			System.exit(0);
		}
		
	}
}
