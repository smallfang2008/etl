/**
 * 
 */
package com.zbiti.etl.core.smo.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zbiti.etl.core.dao.ZookeeperClusterDao;
import com.zbiti.etl.core.smo.IZookeeperClusterService;
import com.zbiti.etl.core.vo.ZookeeperCluster;

/**
 * @author wuzhongbo
 *
 */
@Service("ZookeeperClusterServiceImpl")
public class ZookeeperClusterServiceImpl implements IZookeeperClusterService {

	@javax.annotation.Resource
	private ZookeeperClusterDao zookeeperClusterDao;
	@Override
	public List<ZookeeperCluster> selectZookeeperClusterList(
			Map<String, Object> condition) {
		return zookeeperClusterDao.selectZookeeperClusterList(condition);
	}

}
