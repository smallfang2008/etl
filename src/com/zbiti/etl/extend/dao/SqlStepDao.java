package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.OracleLoadStep;
import com.zbiti.etl.extend.vo.SqlStep;

public interface SqlStepDao extends SqlMapper{
	public SqlStep getSqlStepByStepId(@Param("stepId")String stepId);
	
	public void save(SqlStep sqlStep);
	
	public void update(SqlStep sqlStep);
	
	public void delete(@Param("stepId")String stepId);
}
