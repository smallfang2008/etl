package com.zbiti.etl.extend.executer.convert;

import java.util.HashMap;
import java.util.Map;

public class ConvertUtil {
	public static Map<String,String> cityMap=new HashMap<String, String>();
	static{
		cityMap.put("00","00");
        cityMap.put("","00");
        cityMap.put("0001","0100");
        cityMap.put("0002","0200");
        cityMap.put("0003","0300");
        cityMap.put("0004","0400");
        cityMap.put("0005","0500");
        cityMap.put("0006","0600");
        cityMap.put("0007","0700");
        cityMap.put("0008","0800");
        cityMap.put("0009","0900");
        cityMap.put("0010","1000");
        cityMap.put("0011","1100");
        cityMap.put("0012","1200");
        cityMap.put("0013","1300");
        cityMap.put("\u5168\u7701","00");
        cityMap.put("\u5357\u4EAC","0100");
        cityMap.put("\u82CF\u5DDE","0200");
        cityMap.put("\u65E0\u9521","0300");
        cityMap.put("\u5E38\u5DDE","0400");
        cityMap.put("\u9547\u6C5F","0500");
        cityMap.put("\u626C\u5DDE","0600");
        cityMap.put("\u5357\u901A","0700");
        cityMap.put("\u6CF0\u5DDE","0800");
        cityMap.put("\u5F90\u5DDE","0900");
        cityMap.put("\u6DEE\u5B89","1000");
        cityMap.put("\u76D0\u57CE","1100");
        cityMap.put("\u8FDE\u4E91\u6E2F","1200");
        cityMap.put("\u5BBF\u8FC1","1300");
	}
	public final static String SPIT_SIGN = "#_#";
}