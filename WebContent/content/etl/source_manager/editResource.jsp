<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>编辑资源</title>
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
    <form id ="J_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/resource/update.do" method="post">
      <input name="resourceId" value="${editObj.resourceId}" type="hidden" />
      <!--    -->
      <h3>公共信息</h3>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label"><s>*</s>资源类型：</label>
          <div class="controls" id="resourceType">
          	<input type="hidden" id="resourceTypeHide" value="${editObj.resourceType.resourceTypeId}" name="resourceType.resourceTypeId">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label"><s>*</s>资源名称：</label>
          <div class="controls">
            <input name="resourceName" type="text" value="${editObj.resourceName}" class="control-text" data-rules="{required:true}">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label"><s>*</s>主机名：</label>
          <div class="controls">
            <input name="hostName" type="text" value="${editObj.hostName}" class="control-text" data-rules="{required:true}">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label">资源编码：</label>
          <div class="controls">
            <input name="resourceEncoding" type="text" value="${editObj.resourceEncoding}" class="control-text">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label"><s>*</s>用户名：</label>
          <div class="controls">
            <input name="userName" type="text" value="${editObj.userName}" class="control-text" data-rules="{required:true}">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label"><s>*</s>密码：</label>
          <div class="controls">
            <input name="password" type="text" value="${editObj.password}" class="control-text" data-rules="{required:true}">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label"><s>*</s>端口：</label>
          <div class="controls">
            <input name="port" type="text" value="${editObj.port}" class="control-text" data-rules="{required:true}">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label">创建人：</label>
          <div class="controls">
            <input name="createOp" type="text" value="${editObj.createOp}" class="control-text">
          </div>
        </div>
      </div>
      
      <hr/>
      <h3>FTP信息</h3>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label">资源模式：</label>
          <div class="controls" id="resourceMode">
            <input type="hidden" id="resourceModeHide" value="${editObj.resourceMode}" name="resourceMode">
          </div>
        </div>
      </div>
      <hr/>

      <h3>数据库信息</h3>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label">URI：</label>
          <div class="controls">
            <input name="uri" type="text" value="${editObj.uri}" class="control-text span8">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label">服务名：</label>
          <div class="controls">
            <input name="serviceName" type="text" value="${editObj.serviceName}" class="control-text">
          </div>
        </div>
      </div>
      <div class="row">
        <div class="control-group span12">
          <label class="control-label">驱动名称：</label>
          <div class="controls">
            <input name="driver" type="text" value="${editObj.driver}" class="control-text span8">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label">最大活动数：</label>
          <div class="controls">
            <input name="maxActive" type="text" value="${editObj.maxActive}" class="control-text">
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="control-group span12">
          <label class="control-label">最大等待数：</label>
          <div class="controls">
            <input name="maxWait" type="text" value="${editObj.maxWait}" class="control-text">
          </div>
        </div>
        <div class="control-group span12">
          <label class="control-label">最大闲置数：</label>
          <div class="controls">
            <input name="maxIdle" type="text" value="${editObj.maxIdle}" class="control-text">
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="form-actions offset3">
          <button type="submit" class="button button-primary">保存</button>
          <button type="button" class="button" id="cancelButton">取消</button>
        </div>
      </div>
    </form>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
    var resourceType = new BUI.Select.Select({
        render:'#resourceType',
        valueField:'#resourceTypeHide',
        store:new BUI.Data.Store({
	      url : '${pageContext.request.contextPath}/etl/resource/qryresource.do',
	      autoLoad : true
	    })
    });
  	resourceType.render();
  	
  	var resourceMode=new BUI.Select.Select({
        render:'#resourceMode',
        valueField:'#resourceModeHide',
        items:[
         {text:'ACTIVE',value:'ACTIVE'},
         {text:'PASSIVE',value:'PASSIVE'}
       ]
    });
  	resourceMode.render();
  	
  	jQuery("#cancelButton").click(function(){
  		location.href="${pageContext.request.contextPath}/content/etl/source_manager/resourceList.jsp";
  	});
  	
  	 new BUI.Form.HForm({
        srcNode : '#J_Form',
        submitType : 'ajax',
        callback : function(data){
        	if(data==1){
        		BUI.Message.Alert("保存成功!",function(){
					location.href="${pageContext.request.contextPath}/content/etl/source_manager/resourceList.jsp";
				},"success");
        		//history.back();
        	}else if(data==2){
        		BUI.Message.Alert("保存失败!",function(){},"error");
        	}else if(data==3){
        		BUI.Message.Alert("资源名称已存在!",function(){},"error");
        	}else{
        		
        	}
          
        }
     }).render();
  </script>

</body>
</html> 