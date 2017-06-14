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
import com.zbiti.etl.core.smo.IStepService;
import com.zbiti.etl.core.vo.Node;
import com.zbiti.etl.core.vo.ServerCluster;


/**
 * @author wuzhongbo
 *
 */
@Controller
@RequestMapping("/etl/node")
public class NodeController {
	private Log log = LogFactory.getLog(NodeController.class);
	@Autowired(required = true)
	private IServerClusterService clusterService;
	@Autowired(required = true)
	private INodeService nodeService;
	@Autowired(required = true)
	private IStepService stepService;
	
	@RequestMapping("")
	public String index()
	{
		return "/etl/work_node/nodeList";
	}
	@RequestMapping("/queryNodeByPage")
	@ResponseBody
	public PageQueryResult<Node> queryNodeByPage(DataVO<String, String> vo,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int rows,
			@RequestParam(value = "nodeCode", required = false) String nodeCode,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "ipAddress", required = false) String ipAddress,
			@RequestParam(value = "serverClusterId", required = false) String serverClusterId) throws IOException{
		log.debug("查询集群");
		Page page = new Page();
		page.setCurrentPage(pageIndex+1);
		page.setShowCount(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeCode", nodeCode);
		map.put("serverName", serverName);
		map.put("serverClusterId", "-".equals(serverClusterId) ? "" : serverClusterId);
		map.put("ipAddress", ipAddress);
		PageQueryResult<Node> queryResult = nodeService.selectNodeListPage(map, page);
		return queryResult;
	}
	@RequestMapping("/queryServerClusterList")
	@ResponseBody
	public List<Map<String, Object>> queryServerClusterList(@RequestParam(value = "needAll", required = false) String needAll) {
		List<ServerCluster> cluserList = clusterService.queryServerClusterList(new HashMap<String, Object>());
		List<Map<String, Object>> reslutList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		if ("1".equals(needAll)) {
			map = new HashMap<String, Object>();
			map.put("text", "全部");
			map.put("value", "-");
			reslutList.add(map);
		}
		if (null != cluserList && !cluserList.isEmpty()) {
			for (ServerCluster cluser: cluserList) {
				map = new HashMap<String, Object>();
				map.put("text", cluser.getServerClusterName());
				map.put("value", cluser.getServerClusterId());
				reslutList.add(map);
			}
		}
		return reslutList;
	}
	
	@RequestMapping("/addNode")
	@ResponseBody
	public String addNode(Node node){
		String err = "0";
		try {
			int val = validNull(node, false);
			if (1 != val) {
				return val+"";
			}
			String regexpIp = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
			if(!node.getIpAddress().matches(regexpIp))
				return "2";//IP地址输入不正确！
			/*if (null != nodeService.getByCode(node.getNodeCode())){
				return "3";//以存在节点编码！
			}*/
			nodeService.save(node);
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getLocalizedMessage();
		}
		return err;
	}
	
	@RequestMapping("/editNode")
	@ResponseBody
	public String editNode(Node node){
		String err = "0";
		try {
			int val = validNull(node, true);
			if (1 != val) {
				return val+"";
			}
			String regexpIp = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
			if(!node.getIpAddress().matches(regexpIp))
				return "2";//IP地址输入不正确！
			/*if (!node.getOldNodeCode().equals(node.getNodeCode()) && null != nodeService.getByCode(node.getNodeCode())){
				return "3";//以存在节点编码！
			}*/
			nodeService.update(node);
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getLocalizedMessage();
		}
		return err;
	}
	
	@RequestMapping("/delNode")
	@ResponseBody
	public String delNode(@RequestParam(value = "nodeCode", required = false, defaultValue = "-404") String nodeCode){
		if ("".equals(nodeCode))//报404
			return "404";
		if ("-404".equals(nodeCode)) //报501
			return "501";
		String err = "0";
		try {
			String [] nodeCodeArr = nodeCode.split(",");
			for (String code : nodeCodeArr) {
				if ("".equals(code)) {
					continue;
				}
				List<?> nodeList = stepService.listStepByNode(code);
				if (null != nodeList && !nodeList.isEmpty()) {
					return "2";//存在被使用的节点
				}
			}
			nodeService.deletes(nodeCode);
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getLocalizedMessage();
		}
		return err;
	}
	
	@RequestMapping("/checkExistNodeCode")
	@ResponseBody
	public String checkExistNodeCode(@RequestParam(value = "nodeCode", required = true, defaultValue = "") String nodeCode){
		String err = "0";
		try {
			if (null == nodeCode || "".equals(nodeCode)) {
				return "3";
			}
			if (null != nodeService.getByCode(nodeCode)){
				return "2";//以存在节点编码！
			}
			return "1";
		} catch (Exception e) {
			log.error(e);
			err = e.getLocalizedMessage();
		}
		return err;
	}
	
	@RequestMapping("/toEditNode")
	public ModelAndView toEditNode(@RequestParam(value = "nodeCode", required = false, defaultValue = "1") String nodeCode) {
		Node node = nodeService.getByCode(nodeCode);
		ModelAndView mav = new ModelAndView("/etl/work_node/editNode");
		List<?> nodeList = stepService.listStepByNode(nodeCode);
		mav.addObject("editObj", node);
		mav.addObject("editFlag", null == nodeList || nodeList.isEmpty());
		return mav;
	}
	
	/**校验属性是否为null*/
	private int validNull(Node node, boolean isEdit) {
		if (null == node) {
			return 101;
		}
		if (null == node.getNodeCode() || "".equals(node.getNodeCode().trim())) {
			return 102;
		}
		if (null == node.getLogPath() || "".equals(node.getLogPath().trim())) {
			return 103;
		}
		if (null == node.getIpAddress() || "".equals(node.getIpAddress().trim())) {
			return 104;
		}
		if (null == node.getIsGetClusterTask() || "".equals(node.getIsGetClusterTask().trim())) {
			node.setIsGetClusterTask("0");
		}
		if (null == node.getServerName() || "".equals(node.getServerName().trim())) {
			return 105;
		}
		if (null == node.getServerCluster() || null == node.getServerCluster().getServerClusterId() || "".equals(node.getServerCluster().getServerClusterId().trim())) {
			return 106;
		}
		if (isEdit) {
			if (null == node.getOldNodeCode() || "".equals(node.getOldNodeCode().trim())) {
				return 107;
			}
		}
		return 1;
	}
}
