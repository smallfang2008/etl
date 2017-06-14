<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>集群详情</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/page-min.css" rel="stylesheet" type="text/css" />  
 </head>
 <body>
  <div class="container">
    <h3>集群详情</h3>
    <div style="text-align: right;padding-right: 20px;">
    	<input type="button" class="button button-small button-info" id="backButton" value="返回">
    </div>
  	<hr>
  	<p><span class="auxiliary-text">集群基本信息</span></p>
  <div class="row">
    <form id ="SC_Form" class="form-horizontal" action="" method="post">
      <!--    -->
      <div class="row">
        <div class="control-group span12">
          <label class="control-label" style="width: 200px;">集群名称：</label>
          <div class="controls">
          	<span class="control-text">${editObj.serverClusterName }</span>
          </div>
        </div>
        <div class="control-group span10">
          <label class="control-label" style="width: 130px;">根目录：</label>
          <div class="controls">
          	<span class="control-text">${editObj.rootPath }</span>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span22">
          <label class="control-label" style="width: 200px;">ZOOKEEPER集群：</label>
          <div class="controls">
          	<span class="control-text">${editObj.zookeeperCluster.zookeeperName }</span>
          </div>
        </div>
      </div>
    </form>
    </div>
    <p><span class="auxiliary-text">集群所含节点</span></p>
     <div class="search-grid-container">
      <div id="info_grid"></div>
    </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  var Grid = BUI.Grid,
	Store = BUI.Data.Store,
	node_columns = [
      { title: '节点编码', width:150, dataIndex: 'nodeCode'},
      { title: 'IP地址',width:100, dataIndex: 'ipAddress'},
      { title: 'FTP服务名称',width:150, dataIndex: 'serverName'},
      { title: '日志目录',width:100,dataIndex: 'logPath'},
      { title: '最大任务数', width: 100, dataIndex: 'maxTasks'}
    ];
  var node_store = new Store({
      url : '${pageContext.request.contextPath}/etl/cluser/queryNodeList.do',
      autoLoad:false,
	  pageSize:8
    });
  var grid_serv = new Grid.Grid({
      render:'#info_grid',
      loadMask: true,
      forceFit: true,
      columns : node_columns,
      store: node_store,
      //plugins : [Grid.Plugins.AutoFit], //自适应宽度插件
      // 底部工具栏
      bbar : {
        //items 也可以在此配置
        // pagingBar:表明包含分页栏
        pagingBar:true
      }
  });
  grid_serv.render();

	$(document).ready(function(){
		$("#backButton").click(function(){
			location.href="${pageContext.request.contextPath}/content/etl/server_farm/cluserList.jsp";
		});
		node_store.load({"serverClusterId":"${editObj.serverClusterId}","limit":8});
	});
  </script>

<body>
</html> 