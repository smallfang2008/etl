package com.zbiti.etl.core.smo.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbiti.common.memory.DataToMemory;
import com.zbiti.etl.core.dao.SceneDao;
import com.zbiti.etl.core.smo.ISceneService;
import com.zbiti.etl.core.vo.Scene;

@Service
public class SceneServiceImpl implements ISceneService{

	@Resource
	SceneDao sceneDao;
	@Override
	public Scene getSceneById(String sceneId) {
		Object o = DataToMemory.readData("Scene", sceneId);
		if (o == null) {

			Scene scene= sceneDao.getSceneById(sceneId);
			DataToMemory.write("Scene", sceneId, scene); 
			return scene;
		}
		return (Scene)o;
	}

}
