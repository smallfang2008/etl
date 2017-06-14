
package com.zbiti.etl.core.smo.impl;

import java.util.Calendar;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class FTPFileNameFilter implements FTPFileFilter{

	private String pattern;
	private Calendar lastMaxModifyDate; 
//	private long lastMaxFileSize;
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		pattern = pattern.replaceAll("\\.", "[.]");
		pattern = pattern.replaceAll("\\*", ".*");
		this.pattern = pattern;
	}

	public Calendar getLastMaxModifyDate() {
		return lastMaxModifyDate;
	}

	public void setLastMaxModifyDate(Calendar lastMaxModifyDate) {
		this.lastMaxModifyDate = lastMaxModifyDate;
	}

//	public long getLastMaxFileSize() {
//		return lastMaxFileSize;
//	}
//
//	public void setLastMaxFileSize(long lastMaxFileSize) {
//		this.lastMaxFileSize = lastMaxFileSize;
//	}

	@Override
	public boolean accept(FTPFile ftpFile) {
		if (ftpFile.getType() == FTPFile.DIRECTORY_TYPE) {// 文件夹
			return false;
		}
		if (lastMaxModifyDate != null && lastMaxModifyDate.compareTo(ftpFile.getTimestamp()) > 0) {
			return false;
		}
//		if(lastMaxModifyDate != null && lastMaxModifyDate.compareTo(ftpFile.getTimestamp()) ==0&&lastMaxFileSize==ftpFile.getSize()){
//			return false;
//		}
		if (pattern != null && !"".equals(pattern.trim())) {
			return ftpFile.getName().matches(pattern);
		} else {
			return true;
		}
	}

//	public static void main(String[] args) {
//		String pattern="aaa*.dat";
//
//		pattern = pattern.replaceAll("\\.", "[.]");
//		pattern = pattern.replaceAll("\\*", ".*");
//		System.out.println("aaa1234.dat.bak".matches(pattern));
//	}
	
}
