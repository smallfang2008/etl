package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;

public interface SceneInsDao extends SqlMapper {

	void saveSceneIns(SceneIns sceneIns);

	void updateSceneIns(SceneIns sceneIns);

	public List<SceneIns> selectTaskListPage(Map<String, Object> param);

	public List<SceneIns> selectTaskHistoryListPage(Map<String, Object> param);
	
	SceneIns querySceneIns(@Param("taskSeries") String taskSeries);
	
	void deleteSceneIns(@Param("sceneId") String sceneId);
}
