<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>任务管理-编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/page-min.css" rel="stylesheet" type="text/css" />  
   <style type="text/css">
   	.input-large{
    		height:100px !important;
    		width:450px !important;
    		overflow:visible; 
    	}
    	.control-row6{
    		height:122px  !important;
    	}
   </style>
 </head>
 <body>
  <div class="container">
    <h3>任务管理-编辑</h3>
    <hr>
    <form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/task/editTask.do" method="post">
	    <div class="row">
	        <div class="row">
	          <div class="control-group span8">
	            <label class="control-label">任务名称：</label>
	            <div class="controls">
	              <input name="sceneId" value="${editObj.sceneId}" type="hidden" />
	              <input type="text" class="control-text" name="name" value="${editObj.name}">
	            </div>
	          </div>
	          <div class="control-group span8">
	            <label class="control-label">集群：</label>
	            <div class="controls" id="serverCluster">
	              <input type="hidden" id="serverClusterHide" value="${editObj.serverCluster.serverClusterId}" name="serverCluster.serverClusterId">
	            </div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span8">
	            <label class="control-label">周期：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="croneExpression" value="${editObj.croneExpression}">
	            </div>
	          </div>
	          <div class="control-group span8">
	            <label class="control-label">是否在用：</label>
	            <div class="controls" id="sceneState">
	              <input type="hidden" id="sceneStateHide" value="${editObj.sceneStatus }" name="sceneStatus">
	            </div>
              </div>
	        </div>
	        <div class="row">
	          <div class="control-group span16">
	            <label class="control-label">任务描述：</label>
	            <div class="controls control-row6">
			    	<textarea name="notes" class="input-large" type="text">${editObj.notes}</textarea>
			  	</div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span8">
	            <label class="control-label">对接人：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="linkman" value="${editObj.linkman}">
	            </div>
	          </div>
	          <div class="control-group span8">
	            <label class="control-label">对接部门或系统：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="linkSource" value="${editObj.linkSource}">
	            </div>
              </div>
	        </div>
	        <div class="row">
	          <div class="span12 offset2" align="center">
	            <button type="submit" id="saveButton" class="button button-primary">保存</button>
	            &nbsp;&nbsp;
	            <button type="button" id="cancelButton" class="button button-primary">取消</button>
	          </div>
	        </div>
	    </div>
	</form>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
	    
	    $("#cancelButton").click(function(){
		   location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
		});
	
		var N_Form = new BUI.Form.HForm({
	      srcNode : '#N_Form',
	      submitType : 'ajax',
	      callback : function(data){
	      	if(data=="1"){
	      		BUI.Message.Alert("任务编辑成功!",function(){
						location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
					},"success");
	      		//history.back();
	      	}else{
	      		BUI.Message.Alert("任务编辑失败!","error");
	      	}
	      }
	   }).render();
	
	  $(document).ready(function(){
		 var select1 = new BUI.Select.Select({
	          render:'#serverCluster',
	          valueField:'#serverClusterHide',
	          width:150,
	          store:new BUI.Data.Store({
			      url : '${pageContext.request.contextPath}/etl/node/queryServerClusterList.do',
			      autoLoad : true
			    })
	         });
	     select1.render();
	   });
	  
	// 状态选择下拉
  	var selectState = new BUI.Select.Select({
          render:'#sceneState',
          valueField:'#sceneStateHide',
          items:[
 	          {text:'在用',value:'0'},
 	          {text:'停用',value:'1'}
 	        ]
         });
  	selectState.render();
  </script>
 <body>
</html>  
