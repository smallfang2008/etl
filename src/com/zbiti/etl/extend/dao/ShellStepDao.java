package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.ShellStep;

public interface ShellStepDao extends SqlMapper{
	public ShellStep getShellStepByStepId(@Param("stepId")String stepId);
	
	public void save(ShellStep shellStep);
	
	public void update(ShellStep shellStep);
	
	public void delete(@Param("stepId")String stepId);
}
