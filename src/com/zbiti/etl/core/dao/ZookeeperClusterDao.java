/**
 * 
 */
package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.ZookeeperCluster;

/**
 * @author wuzhongbo
 *
 */
public interface ZookeeperClusterDao extends SqlMapper{
	
	public List<ZookeeperCluster> selectZookeeperClusterList(Map<String, Object> condition);

	public ZookeeperCluster getZookeeperClusterByCode(Map<String, Object> condition);
}
