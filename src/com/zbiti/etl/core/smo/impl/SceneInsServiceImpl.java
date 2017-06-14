package com.zbiti.etl.core.smo.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbiti.etl.core.dao.SceneInsDao;
import com.zbiti.etl.core.smo.ISceneInsService;
import com.zbiti.etl.core.vo.SceneIns;

@Service
public class SceneInsServiceImpl implements ISceneInsService{

	@Resource
	SceneInsDao sceneInsDao;
	
	@Override
	public void saveSceneIns(SceneIns sceneIns){
		sceneInsDao.saveSceneIns(sceneIns);
	}

	@Override
	public void updateSceneIns(SceneIns sceneIns) {
		sceneInsDao.updateSceneIns(sceneIns);
	}
	
}
