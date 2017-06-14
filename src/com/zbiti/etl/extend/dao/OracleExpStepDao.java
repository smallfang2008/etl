package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.OracleExpStep;

public interface OracleExpStepDao extends SqlMapper{
	public OracleExpStep getOracleExpStepByStepId(@Param("stepId")String stepId);
	
	public void save(OracleExpStep oracleExpStep);
	
	public void update(OracleExpStep oracleExpStep);
	
	public void delete(@Param("stepId")String stepId);
}
