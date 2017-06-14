package com.zbiti.etl.extend.executer.convert.nel;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.zbiti.common.DBCommon;


public abstract class AbstractConverter{
	
	protected BlockingQueue<String> splitQueue =null;
	private BufferedReader bufferedReader = null;
	private FileWriter fw = null;
	private int bufferedMaxNumber=1024*1024;
	private String machineName = "";//抽取LOG的主机名
	private String fileName = "";//抽取文件的文件名
	private String dbDriver="com.sybase.jdbc3.jdbc.SybDriver";
	//private String dbUrl = "jdbc:sybase:Tds:132.228.165.142:2638/ossne";
	private String dbUrl = "jdbc:sybase:Tds:132.228.165.142:2638/ossne";
	private String dbUser = "dba";
	private String dbPassword = "sql";
	private static Map<String,Map<String,String>> valueMap = new HashMap<String,Map<String,String>>();
	final static String SPIT_SIGN = "\"|\"";
	
	private String fromCode = ""; //转换前的编码
	private String toCode = ""; //转换后的编码
	
	public AbstractConverter(){
		
	}
	
	public long getBufferedMaxNumber() {
		return bufferedMaxNumber;
	}

	public void setBufferedMaxNumber(int bufferedMaxNumber) {
		this.bufferedMaxNumber = bufferedMaxNumber;
	}
	
//	public void convert(String gzip, String dest) {
//		convert(gzip,dest,0);
//	}

	/**
	 * 此方法读出解析文件，并写出
	 */	
//	public long convert(String gzip, String dest,long position){
//		LogUtil.writeLog("[CNV]"+gzip+"[CONVERT]START");
//		//编码转换
//		if(fromCode!=null && toCode != null &&!"".equals(fromCode) && !"".equals(toCode)){
//			gzip = fileConv(gzip);
//		}
//		int bufferedMaxNumberTmp=bufferedMaxNumber;
//		int maxLineLength=0;
//		init_p();
//		long tmpTime=0;
//		long start = System.currentTimeMillis();
//		RandomAccessFile raf = null;
//		FileChannel fc = null;
//		ByteBuffer dsts=null;
//		try {
//			raf=new RandomAccessFile(new File(gzip),"r");
//			long fileLength = raf.length();
//			if(fileLength<=position)
//				return position;			
//			fc = raf.getChannel();	
//			byte[] bt;
//			StringBuffer sBuffer;
//			fw = new FileWriter(new File(dest), true);
//			boolean isEnd = false;
//			while(fileLength>position){
//				raf.seek(position);
//				if(bufferedMaxNumberTmp>(fileLength-position)){
//					bufferedMaxNumberTmp = Integer.parseInt(""+(fileLength-position));
//					isEnd=true;
//				}
//				
//				dsts = ByteBuffer.allocate(bufferedMaxNumberTmp);
//				fc.read(dsts);
//				bt = dsts.array();
//				for(int i=bufferedMaxNumberTmp; i>0; i--){
//					if(bt[i-1]=='\n'){
//						bufferedMaxNumberTmp = i;
//						break;
//					}
//				}
//				position += bufferedMaxNumberTmp;
//				
//				bufferedReader = new BufferedReader(new StringReader(new String(dsts.array(),0,bufferedMaxNumberTmp)));
//				String line = bufferedReader.readLine();
//				sBuffer = new StringBuffer();
//				String tmp_str = "";
//				while(line != null) {
//					try{
//						tmp_str = getLine(line);
//                        if(splitQueue != null){
//                            splitQueue.put(tmp_str);
//                        }
//						splitFileData();
//					}
//					catch(Exception e){
//						LogUtil.writeLog("[CNV_ERROR] FILE: "+gzip+" LINE: "+line);
//						tmp_str = "";
//					}
//					int length = tmp_str.length();
//					if(length>maxLineLength)
//						maxLineLength = length;
//					sBuffer.append(tmp_str);
//					line = bufferedReader.readLine();
//				}
//				dsts.clear();
//				long start_1 = System.currentTimeMillis();
//				fw.write(sBuffer.toString());
//				fw.flush();
//				tmpTime += (System.currentTimeMillis()-start_1);
//				if(isEnd)
//					break;
//				bufferedMaxNumberTmp=bufferedMaxNumber;
//			}
//			afterRun();
//			fw.close();
//			
//			LogUtil.writeLog("[CNV]"+gzip+"[CNV_TIME]"+(System.currentTimeMillis()-start)+"[IO_TIME]"+tmpTime+"[MAX_LENGTH]"+maxLineLength);
//			long t=Runtime.getRuntime().totalMemory();
//			long f=Runtime.getRuntime().freeMemory();
//			LogUtil.writeLog("free/total:"+f+"/"+t);
//		} catch (Exception e) {
//			LogUtil.writeLog("[CNV_ERROR]"+e.getMessage());
//		} finally {
//			try {
//				//关闭读入输出流
//				if(raf!=null)raf.close();
//				if(fc!=null)fc.close();
//				if(fw!=null)fw.close();
//			} catch (IOException e) {
//				LogUtil.writeLog("[CONVERT_ERROR]"+e.getMessage());
//			}
//		}
//		return position;
//		
//	}

	private Map<String,String> initMap(String sql){
		try {
			final DBCommon dbCommon = new DBCommon(
					NelCommon.getDBUploadConnTimes(), NelCommon
							.getDBUploadConnWaittime());
			final Connection conn = dbCommon.getConnection(dbDriver, dbUrl,
					dbUser, dbPassword);
			return DBCommon.getCon(sql,dbDriver,conn);
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.writeLog("[CONVERT_ERROR]"+e.getMessage());
			return new HashMap<String,String>();
		}
	}
	
	public synchronized Map<String,String> getMapBySqlName(String sqlName){
		Map<String,String> value = valueMap.get(sqlName);
		if(value==null){
			value = this.initMap(SqlMap.getSqlMap().get(sqlName));
			valueMap.put(sqlName, value);
		}
		return value;
	}
	
	public String getValueByKey(String sqlName,String key){
		Map<String,String> value = this.getMapBySqlName(sqlName);
		String returnValue = value.get(key);
		return returnValue==null?"":returnValue;
	}
	
	/**********2013-12-13 yujl modify end***********/
	public Map getMapByKey(String sqlName){
		Map<String,String> value = this.getMapBySqlName(sqlName);
		return value;
	}
	
//	private String fileConv(String filePath){
//		String convFilePath = filePath+".conv";
//		File convFile = new File(convFilePath);
//		if(!convFile.exists()){
//			try {
//				convFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if(FileCode.write(convFilePath, toCode, FileCode.read(filePath, fromCode))){
//			return convFilePath;
//		}
//		return filePath;
//	}
	
	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		if(this.machineName!=null&&!this.machineName.trim().equals(""))
			return;
		this.machineName = machineName;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}


	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}


	public String getDbUser() {
		return dbUser;
	}


	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}


	public String getDbPassword() {
		return dbPassword;
	}


	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public abstract void init();
	
	public abstract void afterRun();
	/**
	 * 分隔文件
	 */
	public void splitFileData(){
	}
	
	
//	private void init_p(){
//		IQjdbc.url = this.getDbUrl();
//		IQjdbc.user = this.getDbUser();
//		IQjdbc.password = this.getDbPassword();
//		init();
//	}
	
//	public abstract String  getLine(String line) throws Exception;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BlockingQueue<String> getSplitQueue() {
		return splitQueue;
	}

	public void setSplitQueue(BlockingQueue<String> splitQueue) {
		this.splitQueue = splitQueue;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getToCode() {
		return toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

}
