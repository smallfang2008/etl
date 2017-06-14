package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.ConvertRecord;

public interface ConvertRecordDao extends SqlMapper{

	/**
	 * 获取转换记录。fileName包含文件路径
	 */
	ConvertRecord getConvertRecord(@Param("stepId") String stepId,@Param("fileName") String fileName);
	void  saveConvertRecord(ConvertRecord convertRecord);
	void  updateConvertRecord(ConvertRecord convertRecord);
//	void deleteConvertRecord(@Param("stepId") String stepId,@Param("sourceFileDir") String sourceFileDir);
	void deleteConvertRecord(@Param("sourceFileDirId") String sourceFileDirId);
}
