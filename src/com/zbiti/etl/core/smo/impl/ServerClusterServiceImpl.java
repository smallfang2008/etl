/**
 * 
 */
package com.zbiti.etl.core.smo.impl;

//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zbiti.common.dao.DaoUtil;
import com.zbiti.common.memory.DataToMemory;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.dao.ServerClusterDao;
//import com.zbiti.etl.core.dao.ZookeeperClusterDao;
import com.zbiti.etl.core.smo.IServerClusterService;
import com.zbiti.etl.core.vo.ServerCluster;
//import com.zbiti.etl.core.vo.ZookeeperCluster;

/**
 * @author wuzhongbo
 *
 */
@Service("ServerClusterServiceImpl")
public class ServerClusterServiceImpl implements IServerClusterService {

	@javax.annotation.Resource
	private ServerClusterDao clusterDao;
	//@javax.annotation.Resource
	//private ZookeeperClusterDao zookeeperClusterDao;
	
	/* (non-Javadoc)
	 * @see com.zbiti.etl.core.smo.IServerFarmService#delete(java.lang.String)
	 */
	@Override
	public void delete(String serverClusterId) {
		DataToMemory.delete("ServerCluster", serverClusterId); 
		clusterDao.delete(serverClusterId);
	}

	/* (non-Javadoc)
	 * @see com.zbiti.etl.core.smo.IServerFarmService#getById(java.lang.String)
	 */
	@Override
	public ServerCluster getById(String serverClusterId) {
		Object o = DataToMemory.readData("ServerCluster", serverClusterId);
		System.out.println("Object o = = " + o);
		if (o == null) {
			ServerCluster temp = clusterDao.getById(serverClusterId);
			if (null == temp) {
				return null;
			}
			DataToMemory.write("ServerCluster", serverClusterId, temp); 
			return temp;
		} 
		return (ServerCluster) o;
	}

	/* (non-Javadoc)
	 * @see com.zbiti.etl.core.smo.IServerFarmService#queryPage(com.zbiti.etl.core.vo.ServerFarm, com.zbiti.core.dto.Page)
	 */
	@Override
	public PageQueryResult<ServerCluster> queryPage(Map<String, String> map,
			Page page) {
		List<ServerCluster> list = clusterDao.selectServerFarmListPage(DaoUtil.toMap(map, page));
		return new PageQueryResult<ServerCluster>(list, page.getTotalResult());
	}

	/* (non-Javadoc)
	 * @see com.zbiti.etl.core.smo.IServerFarmService#save(com.zbiti.etl.core.vo.ServerFarm)
	 */
	@Override
	public void save(ServerCluster cluster) {
		//DataToMemory.write("ServerCluster", cluster.getServerClusterId(), cluster); 
		clusterDao.save(cluster);
	}

	/* (non-Javadoc)
	 * @see com.zbiti.etl.core.smo.IServerFarmService#update(com.zbiti.etl.core.vo.ServerFarm)
	 */
	@Override
	public void update(ServerCluster cluster) {
		clusterDao.update(cluster);
		DataToMemory.delete("ServerCluster", cluster.getServerClusterId()); 
		/*Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("zookeeperCode", cluster.getZookeeperCluster().getZookeeperCode());
		ZookeeperCluster zkc = zookeeperClusterDao.getZookeeperClusterByCode(condition);
		cluster.getZookeeperCluster().setZookeeperName(null == zkc ? "" : zkc.getZookeeperName());
		DataToMemory.write("ServerCluster", cluster.getServerClusterId(), cluster); */
		
	}

	@Override
	public List<ServerCluster> queryServerClusterList(Map<String, Object> param) {
		return clusterDao.queryServerClusterList(param);
	}

	@Override
	public void deletes(String serverClusterIds) {
		clusterDao.delete(serverClusterIds);
		String [] idArr = serverClusterIds.split(",");
		for (String id : idArr) {
			if ("".equals(id)) {
				continue;
			}
			DataToMemory.delete("ServerCluster", id); 
		}
	}

	@Override
	public void deletes(List<String> serverClusterIdList) {
		StringBuffer ids = new StringBuffer();
		for (String id : serverClusterIdList) {
			if (ids.length() > 0) {
				ids.append(",");
			}
			ids.append(id);
		}
		deletes(ids.toString());
	}

	@Override
	public boolean checkExistClusterName(String clusterName) {
		List<?> scList = clusterDao.queryServerClusterByName(clusterName);
		return null != scList && !scList.isEmpty();
	}

	@Override
	public List<ServerCluster> queryServerClusterByName(String clusterName) {
		return clusterDao.queryServerClusterByName(clusterName);
	}

}
