package com.zbiti.etl.test;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

import com.zbiti.common.json.JSONUtil;
import com.zbiti.common.zookeeper.ZooKeeperUtil;
import com.zbiti.etl.core.vo.Command;
import com.zbiti.etl.core.vo.CommandShow;

public class TestZookeeper {

	public static void main(String[] args) {
		String zkString="132.228.241.94:10181,132.228.241.94:11181,132.228.241.94:12181";
		CuratorFramework zk=ZooKeeperUtil.getZookeeperClient(zkString);
		List<String> errorCmdIds=ZooKeeperUtil.getChildren("/schedule/hiserror", zk);
		System.out.println(errorCmdIds);
		for(String errorCmdId:errorCmdIds){
			String errorCmdStr=ZooKeeperUtil.getData("/schedule/hiserror/"+errorCmdId, zk);
			Command errorCmd=JSONUtil.parse(errorCmdStr, CommandShow.class);
			System.out.println("errorCmdId:"+errorCmdId.split("_")[1]+"\terrorCmdObjId:"+errorCmd.getCmdId()+"\tisSame:"+errorCmdId.split("_")[1].equals(errorCmd.getCmdId()));
		}
		zk.close();
	}
}
