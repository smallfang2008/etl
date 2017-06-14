package com.zbiti.etl.core.smo.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbiti.etl.core.dao.StepInsDao;
import com.zbiti.etl.core.smo.IStepInsService;
import com.zbiti.etl.core.vo.StepIns;

@Service
public class StepInsServiceImpl implements IStepInsService{

	@Resource
	StepInsDao stepInsDao;
	@Override
	public void saveIntoStepInsBatch(List<StepIns> stepInsList) {
		stepInsDao.saveIntoStepInsBatch(stepInsList);
	}
}
