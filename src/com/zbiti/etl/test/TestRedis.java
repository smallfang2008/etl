package com.zbiti.etl.test;

import redis.clients.jedis.Jedis;

import com.zbiti.common.jedis.JedisManager;

public class TestRedis {

	public static void main(String[] args) {
		JedisManager jedisManager=JedisManager.getInstanceByIpPort("132.228.241.94:10380");
		Jedis jedis=jedisManager.getJedis();
		jedis.set("1","1");
		System.out.println(jedis.keys("*"));
		jedis.del("1");
		jedisManager.returnJedis(jedis);
		
	}
}
