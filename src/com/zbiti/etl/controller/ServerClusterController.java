/**
 * 
 */
package com.zbiti.etl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.common.vo.DataVO;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.smo.IServerClusterService;
import com.zbiti.etl.core.smo.IZookeeperClusterService;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.ServerCluster;
import com.zbiti.etl.core.vo.ZookeeperCluster;

/**
 * @author wuzhongbo
 *
 */
@Controller
@RequestMapping("/etl/cluser")
public class ServerClusterController {

	private Log log = LogFactory.getLog(ServerClusterController.class);
	@Autowired(required = true)
	private IServerClusterService clusterService;
	@Autowired(required = true)
	private IZookeeperClusterService zookeeperClusterService;
	@Autowired(required = true)
	private INodeService nodeService;
	
	@RequestMapping("")
	public String index()
	{
		return "/etl/server_farm/cluserList";
	}
	@RequestMapping("/queryServerCluster")
	@ResponseBody
	public PageQueryResult<ServerCluster> queryServerClusterByPage(DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "serverClusterName", required = false) String serverClusterName,
			@RequestParam(value = "zookeeperCode", required = false) String zookeeperCode) throws IOException{
		log.debug("查询集群");
		Page page = new Page();
		page.setCurrentPage(pageIndex+1);
		page.setShowCount(rows);
		Map<String, String> map = new HashMap<String, String>();
		map.put("serverClusterName", serverClusterName);
		map.put("zookeeperCode", "-".equals(zookeeperCode) ? "" : zookeeperCode);
		PageQueryResult<ServerCluster> queryResult = clusterService.queryPage(map, page);
		return queryResult;
	}
	@RequestMapping("/queryZookeeperCluserList")
	@ResponseBody
	public List<Map<String, Object>> queryZookeeperCluserList(@RequestParam(value = "needAll", required = false) String needAll) {
		List<ZookeeperCluster> zookeeperList = zookeeperClusterService.selectZookeeperClusterList(new HashMap<String, Object>());
		List<Map<String, Object>> reslutList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		if ("1".equals(needAll)) {
			map = new HashMap<String, Object>();
			map.put("text", "全部");
			map.put("value", "-");
			reslutList.add(map);
		}
		if (null != zookeeperList && !zookeeperList.isEmpty()) {
			for (ZookeeperCluster zookeeperCluster: zookeeperList) {
				map = new HashMap<String, Object>();
				map.put("text", zookeeperCluster.getZookeeperName());
				map.put("value", zookeeperCluster.getZookeeperCode());
				reslutList.add(map);
			}
		}
		return reslutList;
	}
	
	@RequestMapping("/queryNodeList")
	@ResponseBody
	public PageQueryResult<Node> queryNodeList(DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId) {
		Page page = new Page();
		page.setCurrentPage(pageIndex+1);
		page.setShowCount(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serverClusterId", serverClusterId);
		PageQueryResult<Node> queryResult = nodeService.selectNodeListPage(map, page);
		return queryResult;
	}
	
	@RequestMapping("/addServerCluster")
	@ResponseBody
	public String addServerCluster(ServerCluster cluster){
		String err = "0";
		try {
			int val = validNull(cluster, false);
			if (1 != val) {
				return val+"";
			}
			/*if (clusterService.checkExistClusterName(cluster.getServerClusterName())) {
				return "2";
			}*/
			clusterService.save(cluster);
			return "1";
		} catch (Exception e) {
			err = e.getLocalizedMessage();
			log.error(e);
		}
		return err;
	}
	
	@RequestMapping("/editServerCluster")
	@ResponseBody
	public String editServerCluster(ServerCluster cluster){
		String err = "0";
		try {
			int val = validNull(cluster, true);
			if (1 != val) {
				return val+"";
			}
			/*List<ServerCluster> scList = clusterService.queryServerClusterByName(cluster.getServerClusterName());
			if (null != scList && !scList.isEmpty()) {
				ServerCluster sc = scList.get(0);
				if (!sc.getServerClusterId().equals(cluster.getServerClusterId())) {
					return "2";
				}
			}*/
			clusterService.update(cluster);
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getLocalizedMessage();
		}
		return err;
	}
	
	@RequestMapping("/delServerCluster")
	@ResponseBody
	public String delServerCluster(@RequestParam(value = "serverClusterIds", required = false, defaultValue = "-404") String serverClusterIds){
		if ("".equals(serverClusterIds))//报404
			return "404";
		if ("-404".equals(serverClusterIds)) //报501
			return "501";
		String err = "0";
		try {
			//校验被删除集群是否已经被使用
			String [] serverClusterIdArr = serverClusterIds.split(",");
			for (String serverClusterId : serverClusterIdArr) {
				List<Node> nodeList = nodeService.getByServerClusterId(serverClusterId);
				if (null != nodeList && !nodeList.isEmpty()) {
					return "2";//存在被使用的服务器集群
				}
			}
			clusterService.deletes(serverClusterIds);
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getMessage();
		}
		return err;
	}
	
	@RequestMapping("/checkExistClusterName")
	@ResponseBody
	public String checkExistClusterName(@RequestParam(value = "clusterName", required = true, defaultValue = "") String clusterName){
		String err = "0";
		try {
			if (null == clusterName || "".equals(clusterName)) {
				return "3";
			}
			if (clusterService.checkExistClusterName(clusterName)) {
				return "2";
			}
			return "1";
		} catch (Exception e) {
			err = e.getLocalizedMessage();
			log.error(e);
		}
		return err;
	}
	
	@RequestMapping("/toEditServerCluster")
	public ModelAndView toUpdateServerCluster(@RequestParam(value = "serverClusterId", required = false, defaultValue = "1") String serverClusterId) {
		ServerCluster cluster = clusterService.getById(serverClusterId);
		ModelAndView mav = new ModelAndView("/etl/server_farm/editServerCluser");
		mav.addObject("editObj", cluster);
		return mav;
	}
	
	@RequestMapping("/infoServerCluster")
	public ModelAndView toInfoServerCluster(@RequestParam(value = "serverClusterId", required = false, defaultValue = "1") String serverClusterId) {
		ServerCluster cluster = clusterService.getById(serverClusterId);
		ModelAndView mav = new ModelAndView("/etl/server_farm/infoServerCluser");
		mav.addObject("editObj", cluster);
		return mav;
	}
	
	/**校验属性是否为null*/
	private int validNull(ServerCluster cluster, boolean isEdit) {
		if (null == cluster) {
			return 101;
		}
		if (null == cluster.getRootPath() || "".equals(cluster.getRootPath().trim())) {
			return 103;
		}
		if (null == cluster.getServerClusterName() || "".equals(cluster.getServerClusterName().trim())) {
			return 104;
		}
		if (null == cluster.getZookeeperCluster() || null == cluster.getZookeeperCluster().getZookeeperCode() || "".equals(cluster.getZookeeperCluster().getZookeeperCode().trim())) {
			return 105;
		}
		if (isEdit) {
			if (null == cluster.getServerClusterId() || "".equals(cluster.getServerClusterId().trim())) {
				return 106;
			}
		}
		return 1;
	}
}
