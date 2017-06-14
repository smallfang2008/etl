package com.zbiti.etl.core.smo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import com.zbiti.common.ConfigUtil;
import com.zbiti.common.FileUtil;
import com.zbiti.common.StringUtil;
import com.zbiti.common.TimeoutUtil;
import com.zbiti.etl.core.vo.FileDesc;

import edu.emory.mathcs.backport.java.util.Collections;

public class FtpTransferClient extends AFileTransferClient {

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	private static final Log logger = LogFactory.getLog(FtpTransferClient.class);
	private FTPClient ftpClient;

	@Override
	public void download(FileDesc desc, String toPathFileName) throws Exception {
		File toFile = new File(toPathFileName);
		// 如果不追加先删除文件
		if ("1".equals(desc.getSourceType()) && toFile.exists()) {
			toFile.delete();
		}
		if (!toFile.exists()) {
			if (!toFile.getParentFile().exists()) {
				// toFile.getParentFile().mkdirs();
				FileUtil.mkdir(toPathFileName);
			}
			toFile.createNewFile();
			FileUtil.chmodFile(toPathFileName);
		}
		long begenSize = toFile.length();
		if (logger.isInfoEnabled()) {
			logger.info("start download file [" + desc.getFileName() + "][beginSize:"+begenSize+"] from "
					+ this.getUser() + "@" + this.getServer() + "!");
		}
		
		long start = System.currentTimeMillis();
		final OutputStream out = new FileOutputStream(toFile, true);
		// 这个设置不需要
		// ftpClient.enterLocalPassiveMode(); // 告诉ftp打开一个通道传送数据
		ftpClient.setRestartOffset(begenSize);
		final String ftpFilePathName = StringUtil.ChangeCode(
				desc.getFileName(), this.getResourceEncoding(), "iso-8859-1");
		// ftpClient.retrieveFile(ftpFilePathName, out);
		long timeout=30*60;//30分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		int downTimes = 1;
		while (true) {
			try {// 如果下载异常则尝试多次下载
				TimeoutUtil.execute(new Callable<Object>() {
					public Object call() throws Exception {
						ftpClient.retrieveFile(ftpFilePathName, out);
						return null;
					}
				}, timeout);
				break;
			} catch (Exception e) {
				logger.warn("download file[" + ftpFilePathName + "] from "
						+ this.getUser() + "@" + this.getServer() + " "
						+ downTimes + " times failuer " + e.getMessage()+",wait 30s reconncect.....");
				if (downTimes > this.getMaxDownloadTimes()) {
					throw e;
				}
				
				try {
					Thread.sleep(1000*30);
					if(!ftpClient.isConnected()){
						login();
					}
				} catch (InterruptedException e1) {
					if (logger.isErrorEnabled())
						logger.error("tread sleelp InterruptedException ");
				}
				downTimes++;
				continue;
			}
		}
		out.flush();
		out.close();
		long endSize = toFile.length();
		if (begenSize != endSize)
			desc.setChange(true);
		else
			desc.setChange(false);
		desc.setFileSize(endSize);
		desc.setModifyDate(toFile.lastModified());
		long end = System.currentTimeMillis();
		if (logger.isInfoEnabled()) {
			logger.info("file["+desc.getFileName()+"] down  succuss [" + endSize
					+ "] ,used times(ms) =" + (end - start) + "毫秒");
		}

	}
	@Override
	public void download(FileDesc desc, String toPathFileName,long localFileSize) throws Exception {
		File toFile = new File(toPathFileName);
		logger.info(toPathFileName+" ,to File size =" +toFile.length()+",have do size =" +localFileSize);
		// 单文件下载就先把文件给删了
		if ("1".equals(desc.getSourceType())&& toFile.exists()) {
			toFile.delete();
		}
		if (!toFile.exists()) {
			if (!toFile.getParentFile().exists()) {
				FileUtil.mkdir(toPathFileName);
			}
			toFile.createNewFile();
			FileUtil.chmodFile(toPathFileName);
		}
		
		long begenSize =toFile.length();
		if(localFileSize>0){
			begenSize=localFileSize;
		}
		if (logger.isInfoEnabled()) {
			logger.info("start down file [" + desc.getFileName() + "] from "
					+ this.getUser() + "@" + this.getServer() + "!,local file "+toPathFileName+" size ="+ toFile.length());
		}
		
		long start = System.currentTimeMillis();
		final OutputStream out = new FileOutputStream(toFile, true);
		ftpClient.setRestartOffset(begenSize);
		final String ftpFilePathName = StringUtil.ChangeCode(
				desc.getFileName(), this.getResourceEncoding(), "iso-8859-1");
		int downTimes = 1;

		long timeout=30*60;//30分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		while (true) {
			try {// 如果下载异常则尝试多次下载
				TimeoutUtil.execute(new Callable<Object>() {
					public Object call() throws Exception {
						ftpClient.retrieveFile(ftpFilePathName, out);
						return null;
					}
				}, timeout);
				break;
			} catch (Exception e) {
				if(!ftpClient.isConnected()){
					 login();
				}
				logger.warn("download file[" + ftpFilePathName + "] from "
						+ this.getUser() + "@" + this.getServer() + " "
						+ downTimes + " times failuer " + e.getMessage());
				if (downTimes > this.getMaxDownloadTimes()) {
					throw e;
				}
				try {
					Thread.sleep(this.getWaitTime());
				} catch (InterruptedException e1) {
					if (logger.isErrorEnabled())
						logger.error("tread sleelp InterruptedException ");
				}
				downTimes++;
				continue;
			}
		}
		out.flush();
		out.close();
		long endSize = toFile.length();
		if (begenSize != endSize)
			desc.setChange(true);
		else
			desc.setChange(false);
		desc.setModifyDate(toFile.lastModified());
		long end = System.currentTimeMillis();
		if (logger.isInfoEnabled()) {
			logger.info("ftp down file  succuss [" + desc.getFileName()
					+ "] ,used times(ms) =" + (end - start) + "毫秒");
		}

	}
	@Override
	public List<FileDesc> listFile(String path, String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileDesc> listFile(String path, String pattern,
			Calendar lastMaxModifyDate) throws Exception {
		pattern = StringUtil.ChangeCode(pattern, this.getResourceEncoding(), "iso-8859-1");
		FTPFileNameFilter filter = new FTPFileNameFilter();
		filter.setPattern(pattern);
		filter.setLastMaxModifyDate(lastMaxModifyDate);
		return this.getAllChildrenDir(path, filter);
	}

	@Override
	public List<FileDesc> listPath(String path) throws Exception {
		List<FileDesc> list = new ArrayList<FileDesc>();
		getAllChildrenDir(path, list);
		return list;
	}

	@Override
	public List<FileDesc> listPath(String path, String pathPattern)
			throws Exception {
		List<FileDesc> list = new ArrayList<FileDesc>();
		getAllChildrenDir(path, pathPattern, list);
		return list;
	}

	@Override
	public void login() throws Exception {
		if (ftpClient == null || !ftpClient.isConnected()||!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (logger.isInfoEnabled())
				logger.info("FTP client " + this.getUser() + "@"
						+ this.getServer() + " init start !");
			int connTimes = 1;
			long timeout=60;//30分钟
			try{
				timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_LOGIN_TIMEOUT"));
			}catch (Exception e) {
				logger.error("获取超时时间异常，设置默认值");
			}
			while (true) {
				try {
					ftpClient = new FTPClient();
					// ftpClient.addProtocolCommandListener(listener);
					if (this.getPort() != -1) {
						ftpClient.setDefaultPort(this.getPort());
					}
					final String server = this.getServer();
					final String username = this.getUser();
					final String password = this.getPassword();
					TimeoutUtil.execute(new Callable<Object>() {
						public Object call() throws Exception {
							ftpClient.connect(server);
							ftpClient.login(username, password);
							return null;
						}
					}, timeout);

					if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
						throw new Exception("ftp server [" + this.getUser()
								+ "@" + this.getServer()
								+ "] connected failure");
					}
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
					if ("ACTIVE".equals(this.getFtpModel())) {
						ftpClient.enterLocalActiveMode();
					} else {
						ftpClient.enterLocalPassiveMode();
					}
					break;
				} catch (Exception e) {
					disconnectFtpClient();
					if (logger.isWarnEnabled())
						logger.warn("get ftp " + this.getUser() + "@"
								+ this.getServer() + " connect " + connTimes
								+ " times failuer " + e.getMessage());
					if (connTimes >= this.getMaxConnTimes()) {
						throw new Exception("try " + connTimes
								+ " times，connect [" + this.getServer()
								+ "] failure" + e.getMessage());
					}
					try {
						Thread.sleep(1000*30);
					} catch (InterruptedException e1) {
						if (logger.isErrorEnabled())
							logger.error("tread sleelp InterruptedException ");
					}
					connTimes++;
					continue;
				}
			}
			if (logger.isInfoEnabled())
				logger.info("FTP client " + this.getUser() + "@"
						+ this.getServer() + " init succuss !");
		}
	}

	@Override
	public void disconnectFtpClient() {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (Exception e1) {
				if (logger.isWarnEnabled())
					logger
							.warn(" close ftp connect failure !  "
									+ e1.getMessage());
			}
		}
	}

	/**
	 * 查询传入路劲下所有的文件目录(返回值在传入的list中)
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public void getAllChildrenDir(final String filePath, List<FileDesc> list)
			throws Exception {
		FileDesc fd = new FileDesc();
		fd.setFileName(filePath);
		fd.setServerName(this.getServer());
		list.add(fd);
		// FTPFile[] file = ftpClient.listFiles(filePath);
		if (logger.isInfoEnabled()) {
			logger.info("begin list ftp " + this.getUser() + "@"
					+ this.getServer() + " file");
			logger.info("list directory: " + filePath);
		}

		long timeout=300;//30分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_LIST_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		FTPFile[] file = (FTPFile[]) TimeoutUtil.execute(
				new Callable<Object>() {
					public Object call() throws Exception {
						return ftpClient.listFiles(filePath);
					}
				}, timeout);
		if (logger.isInfoEnabled())
			logger.info("success list directory: " + filePath);
		for (FTPFile f : file) {
			if (f.isDirectory()) {
				String filePathNew = filePath + f.getName() + "/";
				this.getAllChildrenDir(filePathNew, list);
			}
		}
	}

	/**
	 * 查询传入路劲下所有的文件目录(返回值在传入的list中) 根目录确定，子目录中存在模糊匹配。为了效率，遍历的时候层层匹配，将子目录级级拆开
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public void getAllChildrenDir(final String rootPath, String sonPath,
			List<FileDesc> list) throws Exception {
		FileDesc fd = new FileDesc();
		// 只有将子目录遍历完之后才加入list
		if (StringUtil.objectToStr(sonPath).trim().equals("/")
				|| StringUtil.objectToStr(sonPath).trim().equals("")) {
			// System.out.println(rootPath);
			fd.setFileName(rootPath);
//			fd.setServerName(this.getServer());
			list.add(fd);
			return;// 不再遍历最终目录下面的目录
		}
		if (logger.isInfoEnabled()) {
			logger.info("begin list ftp " + this.getUser() + "@"
					+ this.getServer() + " file");
			logger.info("list directory: " + rootPath);
		}
		long timeout=300;//5分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_LIST_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		FTPFile[] files = (FTPFile[]) TimeoutUtil.execute(
				new Callable<Object>() {
					public Object call() throws Exception {
						return ftpClient.listFiles(rootPath);
					}
				}, timeout);
		if (logger.isInfoEnabled())
			logger.info("success list directory: " + rootPath);
		for (FTPFile file : files) {
			String fileName = file.getName();

			if (".".equals(fileName) || "..".equals(fileName)) {
				continue;
			}

			if (file.isDirectory()) {
				// 子目录遍历完之后就不需要匹配只目录表达式
				if (StringUtil.objectToStr(sonPath).trim().equals("/")
						|| StringUtil.objectToStr(sonPath).trim().equals("")) {
					String newRootPath = rootPath + fileName + "/";
					getAllChildrenDir(newRootPath, sonPath, list);
				} else {
					String sonPathFilter = sonPath.replaceAll("\\.", "[.]");
					sonPathFilter = sonPathFilter.replaceAll("\\*", ".*");

					// sonPath.substring(sonPath.indexOf("/")+1,)
					// 以/开头
					String[] sonPathFilterArray = sonPathFilter.split("/");
					String[] sonPathArray = sonPath.split("/");
					if (sonPathFilter.indexOf("/") == 0) {
						if (sonPathArray.length >= 2) {
							if (fileName.matches(sonPathFilterArray[1])) {
								String newRootPath = rootPath + fileName + "/";
								String newSonPath = sonPath
										.substring(("/" + sonPathArray[1])
												.length());
								getAllChildrenDir(newRootPath, newSonPath, list);
							}
						}
					} else {
						if (sonPathArray.length >= 1) {
							if (fileName.matches(sonPathFilterArray[0])) {
								String newRootPath = rootPath + fileName + "/";
								String newSonPath = sonPath
										.substring(sonPathArray[0].length());
								getAllChildrenDir(newRootPath, newSonPath, list);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 查询传入路劲下所有的文件
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public List<FileDesc> getAllChildrenDir(final String filePath,
			final FTPFileFilter filter) throws Exception {
		FileDesc fd = null;
		List<FileDesc> list = new ArrayList<FileDesc>();
		if (logger.isInfoEnabled()) {
			logger.info("begin list ftp " + this.getUser() + "@"
					+ this.getServer() + " file");
			logger.info("list directory: " + filePath);
		}
		long timeout=300;//5分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_LIST_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		FTPFile[] file = (FTPFile[]) TimeoutUtil.execute(
				new Callable<Object>() {
					public Object call() throws Exception {
						return ftpClient.listFiles(filePath, filter);
					}
				}, timeout);
		if (logger.isInfoEnabled())
			logger.info("success list directory: " + filePath);
		for (FTPFile f : file) {
			if (!f.isDirectory()) {
				long fileSize = f.getSize()  ;
				if (fileSize ==0) {
					logger.info("the  file " + f.getName()
							+ " size  =0  not get ,size =" + f.getSize());
					continue;
				}
				fd = new FileDesc();
				String filepathName = StringUtil.ChangeCode(f.getName(),
						"iso-8859-1", this.getResourceEncoding());
				fd.setFileName(filePath + filepathName);
//				fd.setServerName(this.getServer());
				fd.setModifyDate(f.getTimestamp().getTimeInMillis());
				fd.setFileSize(fileSize);
				list.add(fd);
			}
		}
		Collections.sort(list,new Comparator<FileDesc>() {

			@Override
			public int compare(FileDesc o1, FileDesc o2) {
				if(o1.getModifyDate()<o2.getModifyDate())
					return 1;
				else return -1;
			}
		});
		logger.info("get the file num = " + list.size());
		return list;
		// return list;
	}


	public long upload(String fromPath, final String toPath) throws Exception {
		long begin = System.currentTimeMillis();
		final FileInputStream fis = new FileInputStream(fromPath);
		if (logger.isInfoEnabled()) {
			logger.info("begin upload file to ftp " + this.getUser() + "@"
					+ this.getServer());
			logger.info("fromPath: " + fromPath);
			logger.info("toPath: " + toPath);
		}
		int uploadTimes = 1;
		long timeout=30*60;//30分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_TIMEOUT"));
		}catch (Exception e) {
			logger.error("获取超时时间异常，设置默认值");
		}
		while (true) {
			try {// 如果上传异常则尝试多次上传
				TimeoutUtil.execute(new Callable<Object>() {
					public Object call() throws Exception {
//						logger.info("ftpClient.getReply():"+ftpClient.getReply()+";ftpClient.getReplyCode():"+ftpClient.getReplyCode()+";ftpClient.getKeepAlive():"+ftpClient.getKeepAlive());
//						logger.info(ftpClient.getReplyCode());
//						logger.info(ftpClient.isConnected());
//						logger.info(FTPReply.isPositiveCompletion(ftpClient.getReplyCode()));
						if(!ftpClient.isConnected()||!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
							login();
//						ftpClient.appendFile(toPath, fis);
						ftpClient.mkd(toPath.substring(0,toPath.lastIndexOf("/")));
						ftpClient.storeFile(toPath, fis);
						return null;
					}
				}, timeout);
				break;
			} catch (Exception e) {
				logger.warn("upload file[" + fromPath + "] to " + this.getUser()
						+ "@" + this.getServer() + "[" + toPath + "] "
						+ uploadTimes + " times failuer " + e.getMessage());
				// System.out.println("upload file["+fromPath+"] to "+this.getUser()+"@"+this.getServer()+"["+toPath+"] "
				// + uploadTimes + " times failuer " + e.getMessage());
				if (uploadTimes > this.getMaxDownloadTimes()) {
					throw e;
				}
				try {
					Thread.sleep(this.getWaitTime());
				} catch (InterruptedException e1) {
					if (logger.isErrorEnabled())
						logger.error("tread sleelp InterruptedException ");
				}
				uploadTimes++;
				continue;
			}
		}
		if (logger.isInfoEnabled())
			logger.info("upload success!");
		// System.out.println("upload success!");
		fis.close();
		return System.currentTimeMillis() - begin;
	}

	@Override
	public String rename(String fromName, String toName) throws Exception {
		if(!ftpClient.isConnected()||!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
			login();
		ftpClient.rename(fromName, toName);
		return toName;
	}
}
