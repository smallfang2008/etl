package com.zbiti.etl.core.smo.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.zbiti.common.dao.DaoUtil;
import com.zbiti.common.memory.DataToMemory;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.dao.SceneDao;
import com.zbiti.etl.core.dao.SceneInsDao;
import com.zbiti.etl.core.dao.StepDao;
import com.zbiti.etl.core.dao.StepInsDao;
import com.zbiti.etl.core.smo.ITaskService;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.SceneIns;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepIns;
import com.zbiti.etl.extend.dao.DownloadLogDao;

/**
 * @author Administrator
 * 
 */
@Service("TaskServiceImpl")
public class TaskServiceImpl implements ITaskService {

	@javax.annotation.Resource
	private SceneDao sceneDao;

	@javax.annotation.Resource
	private SceneInsDao sceneInsDao;

	@javax.annotation.Resource
	private StepInsDao stepInsDao;

	@javax.annotation.Resource
	private StepDao stepDao;
	
	@javax.annotation.Resource
	private DownloadLogDao downloadLogDao;

	@Override
	public PageQueryResult<SceneIns> selectTaksListPage(
			Map<String, Object> param, Page page) {
		return new PageQueryResult<SceneIns>(
				sceneInsDao.selectTaskListPage(DaoUtil.toMap(param, page)),
				page.getTotalResult());
	}

	@Override
	public Scene getTaskInfo(String sceneId) {
		return sceneDao.getSceneById(sceneId);
	}

	@Override
	public void update(Scene scene) {
		sceneDao.update(scene);
	}

	@Override
	public void save(Scene scene) {
		sceneDao.save(scene);
		DataToMemory.write("Scene", scene.getSceneId(), scene);
	}

	@Override
	public PageQueryResult<SceneIns> selectTaskHistoryListPage(
			Map<String, Object> param, Page page) {
		return new PageQueryResult<SceneIns>(
				sceneInsDao.selectTaskHistoryListPage(DaoUtil
						.toMap(param, page)), page.getTotalResult());
	}

	@Override
	public PageQueryResult<StepIns> selectStepHistoryListPage(
			Map<String, Object> param, Page page) {
		return new PageQueryResult<StepIns>(
				stepInsDao
						.selectStepHistoryListPage(DaoUtil.toMap(param, page)),
				page.getTotalResult());
	}

	@Override
	public String deleteTask(String sceneId) {
		try {
			// 如果任务是已启动状态，需先停止任务
			if("4".equals((sceneDao.getSceneById(sceneId)).getStartStatus())){
				return "-2";
			}
			
			if (CollectionUtils.isNotEmpty(stepDao.listStepBySceneId(sceneId))) {
				// 表示任务下面有步骤，不能删除
				return "-1";
			}
			
			String stepSeries = "";
			List<StepIns> list = stepInsDao.getStepSeries(sceneId);
			for(StepIns stepIns : list){
				stepSeries = stepSeries + "'" + stepIns.getStepSeries() + "'" + ",";
			}
			if(!"".equals(stepSeries)){
			    stepSeries = stepSeries.substring(0, stepSeries.length()-1);
			}else{
				stepSeries = "'" + "'"; 
			}
			
			sceneDao.delete(sceneId);
			sceneInsDao.deleteSceneIns(sceneId);// 删除任务实例表
			downloadLogDao.deleteDownloadLog(stepSeries); //删除文件下载日志表
			stepInsDao.batchDeleteStepIns(stepSeries); //删除步骤历史表
			
			DataToMemory.delete("Scene", sceneId);
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}

	@Override
	public List<Step> listStepByCondition(Map map) {
		return stepDao.listStepByCondition(map);
	}

	@Override
	public PageQueryResult<StepIns> selectStepInsHisListPage(
			Map<String, Object> param, Page page) {
		return new PageQueryResult<StepIns>(stepInsDao.selectStepInsHisListPage(DaoUtil.toMap(param, page)),page.getTotalResult());
	}
	
	@Override
	public void updateStepIns(StepIns stepIns){
		stepInsDao.updateStepIns(stepIns);
	}
	
	@Override
	public void deleteStepIns(String stepSeries){
		stepInsDao.deleteStepIns(stepSeries);
	}

	@Override
	public String getSceneSeq() {
		return sceneDao.getSceneSeq();
	}

	@Override
	public SceneIns querySceneIns(String taskSeries) {
		return sceneInsDao.querySceneIns(taskSeries);
	}
}
