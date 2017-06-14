/**
 * 
 */
package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.ServerCluster;

/**
 * @author wuzhongbo
 *
 */
public interface ServerClusterDao extends SqlMapper {
	
	public ServerCluster getById(@Param("serverClusterId") String serverClusterId);
	
	public List<ServerCluster> selectServerFarmListPage(Map<String, Object> param);
	
	public List<ServerCluster> queryServerClusterList(Map<String, Object> param);
	
	public void save(ServerCluster serverFarm);
	
	public void update(ServerCluster serverFarm);
	
	public void delete(@Param("serverClusterId") String serverClusterId);
	
	public List<ServerCluster> queryServerClusterByName(String clusterName);
}
