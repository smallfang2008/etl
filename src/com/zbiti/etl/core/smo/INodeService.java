package com.zbiti.etl.core.smo;


import java.util.List;
import java.util.Map;


import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.vo.Node;


/**
 * 
 * 节点管理服务
 *
 */
public interface INodeService {
	
	/**
	 * 根据节点编码查找节点,获取节点的同时要附带获取节点提供的容器Node2Container
	 * @param nodeCode
	 * @return
	 */
	public Node getByCode(String nodeCode);
	
	public List<Node> getByServerClusterId(String serverClusterId);
	
	public List<Node> selectNodeList(Map<String, Object> param);
	
	public String getLocationNode();
	
	public PageQueryResult<Node> selectNodeListPage(Map<String, Object> param, Page page);
	
	public void save(Node node);
	
	public void update(Node node);
	
	/**
	 * 删除<br>
	 * @param nodeCode
	 */
	public void delete(String nodeCode);
	
	/**
	 * 批量删除
	 * @param nodeCodes 逗号分隔的节点编码
	 */
	public void deletes(String nodeCodes);
	
	/**
	 * 批量删除
	 * @param 
	 */
	public void deletes(List<String> nodeCodeList);
}
