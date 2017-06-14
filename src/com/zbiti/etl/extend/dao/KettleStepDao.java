package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.KettleStep;

public interface KettleStepDao extends SqlMapper{
	public KettleStep getById(@Param("id")String id);

	public void save(KettleStep kettleStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(KettleStep kettleStep);
	
	public KettleStep getKettleById(@Param("stepId")String stepId);
}
