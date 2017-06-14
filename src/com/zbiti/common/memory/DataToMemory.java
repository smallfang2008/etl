package com.zbiti.common.memory;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.zbiti.common.ConfigUtil;
import com.zbiti.common.jedis.JedisManager;

public class DataToMemory {

	static Log logger =LogFactory.getLog(DataToMemory.class);
//	private static String redisSever = "c71";
	public static String redisServer=ConfigUtil.getValueByKey("MEMORY_REDIS");//="132.228.241.21:6388";
//	static{
//		loadRedisServer();
//	}
//	static void loadRedisServer(){
//		Properties prop = new Properties();
//		try {
//			prop.load(DataToMemory.class.getResourceAsStream("/config/etl.properties"));
//			redisServer=prop.getProperty("MEMORY_REDIS");
//			logger.info(redisServer);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
 	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		String id ="11";
//		Object o = DataToMemory.readData("command", id);
//		System.out.println("Object o = = " + o);
//		//Node tempNode = new Node();
//		List<Command> commands = new ArrayList<Command>();
//		Command cmd =new Command();
//		cmd.setCommandId("aaa");
//		commands.add(cmd);
//		DataToMemory.write("command", id, commands);
//	}
	

	// 1、调用方法
	// 2、从redis读取数据，读取成功，返回结果
	// 3、读取失败，从数据库获取数据，缓存数据，返回结果
	// 4、read 数据
	// 5、writer 数据
	public static Object readData(String objectCode, String dataKey) {
		// 实例化 redis，获取客户端
		JedisManager jedisManager = null;
		Jedis jedis = null;
		try { 
//			jedisManager = JedisManager.getInstance(redisSever);
			jedisManager=JedisManager.getInstanceByIpPort(redisServer);
			jedis = jedisManager.getJedis();
			if(jedis==null){
				System.out.println("error :not get redis ................"  );
				return null;
			}
			byte[] object = jedis.get((objectCode + ":" + dataKey).getBytes());
			if (object != null) {
				return SerializeUtil.unserialize(object);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(jedisManager!=null){
				jedisManager.returnJedis(jedis);
			} 
		}
		return null;
	}

	/**
	 * 数据序列化，然后进行数据的缓存
	 * 
	 * @param ols
	 */
	public static void write(String objectName, String dataKey, Object ols) {
		JedisManager jedisManager = null;
		Jedis jedis = null;
		try { 
			if (ols == null) {
				return;
			}
			byte[] serializeByte = SerializeUtil.serialize(ols);
			if (serializeByte != null) {
				jedisManager=JedisManager.getInstanceByIpPort(redisServer);
				jedis = jedisManager.getJedis();
				if(jedis==null){
					System.out.println("............<<<< error not get redis instance...........");
					return ;
				}
				jedis.set((objectName + ":" + dataKey).getBytes(), serializeByte);		
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(jedisManager!=null){
				jedisManager.returnJedis(jedis);
			} 
		}
	}
	/**
	 * 
	 * delete 缓存对象的数据删除，如果不知道具体的id全部删除，如果知道，则指定删除，
	 * 用于数据的刷新。
	 * @param ols
	 */
	public static void deleteObject(String objectName, String dataKey) {
		JedisManager jedisManager = null;
		Jedis jedis = null;
		try { 
			jedisManager=JedisManager.getInstanceByIpPort(redisServer);
			jedis = jedisManager.getJedis();
			if(jedis==null){
				System.out.println("............<<<< error not get redis instance...........");
				return ;
			}
			if("".equals(dataKey) || dataKey==null){
				Set<String> keys=jedis.keys(objectName+":*");
				if(keys==null){
					return;
				}
				Iterator<String> keysIterator=keys.iterator();
				while (keysIterator.hasNext()) {
					//jedis.del(keys.iterator().next());
					String key=keysIterator.next();
					//System.out.println(jedis.get(keys.iterator().next()));
					jedis.del(key);
				}
			}else{
				jedis.del((objectName + ":" + dataKey).getBytes());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(jedisManager!=null){
				jedisManager.returnJedis(jedis);
			}
		}
	}
	/**
	 * 数据序列化，然后进行数据的缓存
	 * 
	 * @param ols
	 */
	public static void delete(String objectName, String dataKey) {
		JedisManager jedisManager = null;
		Jedis jedis = null;
		try {
			jedisManager=JedisManager.getInstanceByIpPort(redisServer);
			jedis = jedisManager.getJedis();
			if(jedis==null){
				return  ;
			}
			for(String datKey:dataKey.split(","))
				jedis.del((objectName + ":" + datKey).getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(jedisManager!=null){
				jedisManager.returnJedis(jedis);
			} 
		}
	}
	
	/**
	 * 数据序列化，然后进行数据的缓存
	 * 
	 * @param ols
	 */
	public static void fleshAll() {
		JedisManager jedisManager = null;
		Jedis jedis = null;
		try {
			jedisManager=JedisManager.getInstanceByIpPort(redisServer);
			jedis = jedisManager.getJedis();
			if(jedis==null){
				return  ;
			}
			jedis.flushAll();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(jedisManager!=null){
				jedisManager.returnJedis(jedis);
			} 
		}
	}
}
