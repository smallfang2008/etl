package com.zbiti.etl.extend.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.FileUpStep;

public interface FileUpStepDao extends SqlMapper{
	public FileUpStep getById(@Param("id")String id);

	public void save(FileUpStep fileUpStep);
	
	public void delete(@Param("stepId")String stepId);

	public void update(FileUpStep fileUpStep);
}
