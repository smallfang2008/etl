package com.zbiti.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 更改文件的权限
 * 
 * @author ludianlong
 * 
 */
public class FileUtil {

	public final static String COMPRESS_NO="0";
	public final static String COMPRESS_GZ="1";
	public final static String COMPRESS_TAR_GZ="2";
	
	
	public final static Log logger = LogFactory.getLog(FileUtil.class);

	public static void mkdir(String filePathName) throws Exception {
		if (filePathName == null || "".equals(filePathName)) {
			return;
		}
		String[] fs = filePathName.split("/");
		String path = "";
		for (int i = 0; i < fs.length; i++) {
			if ("".equals(fs[i])) {
				continue;
			}
			path = path + "/" + fs[i];
			File f = new File(path);
			if (f.isFile()) {
				chmodFile(path);
			} else if (!f.exists()) {
				f.mkdirs();
				chmodFile(path);
			}
		}
	}

	/*
	 * 给文件赋予权限
	 */
	public static void chmodFile(String toPathFileName) throws Exception {
		File file = new File(toPathFileName);
		if (file.exists()) {
			Process p = null;
			String cmd = "chmod 777 " + toPathFileName;
			try {
				p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (p != null) {
					p.destroy();
				}
			}
		}
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param pathFileName
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static File writeToFile(String pathFileName, String data)
			throws Exception {
		// 生成ctl
		File ctlFile = null;
		FileWriter fileWrite = null;
		try {
			ctlFile = new File(pathFileName);
			File parent = ctlFile.getParentFile();
			if (parent != null && !parent.exists()) {
				// parent.mkdirs();
				FileUtil.mkdir(ctlFile.getParent());
			}
			ctlFile.createNewFile();
			FileUtil.chmodFile(pathFileName);

			fileWrite = new FileWriter(ctlFile, true);
			fileWrite.write(data);
			fileWrite.flush();
			return ctlFile;
		} finally {
			try {
				if (fileWrite != null)
					fileWrite.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param pathFileName
	 * @param data
	 * @return
	 * @throws Exception
	 */
//	public static void writeToFile(String path, String data,boolean append)
//			throws Exception {
//		File file=new File(path);
//		if(file.exists()){
//			if(!append){
//				file.delete();
//				file.createNewFile();
//			}
//		}else{
//			if(!file.getParentFile().exists()){
//				file.getParentFile().mkdirs();
//			}
//			file.createNewFile();
//		}
//		FileUtil.chmodFile(path);
//		PrintWriter pw=null;
//		try{
//			pw=new PrintWriter(file);
//			pw.append(data);
//			pw.flush();
//		}catch (Exception e) {
//			throw e;
//		}finally{
//			if(pw!=null)
//				pw.close();
//		}
//	}
	
	/**
	 * 写数据到文件中
	 * 
	 * @param pathFileName
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static File writeToFileV2(String pathFileName, String data)
			throws Exception {
		// 生成ctl
		File ctlFile = null;
		FileWriter fileWrite = null;
		try {
			ctlFile = new File(pathFileName);
			File parent = ctlFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			ctlFile.createNewFile();
			FileUtil.chmodFile(pathFileName);

			fileWrite = new FileWriter(ctlFile, true);
			fileWrite.write(data);
			fileWrite.flush();
			return ctlFile;
		} finally {
			try {
				if (fileWrite != null)
					fileWrite.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 文件移动的到新的目录下
	 * 
	 * @param mf
	 * @param bakpathFile
	 * @throws Exception
	 */
	public static void doRemoveFile(String from, String topath)
			throws Exception {
		File fromFile = new File(from);
		// 获取文件名

		File ftopath = new File(topath);
		// String parent = toFile.getParent();
		// File parentFile = new File(parent);
		if (!ftopath.exists()) {
			// parentFile.mkdirs();
			FileUtil.mkdir(topath);
		}

		int last = from.lastIndexOf("/");
		if (last <= 0) {
			return;
		}
		String tofileName = from.substring(last + 1, from.length());
		String toPathFile = topath + "/" + tofileName;
		File ftoPathFile = new File(toPathFile);
		if (ftoPathFile.exists()) {
			ftoPathFile.delete();
		}
		if (fromFile.exists()) {
			fromFile.renameTo(ftoPathFile);
		}

		System.out.println("remove succuss, file from = " + from + " to ="
				+ tofileName);
	}

	/**
	 * 脚本文件.sh .ctl 文件到bak目录下 ludl edit for 入库之后之间删除 转换之后的文件，控制文件和shell文件
	 * 
	 * @param fromPathFileName
	 * @throws Exception
	 */
	public static void rmFile(String csvFileName, String ctlFileName,
			String shFileName, String csvFilePath) throws Exception {

		// csv file delete
		File csvfile = new File(csvFileName);
		if (csvfile.exists()) {
			csvfile.delete();
			logger.info("do delete file name = " + csvFileName);
		}

		// ctl file delete
		File ctlfile = new File(csvFilePath + "/" + ctlFileName);
		if (ctlfile.exists()) {
			ctlfile.delete();
			logger.info("do delete file name = " + csvFilePath + "/"
					+ ctlFileName);
		}

		// sh file delete
		File shfile = new File(shFileName);
		if (shfile.exists()) {
			shfile.delete();
			logger.info("do delete file name = " + shFileName);
		}

		// log file delete
		File logfile = new File(csvFilePath + "/" + ctlFileName + ".log");
		if (logfile.exists()) {
			logfile.delete();
			logger.info("do delete file name = " + csvFilePath + "/"
					+ ctlFileName + ".log");
		}

		/*
		 * int last = csvFilePath.lastIndexOf("/"); String csvparaentPathbak =
		 * ""; if (last > 0) { csvparaentPathbak = csvFilePath.substring(0,
		 * last) + "_bak"; } else { csvparaentPathbak = csvFilePath + "_bak"; }
		 * // ctl file FileUtil.doRemoveFile(csvFilePath + "/" + ctlFileName,
		 * csvparaentPathbak + "/" + ctlFileName);
		 * logger.info("do remove succuss file name = " + ctlFileName); // sh
		 * file FileUtil.doRemoveFile(shFileName, csvparaentPathbak);
		 * logger.info("do remove succuss file name = " + shFileName); // log
		 * file FileUtil.doRemoveFile(csvFilePath + "/" + ctlFileName + ".log",
		 * csvparaentPathbak + "/" + ctlFileName + ".log");
		 * logger.info("do remove succuss file name = " + csvFilePath + "/" +
		 * ctlFileName + ".log");
		 */
	}


	public static void getSonFilePathByDir(String dir, List<String> filePath) {
		File sourceFileDir = new File(dir);
		if (!sourceFileDir.exists())
			return;
		File[] sonFileArray = sourceFileDir.listFiles();
		for (File sonFile : sonFileArray) {
			if (sonFile.isFile())
				filePath.add(sonFile.getAbsolutePath());
			if (sonFile.isDirectory())
				getSonFilePathByDir(sonFile.getAbsolutePath(), filePath);
		}
	}

	/**
	 * stop thread 
	 * @return
	 */
	public static boolean stop() {
		try{
			String stoppath=StringUtil.getFilePath("stop");
			File runfile = new File(stoppath);
			if (runfile.exists()) {
				logger.info("i have get the stop file ,stop the server..........");
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		} 
		return false;
	}


	/**
	 * 写数据到文件中
	 * 
	 * @param pathFileName
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static void appendDataToFile(String pathFileName, String data){
		File ff = null;
		FileWriter fileWrite = null;
		try {
			ff = new File(pathFileName);
			File parent = ff.getParentFile();
			
			if (parent != null && !parent.exists()) {
				FileUtil.mkdir(pathFileName);
			}
			if(!ff.exists()){
				ff.createNewFile();
			}
			FileUtil.chmodFile(pathFileName);
			fileWrite = new FileWriter(ff, true);
			fileWrite.write(data);
			fileWrite.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				if (fileWrite != null)
					fileWrite.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 文件删除,同时做文件的记录
	 * mnt/disk1/convert/aa.txt;
	 * @param filePatheName
	 */
	public static void doDeleteFile(String filePatheName) {
		File file = new File(filePatheName);
		if (file.exists()) {
			String date=TimeUtil.dateFormat(new Date());
			String data=date+" "+filePatheName+" " +file.length();
			String filePath=filePatheName.substring(0, filePatheName.lastIndexOf("/"))+"/file.txt \r\n";
			FileUtil.appendDataToFile(filePath,data);
			file.delete();
			logger.info("do delete file name = " + filePatheName);
		}
	}
	
	/**
	 * iqisql -U dw -P dw -S ossne_B -i
	 * /ossnq_data/apk_test/data//APKdownload/convert
	 * /20140609201750809_U0NUT5.ctl
	 * 
	 * @param shFilePathName
	 * @throws Exception
	 */
	public static void doRunCmd(String shFilePathName) throws Exception {
		Process process = null;
		Future<Boolean> future=null;
		try {
			logger.info("run the command = " + "sh " + shFilePathName);
			process = Runtime.getRuntime().exec("sh " + shFilePathName);
			// 增加数据流的输出，解决卡死的问题
//			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
//					"ERROR");
//			// kick off stderr
//			errorGobbler.start();
//			StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(),
//					"STDOUT");
//			// kick off stdout
//			outGobbler.start();
			
			ExecutorService serviceNomal = Executors.newSingleThreadExecutor();
			ExecutorService serviceError = Executors.newSingleThreadExecutor();
			final BufferedReader bfNomal = new BufferedReader(
					new InputStreamReader(process.getInputStream(), "UTF-8"));
			final BufferedReader bfError = new BufferedReader(
					new InputStreamReader(process.getErrorStream(), "UTF-8"));
			serviceNomal.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					String s2 = null;
					while ((s2 = bfNomal.readLine()) != null) {
						logger.info(s2);
					}
					return null;
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
			if(future.get()){
				throw new Exception("shell "+shFilePathName+"执行异常");
			}
		}
	}
	
	public static String readTail(String filepath, int lines, String encoding) {
		StringBuffer sb = new StringBuffer();
		File f = new File(filepath);
		try {
			FileInputStream in = new FileInputStream(f);
			InputStreamReader reader = new InputStreamReader(in,encoding);
			BufferedReader br = new BufferedReader(reader);
			long available = in.available();
			
			long skip=available-lines;
			if (skip>0){
				in.skip(skip);
			}
			String s=null;
			while((s=br.readLine())!=null){
				sb.append(s+"<br/>");
			}
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return sb.toString();
		}
	}
	
	public static void unzipFile(File file,String destDir) throws Exception{
		ZipFile zipFile=new ZipFile(file);

		File unzipFile = new File(destDir);
		if(unzipFile.exists()){
			unzipFile.delete();
		}
		if(!unzipFile.exists()){//创建解压缩文件夹
			unzipFile.mkdirs();
		}
		Enumeration<?> zipEnum =zipFile.entries();
		OutputStream out = null;
	    InputStream in=null;
	    try{
			while(zipEnum.hasMoreElements()){
				ZipEntry entry = (ZipEntry) zipEnum.nextElement();
				String entryName = new String(entry.getName().getBytes("ISO8859_1"));
				if (entry.isDirectory()){
				    new File(unzipFile.getAbsolutePath() + "/" + entryName).mkdir();
				    continue;
				}
				in = zipFile.getInputStream(entry); 
				out = new FileOutputStream(new File(unzipFile.getAbsolutePath()+ "/" + entryName));
				byte[] buf1 = new byte[1024]; 
		    	int len; 
		    	while ((len = in.read(buf1)) > 0) { 
		    		out.write(buf1, 0, len); 
		    	}
			}
	    }catch (Exception e) {
			throw e;
		}finally{
			if(in!=null)
				in.close();
			if(out!=null)
				out.close();
		}
	}
	
	public static void deleteDir(String dirPath){
		File dir=new File(dirPath);
		if(!dir.exists())
			return;
		if(!dir.isDirectory())
			return;
		File[] subFiles=dir.listFiles();
		
		for(File subFile:subFiles){
			if(subFile.isFile())
				subFile.delete();
			if(subFile.isDirectory())
				deleteDir(subFile.getAbsolutePath());
		}
		dir.delete();
	}
}
