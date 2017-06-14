package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.ConvertStep;

public interface ConvertStepDao extends SqlMapper{
	public ConvertStep getById(@Param("id")String id);

	public void save(ConvertStep convertStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(ConvertStep convertStep);
}
