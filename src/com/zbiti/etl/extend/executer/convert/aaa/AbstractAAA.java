package com.zbiti.etl.extend.executer.convert.aaa;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.zbiti.etl.extend.executer.convert.nel.AbstractConverter;


public abstract class AbstractAAA  extends AbstractConverter {
	Locale locale = Locale.US;
	SimpleDateFormat aaaOld = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",locale);
	SimpleDateFormat aaaNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat aaaNewId = new SimpleDateFormat("yyyyMMddHH");
	SimpleDateFormat aaaNewTimeId = new SimpleDateFormat("yyyyMMddHHmmss");
	//map用于从数据库读取的IMSI<-->REGION_ID
	private Map<String,String> record_map = new HashMap<String,String>();
	
	//错误原因匹配MAP
	private Map<String,String> error_map = new HashMap<String,String>();

	private String error_id;

	//暂时先注释掉
	public void init(){
//		if(record_map==null||record_map.size() == 0){
//			try {
//				String sql= "select IMSI,REGION_ID from demo.C_PSD_D_IMSI";		
//				record_map = IQjdbc.getCon(sql);
//				sql = this.getErrorSql();
//				if (sql != null) {
//					error_map = IQjdbc.getCon(sql);
//				}
//			} catch (Exception e) {
//				LogUtil.writeLog("[CONVERT_ERROR]"+e.getMessage());
//				record_map = new HashMap<String,String>();
//				error_map = new HashMap<String,String>();
//			}
//		}
//		initExt();
	}
	
	
//	public abstract String  getErrorSql();
	
	public void initExt(){
		
	}
	
//	首先对IMSI是非460开头的---》记region id为国外的那个id
//	然后对一些特殊的（如空，UNKNOW USER等）--》计入特殊ID
//	460开头的---》遍历表对应（如果在这种情况下还有不能对应的---》计入未关联运营商的那个ID
	public String imsiToregion(String imsi){
		if(imsi == null || imsi.equals("")||imsi.equalsIgnoreCase("UNKNOWN USER")) {
			return "89000000";
		} else if(!imsi.startsWith("460")) {
			return "87000000";
		} else {
			//			if(imsi.substring(0,3).equals("460")) {
			if(imsi.length() > 11) {
				imsi = imsi.substring(0, 11).trim();
//				System.out.println(imsi);
			}
			String region_id = record_map.get(imsi);
			if(region_id == null || region_id.equals("")){
				return "88000000";
			}else {
				return region_id;
			}
		}
	}
	
	/**时间转换DATE_ID程序
	 * 根据日志是何种,ANAAA不同于AAA和VPDN的格式
	 * 处理方法也不一致
	 */
	public String timeToId(String time, String type){
		try {
			if(type.equals("VPDN")||type.equals("AAA")) {
				return aaaNewId.format(aaaOld.parse(time));
			} else {
				return aaaNewId.format(aaaNew.parse(time));
			}
		} catch (Exception e) {
			return aaaNewId.format(new Date());
		}
	}
	
	// 对形如“Wed Dec 15 00:00:01 2010”的日期变换成“2010-10-15 00:00:01”的格式
	public String getFormatDate(String time) {
		try {
			return aaaNew.format(aaaOld.parse(time));
		} catch (Exception e) {
			return aaaNew.format(new Date());
		}
	}
	
	//连接数据库，并一次性将IMSI和REGION_ID匹配信息查询放入MAP中
	//
	public String errorToId (String code, String type) {
		error_id = error_map.get(code);
		if(error_id == null){
			error_id = "";
		}
		return error_id;
	}

	//符合条件的记录导出到指定路径下
	public String getNewFileTempPath(String newFileTempPath){
		String path = "";
		if(newFileTempPath!=null&&!newFileTempPath.trim().equals("")){
			if(!newFileTempPath.endsWith("/")&&!newFileTempPath.endsWith("\\"))
				path = new File(newFileTempPath).getParent();
			else
				path = newFileTempPath;
			if(!new File(path).exists()){
				new File(path).mkdirs();
			}
		}
		return path;
	}
	
	public File createFile(String path){		
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	public void writeString2File(String result,String path,RandomAccessFile getFile) throws Exception{
		
		getFile = new RandomAccessFile(createFile(path),"rw");
		getFile.seek(getFile.length());
		getFile.writeBytes(result);
//		File file = new File(path+ "_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()));
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)),true);
//		pw.println(result);
//		if(pw!=null)
//			pw.close();
//		getFile.close();
	}
	
	public void IOClosable (RandomAccessFile accessFile)throws Exception{
		if(accessFile != null){
			accessFile.close();
		}
	}
	
	public boolean ifMatch(String sqlName,String key){
		Map<String,String> value = this.getMapBySqlName(sqlName);
		boolean flag = false;
		value.remove(null);
		value.remove("");
		
		flag =value.containsKey(key);
//		for(Iterator<String> it= value.keySet().iterator();it.hasNext();){
//			String tempKey = (String)it.next();
//			if(tempKey == key || tempKey.equals(key)){
//				return true;
//			}else{
//				continue;
//			}
//		}
//		for(String key1:value.keySet()){
//			System.out.println(key1);
//		}
		return flag;
	}
	public void afterRun(){
		
	}
	
	public String getFormatDate(SimpleDateFormat oldTimeFormat,SimpleDateFormat newTimeFormat,String time) {
		try {
			return newTimeFormat.format(oldTimeFormat.parse(time));
		} catch (Exception e) {
			return newTimeFormat.format(new Date());
		}
	}
}
