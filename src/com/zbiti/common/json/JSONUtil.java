package com.zbiti.common.json;

import net.sf.json.JSONObject;

public class JSONUtil
{
	@SuppressWarnings("unchecked")
	public static <T>T parse(String obj,Class<T> clazz){
		try{
			JSONObject json= JSONObject.fromObject(obj);
			return (T)(JSONObject.toBean(json,clazz));
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		//JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy/MM/dd HH:mm:ss"}) );
		//JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"MM/dd/yyyy HH:mm:ss"}) );
		
		//ludl add for log warn 
		//Property 'day' of class java.util.Date has no write method. SKIPPED.
		
		
	}
	
	public static String toJsonString(Object obj){
		JSONObject json =JSONObject.fromObject(obj);
		return json.toString();
	}
	
	/*
	public static void main(String[] args){
		
		Filename filename = new Filename();
		filename.setModifyDate(new Date());
		//filename.setName("myfile");
		List<Filename> filenames = new ArrayList<Filename>();
		filenames.add(filename);
		
		Command cmd = new Command();
		cmd.getParam().put("filenames", filenames);
		String cmdString = JsonUtil.toJsonString(cmd);
		System.out.println(cmdString);
		
		Command filename2 = JsonUtil.parse(cmdString,Command.class);
		//System.out.println(filename2.getModifyDate());
	}
	*/
}
