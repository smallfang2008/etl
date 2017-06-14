/**
 * 
 */
package com.zbiti.etl.core.smo;

import java.util.List;
import java.util.Map;

import com.zbiti.etl.core.vo.ZookeeperCluster;

/**
 * @author wuzhongbo
 *
 */
public interface IZookeeperClusterService {
	
	public List<ZookeeperCluster> selectZookeeperClusterList(Map<String, Object> condition);
}
