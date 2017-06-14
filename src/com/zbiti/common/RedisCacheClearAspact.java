package com.zbiti.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zbiti.common.memory.DataToMemory;

public class RedisCacheClearAspact {

	private Log logger = LogFactory.getLog(RedisCacheClearAspact.class);
	public void clear(){
		logger.info("清除Redis缓存");
		DataToMemory.fleshAll();
	}
}
