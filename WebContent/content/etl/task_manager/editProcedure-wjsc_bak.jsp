<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>步骤修改</title>
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
	<form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/procedure/editStepwjsc.do" method="post">
  		<h3>基础配置</h3>
		<div class="row">
			<div class="control-group span10">
				<label class="control-label">步骤类型：</label>
				<div class="controls">
					<input name="scene.sceneId" type="hidden" value="${step.scene.sceneId }" />
					<input name="stepId" type="hidden" value="${step.stepId }" />
					<input name="stepType.stepTypeId" type="hidden" value="${step.stepType.stepTypeId }" /><span class="detail-text"> ${step.stepType.stepTypeName }</span>
				</div>
			</div>
			<div class="control-group span10">
				<label class="control-label"><s>*</s>步骤序号：</label>
				<div class="controls">
					<input name="step" type="text" value="${step.step }" data-rules="{required:true}" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label"><s>*</s>上一步：</label>
				<div class="bui-form-group controls" id="previousStepid" style="width: 480px;height: auto;" data-rules="{checkRange:[1,4]}" data-messages="{checkRange:'至少选一个'}">
					
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span10">
				<label class="control-label"><s>*</s>步骤名称：</label>
				<div class="controls">
					<input name="stepName" type="text" value="${step.stepName }" data-rules="{required:true}" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span10">
				<label class="control-label"><s>*</s>运行位置类型：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwzlx" name="runPositionType" onchange="loadwz()">
						<option value="1" ${step.runPositionType == 1 ?"selected='selected'":"" }>服务器集群</option>
						<option value="2" ${step.runPositionType == 2 ?"selected='selected'":"" }>指定服务器</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span10">
				<label class="control-label"><s>*</s>运行位置：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwz" name="type">
					</select>
					<input type="hidden" id="yxwza" value="" name=""/>
				</div>
			</div>
			<div class="control-group span10">
				<label class="control-label"><s>*</s>FTP资源选择：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="zylx" name="resourceName" style="width: auto;">
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label"><s>*</s>上传目录：</label>
				<div class="controls">
					<input name="upPath" id="scml" type="text" data-rules="{required:true}" class="input-normal control-text" />&nbsp;注：动态目录举例/dev/\${yyyyMMdd}/
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label">动态目录偏移量：</label>
				<div class="controls">
					<input name="upPathOffset" id="upPathOffset" type="text" value="0" data-rules="{number:true}" class="input-normal control-text">&nbsp;（天）
				</div>
			</div>
		</div>
  		<hr>
		<h3>文件名策略</h3>
		<div class="row">
			<div class="control-group span10">
				<label class="control-label">文件名追加日期：</label>
				<div class="controls">
					<select name="isAppendTime" id="isAppendTime">
						<option value="1">不追加</option>
						<option value="2">文件名前</option>
						<option value="3">文件名后缀前</option>
						<option value="4">文件名后</option>
					</select>
				</div>
			</div>
			<div class="control-group span10">
				<label class="control-label">文件名日期偏移量：</label>
				<div class="controls">
					<input name="appendTimeOffset" id="appendTimeOffset" data-rules="{number:true}" type="text" value="0" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label">文件名日期格式：</label>
				<div class="controls">
					<input name="appendTimePattern" id="appendTimePattern" type="text" value="" class="input-normal control-text">&nbsp;如：yyyyMMdd（不追加则不填）
				</div>
			</div>
		</div>
  		<hr>
		<h3>压缩策略</h3>
		<div class="row">
			<div class="control-group span16">
				<label class="control-label">是否压缩成GZ：</label>
				<div class="controls">
					<select name="isGz" id="isGz">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</div>
			</div>
		</div>
  		<hr><h3>备份策略（备份策略将遵从文件名策略和压缩策略）</h3>
  		<div class="row">
			
			<div class="control-group span20">
				<label class="control-label">是否备份：</label>
				<div class="controls">
					<select name="isBak" id="isBak">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="control-group span20">
				<label class="control-label">备份FTP：</label>
				<div class="controls">
					<select id="bakResourceName" name="bakResourceName" style="width: auto;">
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label">备份目录：</label>
				<div class="controls">
					<input name="bakUpPath" id="bakUpPath" type="text" class="input-normal control-text" />&nbsp;注：动态目录举例/dev/\${yyyyMMdd}/
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span20">
				<label class="control-label">备份目录偏移量：</label>
				<div class="controls">
					<input name="bakUpPathOffset" id="bakUpPathOffset" type="text" value="0" data-rules="{number:true}" class="input-normal control-text">&nbsp;（天）
				</div>
			</div>
		</div>
  		<hr>
		<h3>其他</h3>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">内存最大值：</label>
				<div class="controls">
					<input name="memMax" type="text" value="${step.memMax }" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">内存最小值：</label>
				<div class="controls">
					<input name="memMin" value="${step.memMin }" type="text" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">并发量：</label>
				<div class="controls">
					<input name="threadNum" data-rules="{number:true}" type="text" value="${step.threadNum }" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">运行方式：</label>
				<div class="controls">
					<select name="runType">
						<option value="0" ${step.runType == 0 ?"selected='selected'":"" }>进程</option>
						<option value="1" ${step.runType == 1 ?"selected='selected'":"" }>线程</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">是否等待前置步骤执行完成：</label>
				<div class="controls">
					<select name="isWaitPre">
						<option value="0" ${step.isWaitPre == 0 ?"selected='selected'":"" }>否</option>
						<option value="1" ${step.isWaitPre == 1 ?"selected='selected'":"" }>是</option>
					</select>
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">修改人：</label>
				<div class="controls">
					<input name="modifyOp" value="${step.modifyOp }" type="text" class="input-normal control-text"/>
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
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min-jerry.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
	var jldata = [];
  	$("#cancelButton").click(function(){
  		location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=${step.scene.sceneId }";
  	});
  	
  	function loadwz(){
  		var lx = $("#yxwzlx").val();
  		var wz = $("#yxwz");
  		var wza = $("#yxwza");
  		var value = '';
  		if (lx == 1){
  			wz.attr("name","serverCluster.serverClusterId");
  			wza.attr("name","node.nodeCode");
  			value = '${step.serverCluster.serverClusterId}';
  		} else {
  			wz.attr("name","node.nodeCode");
  			wza.attr("name","serverCluster.serverClusterId");
  			value = '${step.node.nodeCode}';
  		}
  		var data = lxdata[lx];
  		wz.html("");
		for(var i = 0; i < data.length; i++){
			wz.append('<option value="'+data[i].value+'" '+ (data[i].value == value?'selected="selected"':'') +'>'+data[i].text+'</option>');
		}	
  	}
  	
  	function contains(arrlist,obj){
  		var isTrue = false;
  		for (var o in arrlist) {
			if (arrlist[o] == obj) {
				isTrue = true;
				break;
			}
		}
  		return isTrue;
  	}
  	
  	function ftplist(value,bakResourceName){
  		$.ajax({
  			type:'post',
  			url:'${pageContext.request.contextPath}/etl/procedure/getFtpList.do',
  			dataType:'json',
  			success:function(data){
  				var zylx = $("#zylx");
  				var bakResourceName=$("#bakResourceName");
  				zylx.html("");
  				bakResourceName.html("");
  				for(var i = 0; i < data.length; i++){
  					zylx.append('<option value="'+data[i].NAME+'" '+ (data[i].NAME == value?'selected="selected"':'') +'>'+data[i].NAME+'</option>');
  					bakResourceName.append('<option value="'+data[i].NAME+'" '+ (data[i].NAME == bakResourceName?'selected="selected"':'') +'>'+data[i].NAME+'</option>');
  				}
  			}
  		});
  	}
  	
  	 $(document).ready(function(){
  		$.ajax({
  			type:'post',
  			url:'${pageContext.request.contextPath}/etl/procedure/queryStepNameList.do',
  			data:{sceneId:'${step.scene.sceneId }'},
  			dataType:'json',
  			success:function(data){
  				var html = "";
  				var stepId = '${step.stepType.stepTypeId}';
  				var stepPreviousStep = '${step.previousStep}'.split(',');
  				html = '<label class="checkbox"><input name="previousStep" type="checkbox" value="-1"  '+ (contains(stepPreviousStep,-1)?'checked="checked"':'') +'/>无</label>';
  				for (var i = 0; i < data.length; i++) {
  					//if(data[i].STEPTYPEID == stepId){
  						//continue;
  					//}
					html = html + '<label class="checkbox"><input name="previousStep" type="checkbox" '+ (contains(stepPreviousStep,data[i].STEP_TYPE_ID)?'checked="checked"':'') +' value="'+data[i].STEP_TYPE_ID+'" />'+data[i].STEP_TYPE_NAME+'</label>';
				}
  				$("#previousStepid").html(html);
  				var N_Form = new BUI.Form.HForm({
  			        srcNode : '#N_Form',
  			        submitType : 'ajax',
  			        callback : function(data){
  			        	if(data=="1"){
  			        		BUI.Message.Alert("步骤修改成功!",function(){
  			        			location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=${step.scene.sceneId }";
  							},"success");
  			        		//history.back();
  			        	}else{
  			        		BUI.Message.Alert("步骤修改失败!<br>",function(){},"error");
  			        	}
  			          
  			        }
  			     }).render();
  			}
  		});
  		
  		$.ajax({
  			type:'post',
  			url:'${pageContext.request.contextPath}/etl/procedure/queryyxlxList.do',
  			dataType:'json',
  			success:function(data){
  				lxdata = data;
  				loadwz();
  			}
  		});
  		
  		$.ajax({
  			type:'post',
  			url:'${pageContext.request.contextPath}/etl/procedure/getFtp.do?stepId=${step.stepId}',
  			dataType:'json',
  			success:function(data){
  				ftplist(data["resourceName"],data["bakResourceName"]);
  				$("#scml").val(data["upPath"]);
  				$("#upPathOffset").val(data["upPathOffset"]);
  				$("#isAppendTime").val(data["isAppendTime"]);
  				$("#appendTimePattern").val(data["appendTimePattern"]);
  				$("#appendTimeOffset").val(data["appendTimeOffset"]);
  				$("#isGz").val(data["isGz"]);
  			}
  		});
  				
  	 });
 
  </script>

<body>
</html> 