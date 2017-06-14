<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>添加集群</title>
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
  	<h3>集群新建</h3>
  	<hr>
    <form id ="SC_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/cluser/addServerCluster.do" method="post">
      <!--    -->
      <div class="row">
        <div class="control-group span20">
          <label class="control-label" style="width: 150px;"><s>*</s>集群名称：</label>
          <div class="controls">
            <input name="serverClusterName" type="text" value="" class="control-text" data-rules="{required:true}" style="width: 200px;" onblur="checkExistClusterName(this);">
          </div>
          <label class="control-label" style="width: 150px;padding-left: 2px;text-align: left;"><s id="clusterNameTip"></s></label>
        </div>
      </div>
      <div class="row">
        <div class="control-group span20">
          <label class="control-label" style="width: 150px;"><s>*</s>根目录：</label>
          <div class="controls">
            <input name="rootPath" type="text" value="" class="control-text" data-rules="{required:true}" style="width: 200px;">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span20">
          <label class="control-label" style="width: 150px;"><s>*</s>ZOOKEEPER集群：</label>
          <div class="controls" id="zookeeperCluserCode">
            <input id="zookeeperCluserCodeHide" name="zookeeperCluster.zookeeperCode" type="hidden" value="" data-rules="{required:true}">
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="form-actions offset3">
          <button type="button" class="button button-primary" id="subButton">保存</button>
          <button type="button" class="button" id="cancelButton">取消</button>
        </div>
      </div>
    </form>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">	
  	$("#cancelButton").click(function(){
  		location.href="${pageContext.request.contextPath}/content/etl/server_farm/cluserList.jsp";
  	});

  	$("#subButton").click(function(){
		setTimeout(function(){bfh.submit();},100);
  	});
  
  	var bfh = new BUI.Form.HForm({
        srcNode : '#SC_Form',
        submitType : 'ajax',
        callback : function(data){
        	if(data==1){
        		BUI.Message.Alert("集群新建成功!",function(){
					location.href="${pageContext.request.contextPath}/content/etl/server_farm/cluserList.jsp";
				},"success");
        		//history.back();
        	}else{
            	var tip = data;
            	if ("101" == data) {
            		tip = "非法的操作！";
            	} else if ("103" == data) {
            		tip = "跟目录为空";
            	} else if ("104" == data) {
            		tip = "集群名称为空";
            	} else if ("105" == data) {
            		tip = "ZOOKEEPER集群为空";
            	} else if ("2" == data) {
                	tip = "存在同名集群";
            	}
        		BUI.Message.Alert("集群新建失败!<br>失败信息："+tip,function(){},"error");
        	}
          
        }
     }).render().on("beforesubmit",function(){
		if ("" == $("#clusterNameTip").text()) {
			return true;
		}
		return false;
     });

  	 $(document).ready(function(){
  		var select = new BUI.Select.Select({
	          render:'#zookeeperCluserCode',
	          valueField:'#zookeeperCluserCodeHide',
	          width:210,
	          store:new BUI.Data.Store({
			      //url : '${pageContext.request.contextPath}/content/etl/server_farm/json/zookeeperCluser.json',
			      url : '${pageContext.request.contextPath}/etl/cluser/queryZookeeperCluserList.do',
			      autoLoad : true
			    })
	        });
	    select.render();
  	 });

  	 function checkExistClusterName(obj) {
		var objV = $(obj).val();
		if ("" == objV) {
			$("#clusterNameTip").text("");
			return;
		}
		$.ajax({
			url:"${pageContext.request.contextPath}/etl/cluser/checkExistClusterName.do",
			type:"POST",
			data:{"clusterName":objV},
			success:function(r) {
				if ("2" == r) {
					$("#clusterNameTip").text("集群名称重复");
				} else {
					$("#clusterNameTip").text("");
				}
			},
			error:function(e){}
		});
  	 }   
  </script>

<body>
</html> 