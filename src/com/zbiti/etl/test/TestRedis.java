package com.zbiti.etl.test;

import redis.clients.jedis.Jedis;

import com.zbiti.common.jedis.JedisManager;

public class TestRedis {

	public static void main(String[] args) {
		JedisManager jedisManager=JedisManager.getInstanceByIpPort("127.0.0.1:6379");
		Jedis jedis=jedisManager.getJedis();
		jedis.set("runoobkey","runoobkey");
		System.out.println(jedis.keys("*"));
//		jedis.del("runoobkey");
		jedisManager.returnJedis(jedis);
		
	}
}
