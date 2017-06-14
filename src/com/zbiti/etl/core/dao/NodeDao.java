/**
 * 
 */
package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Node;

/**
 * @author wuzhongbo
 *
 */
public interface NodeDao extends SqlMapper {
	
	public Node getByCode(@Param("nodeCode") String nodeCode);
	
	public List<Node> getByServerClusterId(@Param("serverClusterId") String serverClusterId);
	
	public List<Node> selectNodeListPage(Map<String, Object> param);
	
	public List<Node> selectNodeList(Map<String, Object> param);
	
	public void save(Node node);
	
	public void update(Node node);
	
	public void delete(@Param("nodeCode") String nodeCode);
}
