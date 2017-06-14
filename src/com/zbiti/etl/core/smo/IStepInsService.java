package com.zbiti.etl.core.smo;

import java.util.List;

import com.zbiti.etl.core.vo.StepIns;

public interface IStepInsService {


	/**
	 * 批量入库步骤实例
	 * @param cmdList
	 */
	void saveIntoStepInsBatch(List<StepIns> stepInsList);
}
