package com.zbiti.etl.core.smo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Security;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.sun.crypto.provider.SunJCE;
import com.zbiti.common.ConfigUtil;
import com.zbiti.common.StringUtil;
import com.zbiti.common.TimeUtil;
import com.zbiti.common.TimeoutUtil;
import com.zbiti.etl.core.vo.FileDesc;

public class SftpTranferClient extends AFileTransferClient {

	private static final Log log = LogFactory.getLog(FtpTransferClient.class);
	private ChannelSftp channelsftp;

	// private FTPClient ftpClient;
	@Override
	public void download(FileDesc desc, String toPathFileName) throws Exception {
		File toFile = new File(toPathFileName);
		// 单文件下载就先把文件给删了
		if ("1".equals(desc.getSourceType()) && toFile.exists()) {
			toFile.delete();
		}
		if (!toFile.exists()) {
			if (!toFile.getParentFile().exists()) {
				// toFile.getParentFile().mkdirs();
//				FileUtil.mkdir(toPathFileName);
				toFile.getParentFile().mkdirs();
			}
			toFile.createNewFile();
//			FileUtil.chmodFile(toPathFileName);
		}
		long begenSize = toFile.length();
		if (log.isInfoEnabled()) {
			log.info("开始下载文件[" + desc.getFileName() + "]!");
		}
		long start = System.currentTimeMillis();
		OutputStream out = new FileOutputStream(toFile, true);
		// 这个设置不需要
		// ftpClient.enterLocalPassiveMode(); // 告诉ftp打开一个通道传送数据
		// channelsftp.setRestartOffset(begenSize);
		String ftpFilePathName = StringUtil.ChangeCode(desc.getFileName(),
				this.getResourceEncoding(), "iso-8859-1");
		channelsftp.get(ftpFilePathName, out, null, ChannelSftp.RESUME, begenSize);
		// channelsftp.retrieveFile(ftpFilePathName, out);		
		long endSize = toFile.length();
		if (begenSize != endSize)
			desc.setChange(true);
		else
			desc.setChange(false);
		desc.setModifyDate(toFile.lastModified());

		long end = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("结束下载文件[" + desc.getFileName() + "] ,耗时" + (end - start)
					/ 1000 + "秒,+ toFile_size= "+ endSize);
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
		// FTPFileNameFilter filter = new FTPFileNameFilter();
		// filter.setPattern(pattern);
		// filter.setLastMaxModifyDate(lastMaxModifyDate);
		return this.getAllChildrenDir(path, pattern, lastMaxModifyDate);
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

	/**
	 * 
	 * 获取连接
	 * 
	 * @return channel
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	public ChannelSftp connectSFTP(String host, int port, String username,
			String password) throws JSchException, InterruptedException {
		JSch jsch = null;
		//ludl add 
		Security.addProvider(new SunJCE());
		//Security.addProvider(new com.sun.crypto.provider.DHKeyPairGenerator());
		Channel channel = null;
		jsch = new JSch();
		channel = null;
		Session session = jsch.getSession(username, host, port);
		if (password != null && !"".equals(password)) {
			session.setPassword(password);
		}
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");// do not verify
		// host key
		session.setConfig(sshConfig);
		session.setServerAliveInterval(92000);
		session.connect();
		channel = session.openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
	}

	/*	*//**
	 * 获取连接
	 * 
	 * @return channel
	 * @throws Exception
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	/*
	 * public void connectFTP(String host, int port, String username, String
	 * password) throws Exception { ftpClient = new FTPClient(); //
	 * ftpClient.addProtocolCommandListener(listener); if (this.getPort() != -1)
	 * { ftpClient.setDefaultPort(this.getPort()); }
	 * ftpClient.connect(this.getServer()); ftpClient.login(this.getUser(),
	 * this.getPassword()); if (!FTPReply
	 * .isPositiveCompletion(ftpClient.getReplyCode())) { throw new
	 * Exception("服务器[" + this.getServer() + "]拒绝连接 ！"); }
	 * ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	 * ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); if
	 * ("ACTIVE".equals(this.getFtpModel())) { ftpClient.enterLocalActiveMode();
	 * } else { ftpClient.enterLocalPassiveMode(); } }
	 */
	@Override
	public void login() throws Exception {
		if (channelsftp == null || !channelsftp.isConnected()) {
			if (log.isInfoEnabled())
				log.info("SFTP客户端初始化开始！");
			int connTimes = 1;
			while (true) {
				try {
					channelsftp = connectSFTP(this.getServer(), this.getPort(),
							this.getUser(), this.getPassword());
					break;
				} catch (Exception e) {
					disconnectFtpClient();
					if (log.isWarnEnabled())
						log.warn("第" + connTimes + "次获取[" + this.getServer()
								+ "]连接失败！" + e.getMessage());
					if (connTimes >= this.getMaxConnTimes()) {
						throw new Exception("尝试" + connTimes + "次，获取["
								+ this.getServer() + "]连接失败！" + e.getMessage());
					}
					try {
						Thread.sleep(this.getWaitTime());
					} catch (InterruptedException e1) {
						if (log.isErrorEnabled())
							log.error("线程休眠失败！");
					}
					connTimes++;
					continue;
				}
			}
			if (log.isInfoEnabled())
				log.info("FTP客户端初始化结束！");
		}
	}

	@Override
	public void disconnectFtpClient() {
		if (channelsftp != null && channelsftp.isConnected()) {
			try {
				channelsftp.getSession().disconnect();
				channelsftp.disconnect();
			} catch (Exception e1) {
				if (log.isWarnEnabled())
					log.warn("关闭SFTP连接失败！" + e1.getMessage());
			}
		}
	}

	/*
	 * @Override public void disconnectFtpClient() { if (ftpClient != null &&
	 * ftpClient.isConnected()) { try { ftpClient.disconnect(); } catch
	 * (Exception e1) { if (log.isWarnEnabled()) log.warn("关闭FTP连接失败！" +
	 * e1.getMessage()); } } }
	 */

	/**
	 * 查询传入路劲下所有的文件目录(返回值在传入的list中)
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public void getAllChildrenDir(String filePath, List<FileDesc> list)
			throws Exception {
		FileDesc fd = new FileDesc();
		fd.setFileName(filePath);
		fd.setServerName(this.getServer());
		list.add(fd);
		// FTPFile[] file = ftpClient.listFiles(filePath);
		Vector vector = channelsftp.ls(filePath);
		for (int i = 0; i < vector.size(); i++) {
			// 如果是目录则继续向下循环处理
			LsEntry lsEntry = (LsEntry) vector.get(i);
			String fileName=lsEntry.getFilename();
			if(".".equals(fileName) || "..".equals(fileName)){
				continue;
			}
			SftpATTRS t = lsEntry.getAttrs();
			boolean isdir = t.isDir();// 判断是否是文件夹
			if (isdir) {
				String filePathNew = filePath + lsEntry.getFilename() + "/";
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
	public void getAllChildrenDir(String rootPath, String sonPath,
			List<FileDesc> list) throws Exception {
//		System.out.println("rootPath= " + rootPath);
//		System.out.println("sonPath= " + sonPath);
		FileDesc fd = new FileDesc();
		// 只有将子目录遍历完之后才加入list
		if (StringUtil.objectToStr(sonPath).trim().equals("/")
				|| StringUtil.objectToStr(sonPath).trim().equals("")) {
			fd.setFileName(rootPath);
			fd.setServerName(this.getServer());
			list.add(fd);
		}
		// FTPFile[] files=ftpClient.listFiles(rootPath);
		Vector vector = channelsftp.ls(rootPath);
		for (int i = 0; i < vector.size(); i++) {
			LsEntry lsEntry = (LsEntry) vector.get(i);
			String fileName = lsEntry.getFilename();
			if (".".equals(fileName) || "..".equals(fileName)) {
				continue;
			}
//			System.out.println("fileName= " + fileName);
			SftpATTRS t = lsEntry.getAttrs();
			boolean isdir = t.isDir();// 判断是否是文件夹
			if (isdir) {
				// 子目录遍历完之后就不需要匹配只目录表达式

				if (StringUtil.objectToStr(sonPath).trim().equals("/")
						|| StringUtil.objectToStr(sonPath).trim().equals("")) {
					String newRootPath = rootPath + fileName + "/";
					getAllChildrenDir(newRootPath, sonPath, list);
				} else {
					String[] sonPathFromArray=sonPath.split("/");
					String sonPathFilter = sonPath.replaceAll("\\.", "[.]");
					sonPathFilter = sonPathFilter.replaceAll("\\*", ".*");
					String[] sonPathArray = sonPathFilter.split("/");
					if (sonPathFilter.indexOf("/") == 0) {
						if (sonPathArray.length >= 2) {
							if (fileName.matches(sonPathArray[1])) {
								String newRootPath = rootPath + fileName + "/";
								String newSonPath = sonPath
										.substring(("/" + sonPathFromArray[1])
												.length());
								getAllChildrenDir(newRootPath, newSonPath, list);
							}
						}
					} else {
						if (sonPathArray.length == 1) {
							if (fileName.matches(sonPathArray[0])) {
								String newRootPath = rootPath + fileName + "/";
								/*
								 * String newSonPath = sonPath .substring(("/" +
								 * sonPathArray[0]) .length());
								 */
								String newSonPath = "/";
								getAllChildrenDir(newRootPath, newSonPath, list);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 查询传入路劲下所有的文件目录(返回值在传入的list中) 加入文件夹过滤
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public void getAllChildrenDirBak(String filePath, String filePathPattern,
			List<FileDesc> list) throws Exception {
		FileDesc fd = new FileDesc();
		fd.setFileName(filePath);
		fd.setServerName(this.getServer());
		String tempPattern = filePathPattern;
		if (!StringUtil.objectToStr(filePathPattern).equals("")) {
			filePathPattern = filePathPattern.replaceAll("\\.", "[.]");
			filePathPattern = filePathPattern.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(filePathPattern);
			Matcher matcher = pattern.matcher(filePath);
			if (matcher.find()) {
				list.add(fd);
			}
		}
		// FTPFile[] file = ftpClient.listFiles(filePath);
		// for (FTPFile f : file) {
		// if (f.isDirectory()) {
		Vector vector = channelsftp.ls(filePath);
		for (int i = 0; i < vector.size(); i++) {
			LsEntry lsEntry = (LsEntry) vector.get(i);
			String fileName = lsEntry.getFilename();
			SftpATTRS t = lsEntry.getAttrs();
			boolean isdir = t.isDir();// 判断是否是文件夹
			if (isdir) {
				String filePathNew = filePath + fileName + "/";
				this.getAllChildrenDir(filePathNew, tempPattern, list);
			}
		}
	}

	/**
	 * 查询传入路劲下所有的文件 文件过滤
	 * 
	 * @param filePath
	 * @param list
	 * @throws Exception
	 */
	public List<FileDesc> getAllChildrenDir(String filePath,
			String filePattern, Calendar lastMaxModifyDate) throws Exception {
		FileDesc fd = null;
		Pattern pattern = this.getPattern(filePattern);

		List<FileDesc> list = new ArrayList<FileDesc>();
		Vector vector = channelsftp.ls(filePath);
		for (int i = 0; i < vector.size(); i++) {
			LsEntry lsEntry = (LsEntry) vector.get(i);
			String fileName = lsEntry.getFilename();
			SftpATTRS tstr = lsEntry.getAttrs();
			Calendar fileModifyDate = this.getDate(tstr.getMTime());
			boolean isdir = tstr.isDir();// 判断是否是文件夹
			if (!isdir) {
				fd = new FileDesc();
				// iso-8859-1 to GBK ludl edit
				String newFileName = StringUtil.ChangeCode(fileName,
						"iso-8859-1", "GBK");
				boolean isfind = this.fileFilter(newFileName,
						lastMaxModifyDate, fileModifyDate, pattern);
				if (isfind) {
					fd.setFileName(filePath + newFileName);
					fd.setServerName(this.getServer());
					fd.setModifyDate(fileModifyDate.getTimeInMillis());
					fd.setFileSize(tstr.getSize());//加入文件大小
					list.add(fd);
				}
			}
		}
		// message sizes
		this.log.info("get the file num = " + list.size());
		return list;
	}

	/**
	 * 组装过滤正则表达式
	 * 
	 * @param filePattern
	 * @return
	 */
	private Pattern getPattern(String filePattern) {
		Pattern pattern = null;
		if (!StringUtil.objectToStr(filePattern).equals("")) {
			filePattern = filePattern.replaceAll("\\.", "[.]");
			filePattern = filePattern.replaceAll("\\*", ".*");
			pattern = Pattern.compile(filePattern);
			return pattern;
		} else {
			return null;
		}

	}

	/**
	 * 根据文件的时间，返回日期类型的数据
	 * 
	 * @param mTime
	 * @return
	 * @throws ParseException
	 */
	private Calendar getDate(int mTime) throws ParseException {
		// SimpleDateFormat dateformat= new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// long dtime=mTime*1000;
		// System.out.println("dtime =" +dtime);
		// String dateStr=dateformat.format(dtime);
		// Date date = dateformat.parse(dateStr);
		Calendar fileEditTime = Calendar.getInstance();
		long dtime = Long.parseLong(mTime + "000");
		fileEditTime.setTimeInMillis(dtime);
		return fileEditTime;
	}

	/**
	 * 根据文件名称和文件最后的修改时间进行过滤
	 * 
	 * @param fileName
	 * @param lastMaxModifyDate
	 * @param pattern
	 * @return
	 */
	public boolean fileFilter(String fileName, Calendar lastMaxModifyDate,
			Calendar fileModifyTime, Pattern pattern) {
		// 如果日期不合适，则返回1401206400000
		if (lastMaxModifyDate != null
				&& lastMaxModifyDate.compareTo(fileModifyTime) > 0) {
			return false;
		}
		// 日期符合，然后判断是否
		if (pattern != null) {
			Matcher matcher = pattern.matcher(fileName);
			return matcher.find();
		} else {
			return true;
		}
	}

	@Override
	public long upload(String fromPath,final String toPath) throws Exception {
		long begin = System.currentTimeMillis();
		final FileInputStream fis = new FileInputStream(fromPath);
		if (log.isInfoEnabled()) {
			log.info("begin upload file to sftp " + this.getUser() + "@"
					+ this.getServer());
			log.info("fromPath: " + fromPath);
			log.info("toPath: " + toPath);
		}
		// System.out.println("begin upload file to ftp "+this.getUser()+"@"+this.getServer());
		log.info("fromPath: "+fromPath);
		log.info("toPath: "+toPath);
		int uploadTimes = 1;
		long timeout=30*60;//30分钟
		try{
			timeout=Long.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_TIMEOUT"));
		}catch (Exception e) {
			log.error("获取超时时间异常，设置默认值");
		}
		while (true) {
			try {// 如果上传异常则尝试多次上传
				TimeoutUtil.execute(new Callable<Object>() {
					public Object call() throws Exception {
						//ludl edit 断点续传
						//channelsftp.put(fis,toPath );
						channelsftp.put(fis,toPath,ChannelSftp.RESUME);
						return null;
					}
				}, timeout);
				break;
			} catch (Exception e) {
				log.warn("upload file[" + fromPath + "] to " + this.getUser()
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
					if (log.isErrorEnabled())
						log.error("tread sleelp InterruptedException ");
				}
				uploadTimes++;
				continue;
			}
		}
		if (log.isInfoEnabled())
			log.info("upload success!");
		// System.out.println("upload success!");
		fis.close();
		return System.currentTimeMillis() - begin;
	}

	@Override
	public void download(FileDesc desc, String toPathFileName, long localFileSize)
			throws Exception {
		File toFile = new File(toPathFileName);
		// 单文件下载就先把文件给删了
		if ("1".equals(desc.getSourceType())&& toFile.exists()) {
			toFile.delete();
		}
		if (!toFile.exists()) {
			if (!toFile.getParentFile().exists()) {
				// toFile.getParentFile().mkdirs();
//				FileUtil.mkdir(toPathFileName);
				toFile.getParentFile().mkdirs();
			}
			toFile.createNewFile();
//			FileUtil.chmodFile(toPathFileName);
		}
		long begenSize = toFile.length();
		if(localFileSize>0){
			begenSize=localFileSize;
		}
		if (log.isInfoEnabled()) {
			log.info("开始下载文件[" + desc.getFileName() + "]!");
		}
		long start = System.currentTimeMillis();
		OutputStream out = new FileOutputStream(toFile, true);
		// 这个设置不需要
		// ftpClient.enterLocalPassiveMode(); // 告诉ftp打开一个通道传送数据
		// channelsftp.setRestartOffset(begenSize);
		String ftpFilePathName = StringUtil.ChangeCode(desc.getFileName(),
				"GBK", "iso-8859-1");
		channelsftp.get(ftpFilePathName, out, null, ChannelSftp.RESUME, begenSize);
		// channelsftp.retrieveFile(ftpFilePathName, out);
		long endSize = toFile.length();
		if (begenSize != endSize)
			desc.setChange(true);
		else
			desc.setChange(false);
		desc.setModifyDate(toFile.lastModified());
		long end = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("结束下载文件[" + desc.getFileName() + "] ,耗时" + (end - start)
					/ 1000 + "秒");
		}
	}

	@Override
	public String rename(String fromName, String toName) throws Exception {
		channelsftp.rename(fromName, toName);
		return toName;
	}

//	public static void main(String[] args) {
//		String pattern="aaa*.dat";
//		Pattern pattern1=new SftpTranferClient().getPattern(pattern);
//		Matcher matcher = pattern1.matcher("aaa1234.dat.bak");	
//		System.out.println(matcher.find());
//	}
	
}
