package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.StepIns;

public interface StepInsDao extends SqlMapper {

	public void saveIntoStepInsBatch(List<StepIns> stepInsList);
	public void updateStepIns(StepIns stepIns);
	public void deleteStepIns(String stepSeries);
	public List<StepIns> selectStepHistoryListPage(Map<String, Object> param);
	List<StepIns> selectStepInsHisListPage(Map<String, Object> param);
	
	List<StepIns> getStepSeries(@Param("sceneId") String sceneId);
	
	void batchDeleteStepIns(@Param("stepSeries") String stepSeries);
}
