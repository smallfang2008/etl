package com.zbiti.etl.core.smo;

import java.util.List;
import java.util.Map;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepIns;

/**
 * @author Administrator
 * 
 */
public interface ITaskService {

	/**
	 * 任务列表查询
	 * 
	 * @param param
	 * @param page
	 * @return
	 */
	public PageQueryResult<SceneIns> selectTaksListPage(
			Map<String, Object> param, Page page);

	/**
	 * 根据任务ID，查询任务信息
	 * 
	 * @param sceneId
	 * @return
	 */
	public Scene getTaskInfo(String sceneId);

	/**
	 * 编辑任务
	 * 
	 * @param scene
	 */
	public void update(Scene scene);

	/**
	 * 增加任务
	 * 
	 * @param scene
	 */

	public void save(Scene scene);

	/**
	 * 删除任务
	 * 
	 * @param sceneId
	 */
	public String deleteTask(String sceneId);

	/**
	 * 任务历史查询
	 */
	public PageQueryResult<SceneIns> selectTaskHistoryListPage(
			Map<String, Object> param, Page page);

	/**
	 * 步骤历史查询
	 */
	public PageQueryResult<StepIns> selectStepHistoryListPage(
			Map<String, Object> param, Page page);
	
	List<Step> listStepByCondition(Map map);
	public PageQueryResult<StepIns> selectStepInsHisListPage(
			Map<String, Object> param, Page page);
	public void updateStepIns(StepIns stepIns);
	public void deleteStepIns(String stepSeries);
	
	public String getSceneSeq();
	
	public SceneIns querySceneIns(String taskSeries);
}
