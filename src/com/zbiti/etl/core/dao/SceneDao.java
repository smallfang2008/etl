package com.zbiti.etl.core.dao;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Scene;

public interface SceneDao extends SqlMapper {

	Scene getSceneById(@Param("sceneId") String sceneId);

	void update(Scene Scene);

	void save(Scene Scene);

	public void delete(@Param("sceneId") String sceneId);
	
	String getSceneSeq();
}
