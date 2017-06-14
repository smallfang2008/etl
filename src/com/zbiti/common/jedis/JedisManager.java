package com.zbiti.common.jedis;


import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager {
	private JedisPool jedisPool ;
	/*
	static RedisManager redisDaoManager = new RedisManager();
	*/
	
	static Map<String,JedisManager> redisManagerMap = new HashMap<String,JedisManager>();
	
	public static JedisManager getInstance(){
		return getInstance(null);
	}
	public static JedisManager getInstance(String key){
		JedisManager redisManager = null;
		synchronized (JedisManager.class){
			redisManager=redisManagerMap.get(key);
			if (redisManager==null){
				redisManager = new JedisManager(key);
				redisManagerMap.put(key, redisManager);
			}
		}
		return redisManager;
	}
	
	public static JedisManager getInstanceByIpPort(String ipPort){//后来各种分服务器啊,数据来源分/数据去向分/数据统计分/数据上报分/端口也分
		JedisManager redisManager = null;
		synchronized (JedisManager.class){
			redisManager=redisManagerMap.get(ipPort);
			if (redisManager==null){
				String[] ipPortArray=ipPort.split(":");
				int port=6379;//设置默认
				try{
					port=Integer.parseInt(ipPortArray[1]);
				}catch (Exception e) {
					System.out.println("port is null or port is not a num");
				}
				redisManager = new JedisManager(ipPortArray[0],port);//
				redisManagerMap.put(ipPort, redisManager);
			}
		}
		return redisManager;
	}
	
	void initJedisPool(){
		JedisConf redisConf = JedisConf.getInstance(key);
		JedisPoolConfig jedisPoolconfig = new JedisPoolConfig();
		jedisPoolconfig.setMaxActive(redisConf.getMaxActive());
		jedisPoolconfig.setMaxIdle(redisConf.getMaxIdle());
		jedisPoolconfig.setMaxWait(redisConf.getMaxWait());
		jedisPoolconfig.setTestOnBorrow(redisConf.isTestOnBorrow());
        jedisPool = new JedisPool(jedisPoolconfig, redisConf.getServerIp(), redisConf.getServerPort(),redisConf.getTimeOut());
	}
	void initJedisPoolByIp(){//初始化jedisPool，其他配置照常，只是ip按照redis里面读出来的配置获取
		
	}
	
	String key;
	public JedisManager(String key){
		this.key = key;
		initJedisPool();
	}
	
	public JedisManager(String ip,int port){//为了不破坏原有的资源获取
		JedisConf redisConf = JedisConf.getInstance(null);
		JedisPoolConfig jedisPoolconfig = new JedisPoolConfig();
		jedisPoolconfig.setMaxActive(redisConf.getMaxActive());
		jedisPoolconfig.setMaxIdle(redisConf.getMaxIdle());
		jedisPoolconfig.setMaxWait(redisConf.getMaxWait());
		jedisPoolconfig.setTestOnBorrow(redisConf.isTestOnBorrow());
        jedisPool = new JedisPool(jedisPoolconfig, ip, port,redisConf.getTimeOut());
	}
	
	public Jedis getJedis(){
		Jedis jedis=jedisPool.getResource();
		return jedis;
	}
	
	public void returnJedis(Jedis jedis){
		jedisPool.returnBrokenResource(jedis);
		jedisPool.returnResource(jedis);
		//jedisPool.returnBrokenResource(redisDao.getJedis());
	}
	
	
	/*
	public static void main(String [] args){
		RedisManager.getInstance().getRedisDao().incrBy("key_1", "1");
		RedisManager.getInstance().getRedisDao().incrBy("key_1", "1");
		RedisManager.getInstance().getRedisDao().incrBy("key_1", "1");
		RedisManager.getInstance().getRedisDao().incrBy("key_1", "1");
		RedisManager.getInstance().getRedisDao().incrBy("key_1", "1");
	}
	*/
}
