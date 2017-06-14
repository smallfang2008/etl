package com.zbiti.etl.extend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.extend.vo.SourceFile;

public interface SourceFileDao extends SqlMapper{
	public SourceFile getSourceFileByDirIdAndDir(@Param("sourceFileDirId")String sourceFileDirId,@Param("sourceFileDir")String sourceFileDir);
	public List<SourceFile> listSourceFileByDirId(@Param("sourceFileDirId")String sourceFileDirId);
	public void saveSourceFile(SourceFile sourceFile);
	public void updateSourceFile(SourceFile sourceFile);
	
	public void deleteGetStep(@Param("stepId")String stepId);
	
	public void deleteGetDir(@Param("dirId")String dirId);
	
}
