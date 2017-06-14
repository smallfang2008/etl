package com.zbiti.etl.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zbiti.common.memory.DataToMemory;
import com.zbiti.etl.runtime.ICommandService;

@Controller
@RequestMapping("/etl/welcome")
public class WelcomeController {

	Log logger =LogFactory.getLog(WelcomeController.class);
	
	@Autowired
	ICommandService commandService;
	
	@RequestMapping("/loadWelcomeInfo")
	@ResponseBody
	public Map loadWelcomeInfo(){
		Map data=new HashMap();
		data.put("masterNode", commandService.getMasterNode());
		return data;
	}
	
	@RequestMapping("/clearRedisCache")
	@ResponseBody
	public String clearRedisCache(){
		try{
			DataToMemory.fleshAll();
			return "1";
		}catch (Exception e) {
			logger.error("清除缓存失败",e);
			return "0";
		}
	}
}
