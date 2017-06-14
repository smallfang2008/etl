package com.zbiti.etl.runtime.main;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbiti.common.StringUtil;
import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.runtime.IMasterRuntimeService;
import com.zbiti.etl.runtime.INodeRuntimeService;

public class RunNode {

	protected static final Log logger = LogFactory.getLog(RunNode.class);
	public static void main(String[] args) {
		File file=new File(StringUtil.getFilePath("stop"));
		if(file.exists()){
			logger.info("存在stop文件，请先删除");
			System.exit(0);
			
		}

		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"config/applicationContext-common.xml"});
		IMasterRuntimeService masterRuntimeService=ctx.getBean(IMasterRuntimeService.class);
		masterRuntimeService.start();
		INodeRuntimeService nodeRuntimeService=ctx.getBean(INodeRuntimeService.class);
		nodeRuntimeService.start();
		masterRuntimeService.join();
		nodeRuntimeService.join();
//		logger.info("守护进程启动");
		
		logger.info("守护进程准备退出，关闭zookeeper连接");
		INodeService nodeService=ctx.getBean(INodeService.class);;
        Node node=nodeService.getByCode(nodeService.getLocationNode());
        CuratorFramework zk=ZooKeeperUtil.getZookeeperClient(node.getServerCluster().getZookeeperCluster().getZookeeperString());
        ZooKeeperUtil.realeaseZookeeper(zk);
		System.exit(0);
	}
}
