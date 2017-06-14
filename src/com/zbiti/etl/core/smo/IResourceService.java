package com.zbiti.etl.core.smo;

import java.util.List;
import java.util.Map;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.ResourceType;


/**
 * 外部资源管理服务
 * @author sudh
 *
 */
public interface IResourceService {
	/**
	 * 根据资源ID查找资源
	 * @param resourceId
	 * @return
	 */
	public Resource getById(String resourceId);
	
	/**
	 * 根据资源名称查找资源
	 * @param resourceName
	 * @return
	 */
	public Resource getByName(String resourceName);
	public Resource getByNameCache(String resourceName);
	
	/**
	 * 构造一个空的资源
	 * @return
	 */
	public Resource buildResource();
	
	/**
	 * 分页查询资源列表
	 * @param resource
	 * @param page
	 * @return
	 */
	public PageQueryResult<Resource> queryPage(Map<String, String> map,Page page);
	
	/**
	 * 更新资源信息
	 * @param resource
	 */
	public void update(Resource resource);
	
	/**
	 * 保存资源信息
	 * @param resource
	 */
	public void save(Resource resource);
	
	/**
	 * 删除资源信息
	 * @param id
	 */
	public void delete(String id);

	
	/**
	 * 查询所有资源类型ID
	 * @return
	 */
	public List<Map<String, Object>> qryResource();
	
	/**
	 * 查询资源类型名称
	 * @param resourceTypeId
	 * @return
	 */
	public ResourceType getResourceTypeName(String resourceTypeId);

	/**
	 * @return 从资源表里获取所有的资源名称(FTP)
	 */
	public List<Map<String, Object>> qryFTPServerName();
	
	/**
	 * @return 从资源表里获取所有的资源名称(DB)
	 */
	public List<Map<String, Object>> qryDBServerName();
	
	/**
	 * @return 从资源表里获取所有的资源名称(SCP)
	 */
	public List<Map<String, Object>> qrySCPServerName();
	
	/**
	 * 从资源表中获取host_name
	 */
	public Map<String, Object> getHostName(String resourceName);
	/**
	 *根据host_name查询数据库名称
	 */
	public List<Map<String, Object>> qryDBServerNameEds(String hostName);
	/**
	 * 查询username
	 */
	public String getUserName(String resourceName);
	/**
	 * 根据hostName 和 userName查询ResourceName
	 * @param hostName
	 * @param userName
	 * @return
	 */
	public String getResourceName(Map<String, String> map);
}
