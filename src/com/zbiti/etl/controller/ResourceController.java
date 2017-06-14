package com.zbiti.etl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.common.vo.DataVO;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.smo.IResourceService;
import com.zbiti.etl.core.vo.Resource;

/**
 * @author yhp
 * 资源ACTION
 * 2014-6-3
 */
@Controller
@RequestMapping("/etl/resource")
public class ResourceController
{
	@Autowired(required = true)
	private IResourceService resourceService;

	@RequestMapping("")
	public String index()
	{
		return "/etl/source_manager/resourceList";
	}
	
	//查询所有资源
	@RequestMapping("/resourceServicePageQuery")
	public @ResponseBody
	PageQueryResult<Resource> selectAllResource(DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "resourceType", required = false) String resourceType,
			@RequestParam(value = "hostName", required = false) String hostName,
			@RequestParam(value = "resourceName", required = false) String resourceName)
			throws IOException
	{
		System.out.println(resourceName);
		

		Page page = new Page();
		page.setCurrentPage(pageIndex+1);
		page.setShowCount(rows);
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourceType", resourceType);
		map.put("hostName", hostName);
		map.put("resourceName", resourceName);
		PageQueryResult<Resource> pageQueryResult = resourceService.queryPage(map, page);
//		System.out.println(JSONObject.fromObject(pageQueryResult).toString());
		return pageQueryResult;
	}
	
	//资源编辑
	@RequestMapping("/editResource")
	public ModelAndView selectResource(@RequestParam(value = "resourceId", required = false, defaultValue = "1") String resourceId)
		throws IOException
	{
		System.out.println("resourceId:"+resourceId);
		ModelAndView mav = new ModelAndView("/etl/source_manager/editResource");
		Resource queryResourceResult = resourceService.getById(resourceId);
	//	System.out.println("queryResourceResult:{}"+queryResourceResult);
		mav.addObject("editObj", queryResourceResult);
		return mav;
//		return "/etl/source_manager/editScene";
	}
	
//	//资源更新操作
//	@RequestMapping("/update")
//	public ModelAndView updateResource(Resource resource)
//		throws IOException
//	{	
//		
//		ModelAndView mav = new ModelAndView("/etl/source_manager/resourceService");
//		resource.setVersion("");
//	//	ResourceType resourceType = resourceService.getResourceTypeName(resource.getResourceType().getResourceTypeId());
//	//	resource.setResourceType(resourceType);
//	
//		resourceService.update(resource);
//		
//		return mav;
//	}
	
	//资源更新操作
	@RequestMapping("/update")
	public @ResponseBody String updateResource(Resource resource)
		throws IOException
	{	
		resource.setVersion("");
	//	ResourceType resourceType = resourceService.getResourceTypeName(resource.getResourceType().getResourceTypeId());
	//	resource.setResourceType(resourceType);
		String result="1";
		try{
			Resource resourceTmp=resourceService.getByName(resource.getResourceName());
			if(resourceTmp!=null){
				if(resource!=null&&resource.getResourceId()!=null&&resource.getResourceId().equals(resourceTmp.getResourceId())){
					
				}else{
					return "3";
				}
			}
			if(resource!=null&&resource.getResourceId()!=null&&!"".equals(resource.getResourceId()))
				resourceService.update(resource);
			else
				resourceService.save(resource);
		}catch (Exception e) {
			result="2";
			e.printStackTrace();
		}
		return result;
	}

	
	//保存资源
	@RequestMapping("/save")
	public ModelAndView saveResource(Resource resource)
		throws IOException
	{	
		ModelAndView mav = new ModelAndView("/etl/source_manager/resourceService");
		resource.setVersion("");
	//	ResourceType resourceType = resourceService.getResourceTypeName(resource.getResourceType().getResourceTypeId());
	//	resource.setResourceType(resourceType);
		resourceService.save(resource);
		return mav;
	}
	
	//删除资源
	@RequestMapping("/deleteResource")
	public @ResponseBody String deleteResource(HttpServletRequest request,@RequestParam(value = "resourceId", required = false) String resourceId)
		throws IOException
	{
		System.out.println(request.getParameter("resourceId"));
		String result="1";
		try{
			resourceService.delete(resourceId);
		}catch (Exception e) {
			result="2";
			e.printStackTrace();
		}
		return result;
	}
	
	//查询所有的资源类型ID
	@RequestMapping("/qryresource")
	public @ResponseBody List<Map<String, String>> qryResource()
	{
		List<Map<String, Object>> resourceTypeList = resourceService.qryResource();
		List<Map<String,String >> results = new ArrayList<Map<String, String>>();
		for (Map<String, Object> resourceType : resourceTypeList)
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("value", resourceType.get("RESOURCE_TYPE_ID").toString());
			result.put("text", resourceType.get("RESOURCE_TYPE_NAME").toString());
			results.add(result);
		}
		return results;
	}
	
	//查询所有的资源类型ID
	@RequestMapping("/qryFTPServerName")
	public @ResponseBody List<Map<String, String>> qryFTPServerName()
	{
		List<Map<String, Object>> resourceTypeList = resourceService.qryFTPServerName();
		List<Map<String,String >> results = new ArrayList<Map<String, String>>();
		for (Map<String, Object> resourceType : resourceTypeList)
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("value", resourceType.get("RESOURCE_NAME").toString());
			result.put("text", resourceType.get("RESOURCE_NAME").toString());
			results.add(result);
		}
		return results;
	}
}
