package com.zbiti.etl.core.smo.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zbiti.common.dao.DaoUtil;
import com.zbiti.common.memory.DataToMemory;
import com.zbiti.common.pageQuery.PageQueryResult;
import com.zbiti.core.dto.Page;
import com.zbiti.etl.core.dao.NodeDao;
import com.zbiti.etl.core.smo.INodeService;
import com.zbiti.etl.core.vo.Node;

@Service("NodeServiceImpl")
public class NodeServiceImpl implements INodeService{


	@Value("${etl.nodeCode}")
	String nodeCode;
	
	@javax.annotation.Resource
	private NodeDao nodeDao;
	
	@Override
	public Node getByCode(String nodeCode) {
		Object o = DataToMemory.readData("Node", nodeCode);
		if (o == null) {
			Node node = nodeDao.getByCode(nodeCode);
			if (null == node) {
				return null;
			}
			DataToMemory.write("Node", node.getNodeCode(), node);
			return node;
		}
		return (Node) o;
	}

	@Override
	public String getLocationNode() {
		return nodeCode;
	}

	@Override
	public void delete(String nodeCode) {
		nodeDao.delete("'" + nodeCode.replace("'", "''") + "'");
		DataToMemory.delete("Node", nodeCode);
	}

	@Override
	public void save(Node node) {
		nodeDao.save(node);
		DataToMemory.write("Node", node.getNodeCode(), node);
	}

	@Override
	public PageQueryResult<Node> selectNodeListPage(Map<String, Object> param, Page page) {
		return new PageQueryResult<Node>(nodeDao.selectNodeListPage(DaoUtil.toMap(param, page)), page.getTotalResult());
	}

	@Override
	public void update(Node node) {
		//long ii = System.currentTimeMillis();
		nodeDao.update(node);
		DataToMemory.delete("Node", node.getOldNodeCode());
		/*System.out.println("操作数据库耗时：" + (System.currentTimeMillis() - ii));
		ii = System.currentTimeMillis();
		if (!node.getOldNodeCode().equals(node.getNodeCode())) {
			DataToMemory.delete("Node", node.getOldNodeCode());
		}
		DataToMemory.write("Node", node.getNodeCode(), node);
		System.out.println("操作redis耗时：" + (System.currentTimeMillis() - ii));*/
	}

	@Override
	public List<Node> getByServerClusterId(String serverClusterId) {
		return nodeDao.getByServerClusterId(serverClusterId);
	}

	@Override
	public void deletes(String nodeCodes) {
		String nodeCode = "'" + nodeCodes.replace("'", "''").replace(",", "','") + "'";
		nodeDao.delete(nodeCode);
		String [] codeArr = nodeCodes.split(",");
		for (String code : codeArr) {
			if ("".equals(code)) {
				continue;
			}
			DataToMemory.delete("Node", code);
		}
	}

	@Override
	public void deletes(List<String> nodeCodeList) {
		StringBuffer nodeCodes = new StringBuffer();
		for (String code : nodeCodeList) {
			if (nodeCodes.length() > 0) {
				nodeCodes.append(",");
			}
			nodeCodes.append(code);
		}
		deletes(nodeCodes.toString());
	}

	@Override
	public List<Node> selectNodeList(Map<String, Object> param) {
		return nodeDao.selectNodeList(param);
	}

	
}
