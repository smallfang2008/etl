package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.IQLoadStep;

public interface IQLoadStepDao extends SqlMapper{
	public IQLoadStep getById(@Param("id")String id);

	public void save(IQLoadStep iQLoadStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(IQLoadStep iQLoadStep);
}
