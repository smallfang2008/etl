package com.zbiti.etl.core.smo.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zbiti.common.dao.DaoUtil;
import com.zbiti.common.memory.DataToMemory;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.dao.ResourceDao;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.vo.Resource;
import com.zbiti.etl.core.vo.ResourceType;
@Service("ResourceServiceImpl")
public class ResourceServiceImpl implements IResourceService{
	
	@javax.annotation.Resource
	private ResourceDao reDao;

	@Override
	public Resource buildResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
//		DataToMemory.delete("Resource", id); 
		reDao.delete(id);
	}

	@Override
	public Resource getById(String resourceId) {
//		return getByIdFromMemory(resourceId);
		return reDao.getById(resourceId);
	}

	/**
	 * 从内存中获取数据
	 * 
	 * @param id
	 * @return
	 */
	public Resource getByIdFromMemory(String resourceId) { 
		Object o = DataToMemory.readData("Resource", resourceId);
		System.out.println("Object o = = " + o);
		if (o == null) {
			Resource temp = reDao.getById(resourceId); 
			// 数据缓存
			DataToMemory.write("Resource", resourceId, temp); 
			return temp;
		} else {
			return (Resource) o;
		}
	}
	
	@Override
	public Resource getByName(String resourceName) {
//		return getByNameFromMemory(resourceName);
		return reDao.getByName(resourceName);
	}
	/**
	 * 从内存中获取数据
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Resource getByNameCache(String resourceName) { 
		Object o = DataToMemory.readData("Resource", resourceName);
		//System.out.println("Object o = = " + o);
		if (o == null) {
			Resource temp = reDao.getByName(resourceName); 
			// 数据缓存
			DataToMemory.write("Resource", resourceName, temp); 
			return temp;
		} else {
			return (Resource) o;
		}
	}
	
	@Override
	public PageQueryResult<Resource> queryPage(Map<String, String> map, Page page) {
		// TODO Auto-generated method stub
		List<Resource> resourceList = reDao.selectResourceListPage(DaoUtil.toMap(map, page));
		return new PageQueryResult<Resource>(resourceList, page.getTotalResult());
		// return reDao.queryPage(resource, page);
	}

	@Override
	public void save(Resource resource) {
//		DataToMemory.write("Resource", resource.getResourceId(), resource); 
//		DataToMemory.write("Resource", resource.getResourceName(), resource);
		reDao.save(resource);
	}

	
	@Override
	public void update(Resource resource) {
//		DataToMemory.write("Resource", resource.getResourceId(), resource); 
//		DataToMemory.write("Resource", resource.getResourceName(), resource);
		reDao.update(resource);
	}


	@Override
	public List<Map<String, Object>> qryResource() {
		return reDao.qryResource();
	}

	@Override
	public ResourceType getResourceTypeName(String resourceTypeId) {
		return reDao.getResourceTypeName(resourceTypeId);
	}


	@Override
	public List<Map<String, Object>> qryFTPServerName() {
		Map map = null;
		return reDao.qryFTPServerName(map);
	}
	
	@Override
	public List<Map<String, Object>> qryDBServerName() {
		Map map = null;
		return reDao.qryDBServerName(map);
	}
	
	@Override
	public List<Map<String, Object>> qrySCPServerName() {
		Map map = null;
		return reDao.qrySCPServerName(map);
	}

	@Override
	public Map<String, Object> getHostName(String resourceName){
		return reDao.getHostName(resourceName);
	}
	
	@Override
	public List<Map<String, Object>> qryDBServerNameEds(String hostName){
		return reDao.qryDBServerNameEds(hostName);
	}
	
	@Override
	public String getUserName(String resourceName){
		return reDao.getUserName(resourceName);
	}
	
	@Override
	public String getResourceName(Map<String, String> map){
		return reDao.getResourceName(map);
	}
	


}
