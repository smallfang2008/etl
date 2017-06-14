package com.zbiti.etl.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.core.mapper.SqlMapper;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.ResourceType;

public interface ResourceDao extends SqlMapper{
	
	/*
	 * 很据ID查询资源信息
	 */
	public Resource getById(@Param("resourceId") String resourceId);
	
	/*
	 * 根据资源名称查询资源信息
	 */
	public Resource getByName(@Param("resourceName") String resourceName);
	
	/*
	 * 资源的分页查询
	 */
	public PageQueryResult<Resource> queryPage(Resource resource,Page page);
	
	/*
	 * 更新资源信息
	 */
	public void update(Resource resource);
	
	/*
	 * 新增资源信息
	 */
	public void save(Resource resource);
	
	/*
	 * 删除资源信息
	 */
	public void delete(@Param("resourceId") String id);

	
	public List<Resource> selectResourceListPage(Map<String, Object> param);
	
	public List<Map<String, Object>> qryResource();
	
	/**
	 * 查询资源类型名称
	 * @param resourceTypeId
	 * @return
	 */
	public ResourceType getResourceTypeName(String resourceTypeId);
	

	public List<Map<String, Object>> qryFTPServerName(Map map);

	public List<Map<String, Object>> qryDBServerName(Map map);
	
	public List<Map<String, Object>> qrySCPServerName(Map map);
	
	public Map<String, Object> getHostName(String resourceName);
	public String getUserName(String resourceName);
	//qryDBServerNameEds
	public List<Map<String, Object>> qryDBServerNameEds(String hostName);
	public String getResourceName(Map<String, String> map);
}
