package com.zbiti.etl.test;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.smo.ISceneService;
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Scene;
import com.zbiti.etl.core.vo.Step;
import com.zbiti.etl.runtime.ICommandService;

public class Test {
//	protected static final Log logger = LogFactory.getLog(Test.class);
	static Logger logger=Logger.getLogger(Test.class);
//	static Logger logger=Logger.getLogger(Test.class);
	int i=0;
	public static void main(String[] args) {
//		PropertyConfigurator.configure("./src/config/log4j.properties");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"config/applicationContext-common.xml"});
		ICommandService commandService=ctx.getBean(ICommandService.class);
		ISceneService sceneService=ctx.getBean(ISceneService.class);
		IStepService stepService=ctx.getBean(IStepService.class);
//		IStepService stepService=ctx.getBean(IStepService.class);
//		Step step=stepService.getStepContainsNextById("2");
//		System.out.println(step.getStepName());
//		System.out.println(step.getNextStep());
//		List<Step> steps=stepService.listFirstStepBySceneId("1");
//		for(Step step:steps){
//			System.out.println(step.getServerCluster().getServerClusterId());
//		}
//		Step  step = stepService.getStepContainsNextById("7");
//		logger.info(step.getNextStep());
//		while(true){
			Scene scene=sceneService.getSceneById("24");
//			System.out.println(scene.getName());
//		}
//		commandService.
		commandService.createWaitTimer(scene);
//		commandService.stopTimer("4");

//		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
//		CuratorFramework zk=ZooKeeperUtil.getZookeeperClient(zkString);
//		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
//		CuratorFramework zk = CuratorFrameworkFactory.newClient(zkString, new ExponentialBackoffRetry(10 * 000,600));
//		zk.start();
		
		
		
//		List<String> schedules=ZooKeeperUtil.getChildren("/schedule", zk);
//		System.out.println(schedules);
		
//		IJobScheduleService jobScheduleService=ctx.getBean(IJobScheduleService.class);
//		Command cmd=new Command();
//		cmd.setSceneId("1");
//		jobScheduleService.scheduleRunningCmd(cmd);
		
//		Map m=new HashMap();
//		for(int i=0;i<100000;i++){
//			String xxx=TimeUtil.getNowDateTime("yyMMddHHmmss")+TimeUtil.getRandom(4);
//			
//			logger.info(xxx);
//			if(m.containsKey(xxx)){
//				logger.info(xxx+" repeat");
//				break;
//			}
//		}
		
		
//		System.out.println(11);
		
		
		
		
		
		
//		try {
////			ZooKeeperUtil.deleteNode("/schedule/wait/ETL_IN/3069/3111", zk);
//
//			
//			ZooKeeperUtil.createNode("/schedule", null, zk);
//			ZooKeeperUtil.createNode("/schedule/wait", null, zk);
//			ZooKeeperUtil.createNode("/schedule/doing", null, zk);
//			ZooKeeperUtil.createNode("/schedule/timer", null, zk);
//			ZooKeeperUtil.createNode("/schedule/timer/wait", null, zk);
//			ZooKeeperUtil.createNode("/schedule/timer/doing", null, zk);
//			ZooKeeperUtil.createNode("/schedule/error", null, zk);
//			ZooKeeperUtil.createNode("/schedule/his", null, zk);
//			ZooKeeperUtil.createNode("/schedule/clean", null, zk);
//			ZooKeeperUtil.createNode("/schedule/hiserror", null, zk);
////			NodeCache nodeCache=new NodeCache(zk, "/schedule/stop", true);
////			nodeCache.start();
////			nodeCache.getListenable().addListener(new NodeCacheListener() {
////				
////				@Override
////				public void nodeChanged() throws Exception {
////					System.out.println(1);
////				}
////			});
//			 final PathChildrenCache childrenCache = new PathChildrenCache(zk, "/schedule", true);
//			 childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
//			 childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
//				
//				@Override
//				public void childEvent(CuratorFramework zk, PathChildrenCacheEvent event)
//						throws Exception {
////					System.out.println(event.getType());
////					System.out.println(event.getData().getPath());
//					if(event.getType()==Type.CHILD_ADDED&&"/schedule/stop".equals(event.getData().getPath())){
//						//触发事件
//						
//						System.out.println("stop node add");
//					}
//				}
//			});
//			 Thread.sleep(100000);
////			System.out.println(ZooKeeperUtil.getData("/schedule/testyhp", zk));
////			ZooKeeperUtil.createTempNode("/schedule/testyhp", "NODE2", zk);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
		
		
		
//		IEtlUserService userService=ctx.getBean(IEtlUserService.class);
////		EtlUser user=c.getByUsername("yhp");
//////		System.out.println(user.getPassword());
////
////		logger.info(user.getPassword());
//////		logger.error(user.getPassword());
////		
////		logger.error("error");
////		logger.info("info");
////		DataToMemory d=ctx.getBean(DataToMemory.class);
////		System.out.println(DataToMemory.redisServer);
//		SchedulerFactory factory=new StdSchedulerFactory();
//		Scheduler jobSchedule=null;
//		try {
//			jobSchedule = factory.getScheduler();
//		} catch (SchedulerException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		JobDetail jobDetail = new JobDetail("1", IJobScheduleService.JOB_GROUP_NAME,QuartzScheduleStepBean.class);
//		jobDetail.getJobDataMap().put("userService", userService);
//		CronTrigger trigger = new CronTrigger("1",IJobScheduleService.TRIGGER_GROUP_NAME);
//		try {
//			trigger.setCronExpression("1/10 * * * * ?");
//			jobSchedule.scheduleJob(jobDetail, trigger);
//			jobSchedule.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static void main321(String[] args) {
//		Command cc=new Command();
//		
//		System.out.println(cc.getDispatchTime());
//		System.out.println(new Date(cc.getDispatchTime()));
//		cc.setDispatchTime(System.currentTimeMillis());
//		String cmd=JSONUtil.toJsonString(cc);
//		System.out.println(cmd);
//		Command c=JSONUtil.parse(cmd, Command.class);
//		System.out.println(c.getDispatchTime());
		
		
		
		
		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
		CuratorFramework zk=ZooKeeperUtil.getZookeeperClient(zkString);
		final PathChildrenCache childrenCache = new PathChildrenCache(zk, "/schedule/test", true);
		 try {
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework arg0, PathChildrenCacheEvent event)
					throws Exception {
				if(event.getType()==Type.CHILD_ADDED){
					//触发事件
//					System.out.println("第一个监听触发");
//					System.out.println("stop node add");
//					childrenCache.getListenable().removeListener(this);
//					Thread.sleep(5000);
//					System.out.println("休眠结束");
					logger.info("add");
				}
			}
		});
		 
		 
		 try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
//		CuratorFramework zk = CuratorFrameworkFactory.newClient(zkString, new ExponentialBackoffRetry(10 * 000,600));
//		zk.start();
	}
	
	
	
	
	public static void main1234(String[] args) {
		new Thread(new Test().new LockThread("node_1000")).start();
		new Thread(new Test().new LockThread("node_1001")).start();
		new Thread(new Test().new LockThread("node_1002")).start();
		
		

//		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
//		CuratorFramework zk=CuratorFrameworkFactory.newClient(zkString,10000,3000, new ExponentialBackoffRetry(1000, 25));
//		
//		zk.start();
//		
////		try {
////			Thread.sleep(100000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		try {
//			ZooKeeperUtil.createTempNode("/schedule/tmp", null, zk);
//		} catch (KeeperException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			ZooKeeperUtil.createTempNode("/schedule/tmp", null, zk);
//		} catch (KeeperException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("over");
//		zk.close();
		
//		try{
//			return;
////			throw new Exception("信息");
//		}catch (Exception e) {
//			logger.info(e.getMessage());
//		}finally{
//			logger.info("1");
//		}
	}
	
	class LockThread implements Runnable{

		private String node;
		public LockThread(String node){
			this.node=node;
		}
		@Override
		public void run() {
			String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
			CuratorFramework zk=CuratorFrameworkFactory.newClient(zkString, new ExponentialBackoffRetry(1000, 25));
			zk.start();
			try{
				while(true){
					logger.info(node+" try to lock");
					boolean flag=getLock("/schedule", node, zk);
//					InterProcessMutex lock=new InterProcessMutex(zk, "/schedule/test");
//					lock.acquire(1000, TimeUnit.MILLISECONDS)
					if(flag){
						i=i+1;
						logger.info(node+" lock success--------------------------------"+i);//,lock value:"+ZooKeeperUtil.getData("/schedule/lock", zk)+",Sleep 500ms");
//						Thread.sleep(100000);
						Thread.sleep(1000);
						logger.info(node+" unlock --------------------------------------");
						ZooKeeperUtil.releaseLock("/schedule", zk);// 结束之后解锁
						i=0;
//						lock.release();
					}else{
						logger.info(node+" lock failure");
					}
//					logger.info(node+" sleep 1000ms,next");
//					Thread.sleep(1000);
				}
			}catch (Exception e) {
				logger.error(e);
			}finally{
				zk.close();
			}
		}
		
	} 
	public boolean getLock(String path,String node, CuratorFramework zk) {
		try {
			createTempNode(path + "/" + "lock", node, zk);
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
	 * 创建临时节点
	 * 
	 * @param path
	 * @param value
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void createTempNode(String path, String value,
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
	private static void  testLock(){
		
	}
}
