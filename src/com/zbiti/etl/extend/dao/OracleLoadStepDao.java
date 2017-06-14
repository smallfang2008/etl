package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.OracleLoadStep;

public interface OracleLoadStepDao extends SqlMapper{
	public OracleLoadStep getById(@Param("id")String id);

	public void save(OracleLoadStep oracleLoadStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(OracleLoadStep oracleLoadStep);
}
