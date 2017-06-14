package com.zbiti.common.jedis;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.zbiti.common.StringUtil;
import com.zbiti.common.stream.BufferedRandomAccessFile;

public class RedisInfoRemark {


	private RedisInfoRemark(){
		readConfig();
	}
	
	private static RedisInfoRemark instance;
	public static RedisInfoRemark getInstance(){
		if(instance==null){
			instance=new RedisInfoRemark();
		}
		return instance;
	}
	
	void readConfig(){
		try{
			File f=new File(RedisInfoRemark.class.getResource("/redis_info_remark.properties").getPath());
			BufferedRandomAccessFile fileRead =new BufferedRandomAccessFile(f, "r");//read模式
			String temp=new String(); 
			while((temp=fileRead.readLine())!=null){
				String[] redisInfoRemark=StringUtil.ChangeCode(temp.trim(), "iso-8859-1", "utf-8").split("=");
				if(redisInfoRemark.length>=2)
					redisInfoRemarkMap.put(redisInfoRemark[0], redisInfoRemark[1]);
			}
		}catch(Exception ex){
			System.out.println("load conf failure.");
		}
	}
	
	private Map<String,String> redisInfoRemarkMap=new HashMap<String, String>();

	public Map<String, String> getRedisInfoRemarkMap() {
		return redisInfoRemarkMap;
	}
}
