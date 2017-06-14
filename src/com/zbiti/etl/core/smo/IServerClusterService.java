/**
 * 
 */
package com.zbiti.etl.core.smo;

import java.util.List;
import java.util.Map;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.vo.ServerCluster;

/**
 * @author wuzhongbo
 *
 */
public interface IServerClusterService {
	
	public ServerCluster getById(String serverClusterId);
	
	public PageQueryResult<ServerCluster> queryPage(Map<String, String> map, Page page);
	
	public List<ServerCluster> queryServerClusterList(Map<String, Object> param);
	
	public void save(ServerCluster cluster);
	
	public void update(ServerCluster cluster);
	
	/**
	 * 此方法也能处理批量删除<br>
	 * 区别在于不能操作缓存对象
	 * @param nodeCode
	 */
	public void delete(String serverClusterId);
	
	/**
	 * 批量删除
	 * @param serverClusterIds 逗号分隔的服务集群ID
	 */
	public void deletes(String serverClusterIds);
	
	public void deletes(List<String> serverClusterIdList);
	/**是否存在同名集群*/
	public boolean checkExistClusterName(String clusterName);
	
	/**获取指定名称的集群*/
	public List<ServerCluster> queryServerClusterByName(String clusterName);
}
