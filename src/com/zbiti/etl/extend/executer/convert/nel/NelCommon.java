package com.zbiti.etl.extend.executer.convert.nel;

import com.zbiti.common.ConfigUtil;


public class NelCommon {
	public static String getNqRootPath() {
		return ConfigUtil.getValueByKey("NQ_ROOT_PATH");
	}

	public static String getRootPath() {
		return ConfigUtil.getValueByKey("NEL_ROOT_PATH");
	}

	public static String getHanaRootPath() {
		return ConfigUtil.getValueByKey("HANA_ROOT_PATH");
	}

	public static String getNeRootPath() {
		return ConfigUtil.getValueByKey("NE_ROOT_PATH");
	}

	public static int getQueueNumCvn() {
		int queueNumCvn = 10;
		if (ConfigUtil.getValueByKey("QUEUE_NUM_CVN") != null
				&& !"".equals(ConfigUtil.getValueByKey("QUEUE_NUM_CVN"))) {
			queueNumCvn = Integer.parseInt(ConfigUtil.getValueByKey("QUEUE_NUM_CVN"));
		}
		return queueNumCvn;
	}

	public static int getQueueNumSplit() {
		int queueNumSplit = 1000;
		if (ConfigUtil.getValueByKey("QUEUE_NUM_SPLIT") != null
				&& !"".equals(ConfigUtil.getValueByKey("QUEUE_NUM_SPLIT"))) {
			queueNumSplit = Integer.parseInt(
					ConfigUtil.getValueByKey("QUEUE_NUM_SPLIT"));
		}
		return queueNumSplit;
	}

	public static int getQueueNumUpload() {
		int queueNumUpload = 10;
		if (ConfigUtil.getValueByKey("QUEUE_NUM_UPLOAD") != null
				&& !"".equals(ConfigUtil.getValueByKey("QUEUE_NUM_UPLOAD"))) {
			queueNumUpload = Integer.parseInt(
					ConfigUtil.getValueByKey("QUEUE_NUM_UPLOAD"));
		}
		return queueNumUpload;
	}

	public static long getJobSyncWaittime() {
		long waitTime = 60;
		if (ConfigUtil.getValueByKey("JOB_SYNC_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("JOB_SYNC_WAITTIME"))) {
			waitTime = Integer.parseInt(ConfigUtil.getValueByKey("JOB_SYNC_WAITTIME"));
		}
		return waitTime;
	}

	public static int getMemoryMonitorWaittime() {
		int waitTime = 30;
		if (ConfigUtil.getValueByKey("MEMORY_MONITOR_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("MEMORY_MONITOR_WAITTIME"))) {
			waitTime = Integer.parseInt(
					ConfigUtil.getValueByKey("MEMORY_MONITOR_WAITTIME"));
		}
		return waitTime;
	}

	public static long getFtpDownloadWaitTime() {
		long waitTime = 600;
		if (ConfigUtil.getValueByKey("FTP_DOWNLOAD_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("FTP_DOWNLOAD_WAITTIME"))) {
			waitTime = Long
					.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_WAITTIME"));
		}
		return waitTime;
	}

	public static long getFtpDownloadFilelistWaitTime() {
		long waitTime = 300;
		if (ConfigUtil.getValueByKey("FTP_DOWNLOAD_FILELIST_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("FTP_DOWNLOAD_FILELIST_WAITTIME"))) {
			waitTime = Long
					.parseLong(ConfigUtil.getValueByKey("FTP_DOWNLOAD_FILELIST_WAITTIME"));
		}
		return waitTime;
	}

	public static int getFtpDownloadConnTimes() {
		int ConnTimes = 1;
		if (ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_TIMES") != null
				&& !"".equals(ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_TIMES"))) {
			ConnTimes = Integer.parseInt(
					ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_TIMES"));
		}
		return ConnTimes;
	}

	public static long getFtpDownloadConnWaittime() {
		long connWaitTime = 5;
		if (ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_WAITTIME"))) {
			connWaitTime = Long.parseLong(
					ConfigUtil.getValueByKey("FTP_DOWNLOAD_CONN_WAITTIME"));
		}
		return connWaitTime;
	}

	public static long getConvertWaittime() {
		long waitTime = 1200;
		if (ConfigUtil.getValueByKey("CVN_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("CVN_WAITTIME"))) {
			waitTime = Long.parseLong(ConfigUtil.getValueByKey("CVN_WAITTIME"));
		}
		return waitTime;
	}

	public static long getDBUploadWaittime() {
		long waitTime = 1200;
		if (ConfigUtil.getValueByKey("DB_UPLOAD_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("DB_UPLOAD_WAITTIME"))) {
			waitTime = Long.parseLong(ConfigUtil.getValueByKey("DB_UPLOAD_WAITTIME"));
		}
		return waitTime;
	}

	public static int getDBUploadConnTimes() {
		int ConnTimes = 3;
		if (ConfigUtil.getValueByKey("DB_UPLOAD_CONN_TIMES") != null
				&& !"".equals(ConfigUtil.getValueByKey("DB_UPLOAD_CONN_TIMES"))) {
			ConnTimes = Integer.parseInt(
					ConfigUtil.getValueByKey("DB_UPLOAD_CONN_TIMES"));
		}
		return ConnTimes;
	}

	public static long getDBUploadConnWaittime() {
		long connWaitTime = 5;
		if (ConfigUtil.getValueByKey("DB_UPLOAD_CONN_WAITTIME") != null
				&& !"".equals(ConfigUtil.getValueByKey("DB_UPLOAD_CONN_WAITTIME"))) {
			connWaitTime = Long.parseLong(
					ConfigUtil.getValueByKey("DB_UPLOAD_CONN_WAITTIME"));
		}
		return connWaitTime;
	}

}
