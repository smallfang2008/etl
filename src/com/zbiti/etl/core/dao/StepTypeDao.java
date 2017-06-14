package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.core.vo.StepType;

public interface StepTypeDao extends SqlMapper{

	List<StepType> findstepType();
}
