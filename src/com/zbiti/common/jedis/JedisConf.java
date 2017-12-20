package com.zbiti.common.jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JedisConf {

	static Map<String,JedisConf> redisConfMap = new HashMap<String,JedisConf>();
	
	String key;
	
	public JedisConf(String key){
		this.key = key;
		readConf();
	}
	
	int maxActive;
	
	public  int getMaxActive() {
		return maxActive;
	}

	public  int getMaxIdle() {
		return maxIdle;
	}

	public  int getMaxWait() {
		return maxWait;
	}

	public  boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public  String getServerIp() {
		return serverIp;
	}

	public  int getServerPort() {
		return serverPort;
	}
	

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	int maxIdle;
	int maxWait;
	boolean testOnBorrow;
	String serverIp;
	int serverPort;
	int timeOut;
	
	static JedisConf getInstance(String key){
		JedisConf conf =null;
		synchronized(JedisConf.class){
			conf = redisConfMap.get(key);
			if (conf==null){
				conf = new JedisConf(key);
				redisConfMap.put(key, conf);
			}
		}
		return conf;
	}
		
	 void readConf() {
		try{
			Properties prop = new Properties();
			if (key==null||key.equals("")){
				prop.load(JedisConf.class.getResourceAsStream("/config/flow_caculator.properties"));
			}else{
				prop.load(JedisConf.class.getResourceAsStream("/config/flow_caculator."+key+".properties"));
			}
			maxActive = Integer.parseInt(prop.get("redis.maxActive").toString());
			maxIdle = Integer.parseInt(prop.get("redis.maxIdle").toString());
			maxWait = Integer.parseInt(prop.get("redis.maxWait").toString());
			serverIp = prop.get("redis.serverIp").toString();
			//serverIp ="132.228.241.21";
			serverPort = Integer.parseInt(prop.get("redis.serverPort").toString());
			//serverPort=6388;
			testOnBorrow = Boolean.parseBoolean(prop.get("redis.testOnBorrow").toString());
			timeOut = Integer.parseInt(prop.get("redis.timeOut").toString());
//			System.out.println("------------get-conf-info ,serverIp="+ serverIp+",serverPort="+serverPort);
			
		}catch(Exception ex){
			System.out.println("---error---------");
			ex.printStackTrace();
		}
	}
	 public static void main(String[] args) throws Exception {
		 JedisConf aa = new JedisConf("c71");
		 aa.readConf();
		}
}
