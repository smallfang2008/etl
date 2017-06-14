package com.zbiti.etl.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbiti.common.RunShellUtil;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.extend.vo.ShellStep;

public class TestShell {

	public static void main(String[] args) {
//		System.out.println(RunShellUtil.cmdExcute(". /data/nocwg/.bash_profile\ntimeid=`date`\necho '1234'\nhive -e \"select 1 from xxx\" > /data/nocwg/tmpdata/1.txt", "132.228.28.189", "nocwg", "znwg.2017"));
		try {
			RunShellUtil.cmdExcute("sqlldr", "132.228.28.189", "nocwg", "znwg.2017");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"config/applicationContext-common.xml"});
//		IStepService stepService=ctx.getBean(IStepService.class);
//		ShellStep shellStep=stepService.getShellStep("202");
//		System.out.println(shellStep.getShellCommands().replaceAll("\r\n", ""));
//		System.out.println(RunShellUtil.cmdExcute(shellStep.getShellCommands().replaceAll("\r", ""), "132.228.28.189", "nocwg", "znwg.2017"));
//		System.exit(0);
	}
}
