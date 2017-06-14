package com.zbiti.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class RunShellUtil {
	protected static final Log logger = LogFactory.getLog(RunShellUtil.class);
	/**
	 * 远程shell 命令
	 * @param cmd
	 * @param ipAddress
	 * @param username
	 * @param passwd
	 */
	public static void cmdExcute(String cmd,String ipAddress,String username,String passwd ) throws Exception{
		logger.info("[remote ip:"+ipAddress+",username:"+username+",password:"+passwd+"]run command:\n"+cmd);
		Connection conn = new Connection(ipAddress);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username, passwd);
	        if (isAuthenticated == false){
	       	 throw new IOException("Authentication failed.");
	        }
		} catch (IOException e) { 
			e.printStackTrace();
		}
//		StringBuilder sb = new StringBuilder();
		Session session = null;
//		Future<Boolean> future=null;
		try {
			session = conn.openSession();
			session.execCommand(cmd);
			
	        
			final ExecutorService serviceNomal = Executors.newSingleThreadExecutor();
	        final BufferedReader stdout = new BufferedReader(new InputStreamReader(session.getStdout()));
			serviceNomal.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					String s2 = null;
					while ((s2 = stdout.readLine()) != null) {
						logger.info(s2);
					}
					stdout.close();
					serviceNomal.shutdown();
					return null;
				}

			});

			final ExecutorService serviceError = Executors.newSingleThreadExecutor();
	        final BufferedReader stderr = new BufferedReader(new InputStreamReader(session.getStderr()));
//	        future=
	        serviceError.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {

					String s = null;
					boolean isError=false;
					while ((s = stderr.readLine()) != null) {
						isError=true;
						logger.info(s);
					}
					stderr.close();
					serviceError.shutdown();
					return isError;
				}
			});
	        session.waitForCondition(ChannelCondition.EXIT_STATUS, 24*60*60*1000);//一天的超时时间
	        if(session.getExitStatus()!=0){
	        	throw new Exception("远程shell命令执行异常！");
	        }
		} catch (IOException e) { 
			throw e;
		}finally{
			session.close();
			conn.close();
//			if(future.get()){
//				throw new Exception("shell 命令执行异常");
//			}
		}
	} 
	
	/**
	 * 直接在本地运行
	 * @param shell
	 */
	public static void runShell(String shell){
		System.out.println("shell==>"+shell);
		Process process = null;
		try {
			process=Runtime.getRuntime().exec(shell);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		finally{
			if(process!=null){
				process.destroy();
				System.out.println("process.destroy");
			}
		}
	}

	/**
	 * 直接在本地运行
	 * @param shell
	 */
	public static void runShell(String[] shell) throws Exception{
		Process process = null;
		try {
			process=Runtime.getRuntime().exec(shell);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (InterruptedException e) { 
			e.printStackTrace();
			throw e;
		}
		finally{
			if(process!=null){
				process.destroy();
				System.out.println("process.destroy");
			}
		}
	}
	public static void main(String[] args) {
		String cmd="chmod 777 /ossne_data/etldata/fff.csv";
		try {
			RunShellUtil.cmdExcute(cmd, "132.228.165.146", "loginst", "loginst_123");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*private  static  void testrestart(){
		RunShellUtil shellTest = new RunShellUtil();
		while(true){
			String processCount=shellTest.cmdExcute("ps -ef |grep tomcat | grep -v grep | wc -l", "132.228.28.52", "noc", "noc").toString().trim();
			System.out.println("ProcessCount:"+processCount);
			if("0".equals(processCount)){
				System.out.println("tomcat has stoped,restartting....");
				shellTest.cmdExcute(". /home/noc/.bash_profile\n/DPI_DATA/apache-tomcat-6.0.39/bin/startup.sh", "132.228.28.52", "noc", "noc");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
				if(!"0".equals(shellTest.cmdExcute("ps -ef |grep tomcat | grep -v grep | wc -l", "132.228.28.52", "noc", "noc").toString().trim())){
					System.out.println("tomcat restarted SUCCESS");
				}else{
					System.out.println("tomcat restarted FAILED");
				}
			}else{
				System.out.println("tomcat is running....");
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}
	}
	*/
	/**
	 * 查找计算进程数，没找到返回0 ,大于1找到了
	 * @param threadStr
	 * @return
	 */
	public static int getLocalThreadNum(String threadStr) {
		Process process = null;
		int num = 0;
		try {
			String[] cmd = { "sh", "-c", "ps -ef|grep "+threadStr+" |wc -l" };
			process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			BufferedReader inputBufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String lineStr = "";
			while ((lineStr = inputBufferedReader.readLine()) != null) {
				num = Integer.parseInt(lineStr);
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return num;

	}
	/**
	 * kill the thread
	 */
	public static void killThread(String commandId) {
		try {
			//String shell = "ps -ef|grep " + commandId+ " |grep -v grep|cut -c 9-15|xargs kill -9 ";
			String[] shell = {"sh","-c","ps -ef|grep " + commandId+ " |grep -v grep|cut -c 9-15|xargs kill -9 " };
			//process = Runtime.getRuntime().exec(cmd);
			logger.info("shell -c  ps -ef|grep " + commandId+ " |grep -v grep|cut -c 9-15|xargs kill -9");
			RunShellUtil.runShell(shell);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * change file chmode 777
	 * 
	 * @param toPathFile
	 */
	public static void shellChmodFile(String toPathFile) throws Exception{
		String cmd = "chmod 777 " + toPathFile;
		RunShellUtil
				.cmdExcute(cmd, "132.228.165.146", "loginst", "loginst_123");
	}
	
	/**
	 * change file chmode 777
	 * 
	 * @param toPathFile
	 */
	public static void deleteFile(String toPathFile) throws Exception{
		String cmd = "rm -f  " + toPathFile;
		RunShellUtil
				.cmdExcute(cmd, "132.228.165.146", "loginst", "loginst_123");
	}
	

}
