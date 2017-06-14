package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.FinderStep;

public interface FinderStepDao extends SqlMapper{
	public FinderStep getFinderStepByStepId(@Param("stepId")String stepId);
	
	public void save(FinderStep finderStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(FinderStep finderStep);
}
