package com.zbiti.etl.extend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.SourceFileDir;

public interface SourceFileDirDao extends SqlMapper{

	public void save(SourceFileDir sourceFileDir);

	public String getSeq(@Param("id")String id);

	public void delete(@Param("stepId")String stepId);
	
	public void deleteGetDir(@Param("dirId")String dirId);

	public List<SourceFileDir> listSourceFileDirByStepId(String stepId);
	
	public void update(SourceFileDir sourceFileDir);
	
	

}
