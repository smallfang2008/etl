package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Step;

public interface StepDao extends SqlMapper{
	
	List<Step> listFirstStepBySceneId(@Param("sceneId") String sceneId);
	
	List<Step> listStepBySceneId(@Param("sceneId") String sceneId);
	
	List<Map<String,Object>> listStepNameBySceneId(@Param("sceneId") String sceneId);
	
	List<Map<String,Object>> getResurceList(@Param("typeId") String typeId);
	
	String getStepSeq(String seq);
	
	void saveStep(Step step);
	
	void updateStep(Step step);
	
	Step getStep(String stepId);
	
	void deleteStep(@Param("ids") String ids);
	

	List<Step> listStepByNode(@Param("nodeCode") String nodeCode);
	List<Step> listStepByCluster(@Param("cluster") String cluster);
	List<Step> listPreStep(Step step);
	List<Step> listNextSteps(Step step);
	

	List<Step> listStepByCondition(Map map);
}
