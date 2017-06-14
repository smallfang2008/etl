package com.zbiti.common.zookeeper;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 命令管理工具类
 * 
 * @author think
 * 
 */
public class ZooKeeperUtil {

	protected static final Log logger = LogFactory.getLog(ZooKeeperUtil.class);
	private static Map<String,CuratorFramework> zookeeperClients=new HashMap<String,CuratorFramework>();

	public static boolean isNodeExists(String path, CuratorFramework zk) {
		Stat stat;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		if (stat == null)
			return false;
		return true;
	}
	
	public static Stat getNodeStat(String path, CuratorFramework zk) {
		Stat stat=null;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
			return stat;
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
			return stat;
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			return stat;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return stat;
		}
	}

	public static String getData(String path, CuratorFramework zk) {
		try {
			// byte[] bytes = zk.getData(path, false, null);
			byte[] bytes = zk.getData().forPath(path);
			String s = new String(bytes);
			return s;
		} catch (KeeperException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取子节点的个数
	 * @param path
	 * @param zk
	 * @return
	 */
	public static int getChildLength(String path, CuratorFramework zk) {
		try {
			// byte[] bytes = zk.getData(path, false, null);
			Stat s =  zk.checkExists().forPath(path);
			if (s != null)
            {
                //metaData.add(String.valueOf(s.getAversion()));
               // metaData.add(String.valueOf(s.getCtime()));
                //metaData.add(String.valueOf(s.getCversion()));
                //metaData.add(String.valueOf(s.getCzxid()));
               // metaData.add(String.valueOf(s.getDataLength()));
               // metaData.add(String.valueOf(s.getEphemeralOwner()));
               // metaData.add(String.valueOf(s.getMtime()));
               // metaData.add(String.valueOf(s.getMzxid()));
                //metaData.add(String.valueOf(s.getNumChildren()));
               // metaData.add(String.valueOf(s.getPzxid()));
               // metaData.add(String.valueOf(s.getVersion()));
            } 
			return s.getNumChildren();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static List<String> getChildren(String path, CuratorFramework zk) {
		List<String> result = null;
		try {
			// result = zk.getChildren(path, false);
			result = zk.getChildren().forPath(path);
			
		 
		} catch (KeeperException e) {
			result = new ArrayList<String>();
		} catch (InterruptedException e) {
			result = new ArrayList<String>();
		} catch (Exception e) {
			result = new ArrayList<String>();
		}
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).equals("lock")) {
				result.remove(i);
				break;// 提出掉目录下的锁
			}
		}
		return result;
	}

	/**
	 * 获取路径对应的锁
	 * 
	 * @param path
	 * @param zk
	 * @return
	 */
	public static boolean getLock(String path, CuratorFramework zk) {
		try {
			createTempNode(path + "/" + "lock", null, zk);
			return true;
		} catch (KeeperException e) {
			//logger.error(e.getMessage(), e);
//			logger.info(path + "/" + "is locked............" );
			return false;
		} catch (InterruptedException e) {
//			logger.info(path + "/" + "is locked............" );
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 获取路径对应的锁
	 * 锁的值是节点
	 * @param path
	 * @param zk
	 * @return
	 */
	public static boolean getLock(String path,String node, CuratorFramework zk) {
		try {
			createTempNode(path + "/" + "lock", node, zk);
			return true;
		} catch (KeeperException e) {
			String lockValue=getData(path+"/" + "lock", zk);
			if(lockValue!=null&&node.equals(lockValue)){
				return true;
			}
			return false;
		} catch (InterruptedException e) {
//			logger.info(path + "/" + "is locked............" );
			String lockValue=getData(path+"/" + "lock", zk);
			if(lockValue!=null&&node.equals(lockValue)){
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			String lockValue=getData(path+"/" + "lock", zk);
			if(lockValue!=null&&node.equals(lockValue)){
				return true;
			}
			return false;
		}
	}

	/**
	 * 从指定路径获取锁,如果没获取到,就一直等待,直到获取锁为止
	 * 
	 * @param path
	 * @param zk
	 * @return
	 */
	public static boolean getLockAndWait(String path, CuratorFramework zk) {
		boolean isLock = false;

		while (!isLock) {
			isLock = ZooKeeperUtil.getLock(path, zk);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isLock;
	}

	public static void releaseLock(String path, CuratorFramework zk) {
		// deleteNode(path+"/"+"lock",zk);
		deleteNodeTenTimes(path + "/" + "lock", zk);// 直到把锁删除为止
	}

	/**
	 * 删除节点重载方法 当删除失败时尝试继续删除，删10次
	 * 
	 * @param path
	 * @param zk
	 */
	public static void deleteNodeTenTimes(String path, CuratorFramework zk) {
		Stat stat = null;
		
		int i = 0;
		logger.info("删除节点："+path);
		while (i < 10) {
			
			try {
				// stat = zk.exists(path, false);
				if(!zk.getZookeeperClient().isConnected()){
					throw new Exception("zookeeper未连接");
				}
				stat = zk.checkExists().forPath(path);
				if (stat == null) {
					logger.info("删除节点："+path+"不存在");
					break;
				}
				zk.delete().forPath(path);
				break;
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage(), e1);
			}
			i++;
		}
	}
	
	public static void deleteNodeTenTimes_bak_20161014(String path, CuratorFramework zk) {
		Stat stat = null;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (stat != null) {
			int i = 0;
			while (i < 10) {
				try {
					// zk.delete(path, -1);
					zk.delete().forPath(path);
					break;
				} catch (Exception e) {
					i++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						logger.error(e1.getMessage(), e1);
						;
					}
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void deleteNode(String path, CuratorFramework zk) {
		Stat stat = null;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (stat != null) {
			try {
				// zk.delete(path, -1);
				zk.delete().forPath(path);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static boolean createNode(String path, String value,
			CuratorFramework zk) {
		Stat stat = null;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (stat == null) {
			try {
				// 持久节点
				// zk.create(path, value!=null?value.getBytes():null,
				// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				zk.create().withMode(CreateMode.PERSISTENT).withACL(Ids.OPEN_ACL_UNSAFE).forPath(path,
						value != null ? value.getBytes() : null);
				return true;
			} catch (KeeperException e) {
				logger.error(e.getMessage(), e);
				return false;
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
				return false;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean setNode(String path, String value, CuratorFramework zk) {
		Stat stat = null;
		try {
			// stat = zk.exists(path, false);
			stat = zk.checkExists().forPath(path);
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (stat != null) {
			try {
				// zk.setData(path, value!=null?value.getBytes():null, -1);
				zk.setData().forPath(path,
						value != null ? value.getBytes() : null);
				return true;
			} catch (KeeperException e) {
				logger.error(e.getMessage(), e);
				return false;
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
				return false;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		} else {
			return false;
		}
	}

	public static void createNodeE(String path, String value,
			CuratorFramework zk) throws KeeperException, InterruptedException,
			Exception {
		try {
			// zk.create(path, value!=null?value.getBytes():null,
			// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zk.create().withMode(CreateMode.PERSISTENT).withACL(Ids.OPEN_ACL_UNSAFE).forPath(path, value != null ? value.getBytes() : null);
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

	}

	/**
	 * 创建临时节点
	 * 
	 * @param path
	 * @param value
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void createTempNode(String path, String value,
			CuratorFramework zk) throws KeeperException, InterruptedException, Exception{
		try {
			//zk.create(path, value != null ? value.getBytes() : null,
			//		Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zk.create().withMode(CreateMode.EPHEMERAL).forPath(path, value != null ? value.getBytes() : null);
		} catch (KeeperException e) {
			// logger.error(e.getMessage(), e);
			throw e;
		} catch (InterruptedException e) {
			// logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}


	public static void realeaseZookeeper(CuratorFramework zk) {
		/* 关闭 zookeeper 开始 */
		if (zk != null) {
			zk.close();
		}
		/* 关闭 zookeeper 结束 */
	}

	
	/**
	 * 获取zookeeper对象
	 */
	public synchronized static CuratorFramework getZookeeperClient(String zookeeperStr){
		CuratorFramework zk=zookeeperClients.get(zookeeperStr);
		if(zk!=null)
			return zk;
		if(zk==null){
			zk=CuratorFrameworkFactory.newClient(zookeeperStr,300000,10000, new ExponentialBackoffRetry(1000, 25));
	        zk.start();
	        zookeeperClients.put(zookeeperStr, zk);
		}
		return zk;
	}
}
