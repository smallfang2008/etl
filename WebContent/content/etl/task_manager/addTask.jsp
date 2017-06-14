<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>任务管理-增加</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/page-min.css" rel="stylesheet" type="text/css" /> 
    <style type="text/css">
      .bui-select-list{
	    overflow: auto;
	    overflow-x: hidden;
	    max-height: 100px;
	    _height : 100px;
	  }
    </style>
 </head>
 <body>
  <div class="container">
    <h3>任务管理-增加</h3>
    <hr>
    <form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/task/addTask.do" method="post">
	    <div class="row">
	        <div class="row">
	          <div class="control-group span12">
	            <label class="control-label"><s>*</s>任务名称：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="name" value="" data-rules="{required:true}"><span style="color: red">(不允许中文)</span>
	            </div>
	          </div>
	          <div class="control-group span12">
	            <label class="control-label"><s>*</s>执行频率：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="croneExpression" value="" data-rules="{required:true}"><span style="color: red">如：1 1/5 * * * ?</span>
	            </div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span12">
	            <label class="control-label"><s>*</s>任务描述：</label>
	            <div class="controls control-row4">
			    	<textarea name="notes" class="input-large" type="text"></textarea>
			  	</div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span12">
	            <label class="control-label"><s>*</s>集群：</label>
	            <div class="controls" id="serverCluster">
	              <input type="hidden" id="serverClusterHide" value="" name="serverCluster.serverClusterId" data-rules="{required:true}">
	            </div>
	          </div>
	          <div class="control-group span12">
	            <label class="control-label">创建用户：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="creater" value="">
	            </div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span12">
	            <label class="control-label">对接人：</label>
	            <div class="controls">
	              <input type="text" class="control-text" name="linkman" value="">
	            </div>
	          </div>
	          <div class="control-group span12">
	            <label class="control-label">对接部门或系统：</label>
	            <div class="controls">
	            	<input type="text" class="control-text" name="linkSource" value="">
<!--	            	<input type="hidden" id="busiSysIdHide" value="" name="busiSysId">-->
<!--	              <input type="text" class="control-text" name="busiSys.busiSysId" value="" data-rules="{number:true}">-->
	            </div>
	          </div>
	        </div>
	        <div class="row">
	          <div class="control-group span12">
	            <label class="control-label"> 请选择步骤类型：</label>
	            <div class="controls" id="stepType">
	          		<input type="hidden" id="stepTypeHide" value="" name="stepType">
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
	      	if(data != "-1"){
	      		BUI.Message.Alert("增加任务成功!",function(){
				    //location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
	      			var vs = select2.getSelectedValue().split("j-");
	      			if(vs != null && vs != ""){
	              	  location.href="${pageContext.request.contextPath}/content/etl/task_manager/add"+vs[1] + ".jsp?lxid=" + vs[0] + "&rwid=" + data;
	      			}else{
	      			  location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
	      			}
					},"success");
	      		//history.back();
	      	}else{
	      		BUI.Message.Alert("增加任务失败!","error");
	      	}
	      }
	   }).render();
	
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
     
     var select2 = new BUI.Select.Select({
         render:'#stepType',
         valueField:'#stepTypeHide',
         width:250,
         store:new BUI.Data.Store({
 		      url : '${pageContext.request.contextPath}/etl/procedure/queryStepTypeList.do',
 		      autoLoad : true
 		    })
       });
     select2.render();
	  
	  N_Form.getField("name").addRule('regexp',/^[^\u4e00-\u9fa5]{0,}$/,'不允许中文');
  </script>
 <body>
</html>  
