<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>编辑节点</title>
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
  	<h3>编辑节点</h3>
  	<hr>
    <form id="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/node/editNode.do" method="post">
      <input name="oldNodeCode" value="${editObj.nodeCode}" type="hidden" />
      <!--    -->
      <div class="row">
        <div class="control-group span13">
          <label class="control-label"><s>*</s>节点编码：</label>
          <div class="controls">
          	<c:if test="${!editFlag}">
          		<input name="nodeCode" type="text" value="${editObj.nodeCode}" class="control-text" data-rules="{required:true,maxlength:50}" readonly="readonly" style="width: 200px;background-color: #b3b1b2;"><font size="1">已被使用禁止修改</font>
          	</c:if>
          	<c:if test="${editFlag}">
          		<input name="nodeCode" type="text" value="${editObj.nodeCode}" class="control-text" data-rules="{required:true,maxlength:50}" style="width: 200px;" onblur="checkExistNodeCode(this);">
          	</c:if>
          </div>
          <label class="control-label" style="padding-left: 2px;text-align: left;"><s id="nodeCodeTip"></s></label>
        </div>
        <div class="control-group span13">
          <label class="control-label"><s>*</s>IP地址：</label>
          <div class="controls">
            <input name="ipAddress" type="text" value="${editObj.ipAddress}" class="control-text" data-rules="{required:true,maxlength:15}" style="width: 200px;">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span13">
          <label class="control-label"><s>*</s>FTP服务名称：</label>
          <div class="controls">
            <input name="serverName" type="text" value="${editObj.serverName}" class="control-text" data-rules="{required:true,maxlength:130}" style="width: 200px;">
          </div>
        </div>
        <div class="control-group span13">
          <label class="control-label"><s>*</s>日志目录：</label>
          <div class="controls">
            <input name="logPath" type="text" value="${editObj.logPath}" class="control-text" data-rules="{required:true,maxlength:130}" style="width: 200px;">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span13">
          <label class="control-label"><s>*</s>服务器集群：</label>
          <div class="controls" id="serverCluserName">
          	<input type="hidden" id="serverCluserNameHide" value="${editObj.serverCluster.serverClusterId}" name="serverCluster.serverClusterId" data-rules="{required:true}">
          </div>
        </div>
        <div class="control-group span13">
          <label class="control-label"><s>*</s>最大任务数：</label>
          <div class="controls">
            <input name="maxTasks" type="text" value="${editObj.maxTasks}" class="control-text" data-rules="{required:true,number:true,min:0,max:100}" style="width: 200px;">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span13">
          <label class="control-label">是否获取集群任务：</label>
          <div class="controls" id="getClusterTask">
          	<input type="hidden" id="getClusterTaskHide" value="${editObj.isGetClusterTask}" name="isGetClusterTask">
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
  		location.href="${pageContext.request.contextPath}/content/etl/work_node/nodeList.jsp";
  	});

  	$("#subButton").click(function(){
		setTimeout(function(){N_Form.submit();},100);
  	});
  	
  	var N_Form = new BUI.Form.HForm({
        srcNode : '#N_Form',
        submitType : 'ajax',
        callback : function(data){
        	if(data==1){
        		BUI.Message.Alert("节点编辑成功!",function(){
					location.href="${pageContext.request.contextPath}/content/etl/work_node/nodeList.jsp";
				},"success");
        		//history.back();
        	}else{
        		var tip = data;
            	if (2 == data)
                	tip = "IP地址输入不正确！";
            	else if (3 == data)
                	tip = "已存在节点编码！";
            	else if (101 == data || data == 107)
                	tip = "非法操作！";
            	else if (102 == data)
                	tip = "节点编码为空！";
            	else if (103 == data)
                	tip = "日志目录为空！";
            	else if (104 == data)
                	tip = "IP地址为空！";
            	else if (105 == data)
                	tip = "FTP服务名称为空！";
            	else if (106 == data)
                	tip = "服务器集群为空！";
        		BUI.Message.Alert("节点编辑失败!<br>失败信息："+tip,function(){},"error");
        	}
          
        }
     }).render().on("beforesubmit",function(){
		if ("" == $("#nodeCodeTip").text()) {
			return true;
		}
		return false;
     });

  	 $(document).ready(function(){
  		var select1 = new BUI.Select.Select({
	          render:'#serverCluserName',
	          valueField:'#serverCluserNameHide',
	          width:210,
	          store:new BUI.Data.Store({
			      url : '${pageContext.request.contextPath}/etl/node/queryServerClusterList.do',
			      autoLoad : true
			    })
	        });
	    select1.render();
	    //select.setSelectedValue("zookeeper1");
	    var select2 = new BUI.Select.Select({
	          render:'#getClusterTask',
	          valueField:'#getClusterTaskHide',
	          width:210,
	          items:[
	       			{text:'是',value:'1'},
	       			{text:'否',value:'0'}
	       			]
	        });
	    select2.render();
	    N_Form.getField("nodeCode").addRule('regexp',/^([\w0-9\._@\(\)\-])+$/,'只能输入英文字母、数字、点、下划线、横杆、@、小括号等字符');
	    N_Form.getField("maxTasks").addRule('regexp',/^\d+$/,'必须是整数');
	    N_Form.getField("ipAddress").addRule('regexp',/^(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/,'请输入正确的IP地址格式');
  	 });   

  	function checkExistNodeCode(obj){
  		var objV = $(obj).val();
		if ("" == objV || "${editObj.nodeCode}" == objV) {
			$("#nodeCodeTip").text("");
			return;
		}
		$.ajax({
			url:"${pageContext.request.contextPath}/etl/node/checkExistNodeCode.do",
			type:"POST",
			data:{"nodeCode":objV},
			success:function(r) {
				if ("2" == r) {
					$("#nodeCodeTip").text("节点编码重复");
				} else {
					$("#nodeCodeTip").text("");
				}
			},
			error:function(e){}
		});
  	 } 
  </script>

<body>
</html> 