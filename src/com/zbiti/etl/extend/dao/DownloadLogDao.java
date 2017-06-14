package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.DownloadLog;

public interface DownloadLogDao extends SqlMapper{

	public void saveDownloadLog(DownloadLog downloadLog);
	
	void deleteDownloadLog(@Param("stepSeries") String stepSeries);
}
