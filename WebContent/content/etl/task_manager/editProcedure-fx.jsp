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
    <style>
    	.bui-select-list{
		    overflow: auto;
		    overflow-x: hidden;
		    max-height: 150px;
		    _height : 150px;
		 }
    </style> 
 </head>
 <body>
  
  <div class="container">
  	<h3>步骤修改</h3>
  	<hr>
	<form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/procedure/editStepfx.do" method="post">
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">步骤类型：</label>
				<div class="controls">
					<input name="scene.sceneId" type="hidden" value="${step.scene.sceneId }" />
					<input name="stepId" type="hidden" value="${step.stepId }" />
					<input name="stepType.stepTypeId" type="hidden" value="${step.stepType.stepTypeId }" /><span class="detail-text"> ${step.stepType.stepTypeName }</span>
				</div>
			</div>
			<div class="control-group span16">
				<label class="control-label"><s>*</s>步骤序号：</label>
				<div class="controls">
					<input name="step" type="text" value="${step.step }" data-rules="{required:true}" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span16">
				<label class="control-label"><s>*</s>上一步：</label>
				<div class="bui-form-group controls" id="previousStepid" style="width: 480px;height: auto;" data-rules="{checkRange:[1,4]}" data-messages="{checkRange:'至少选一个'}">
					
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label"><s>*</s>步骤名称：</label>
				<div class="controls">
					<input name="stepName" type="text" value="${step.stepName }" data-rules="{required:true}" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label"><s>*</s>运行位置类型：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwzlx" name="runPositionType"  onchange="loadwz()">
						<option value="1" ${step.runPositionType == 1 ?"selected='selected'":"" }>服务器集群</option>
						<option value="2" ${step.runPositionType == 2 ?"selected='selected'":"" }>指定服务器</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label"><s>*</s>运行位置：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwz" name="type" >
					</select>
					<input type="hidden" id="yxwza" value="" name=""/>
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">源类型：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="ylx" name="sourceType"  onchange="loadwz()">
						<option value="0">追加</option>
						<option value="1">不追加</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span16">
				<label class="control-label">压缩格式：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="ysgs" name="compressPattern"  onchange="loadwz()">
						<option value="0">不压缩</option>
						<option value="1">gz压缩</option>
						<option value="2">tar.gz压缩</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
        	<div class="span22 offset3 control-row-auto">
        		<div style="color: red;">
        			
					注：
					<br/>
					"清除扫描记录"将恢复到初次扫描
					<br/>
					转换记录记录有转换步骤清洗的字节数，在"清除扫描记录"之前，如无需处理相同的文件，保留转换记录，否则清除转换记录。
        		</div>
          		<div id="grid"></div>
	          	<input type="hidden" name="listSourceFileDir"  id="listSourceFileDir">
	          	<input type="hidden" name="ids" id="ids">
	        </div>
      	</div>
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
					<select name="runType" >
						<option value="0" ${step.runType == 0 ?"selected='selected'":"" }>进程</option>
<!--						<option value="1" ${step.runType == 1 ?"selected='selected'":"" }>线程</option>-->
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">是否等待前置步骤执行完成：</label>
				<div class="controls">
					<select name="isWaitPre" >
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
  	var enumObj = {};
  
	var Grid = BUI.Grid,
	Store = BUI.Data.Store,
	Select=BUI.Select;
	var store;
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
  	
  	function tables(){
  		var columns = [
  		            { title: '源服务器',width:180,dataIndex: 'serverName',editor : {xtype :'select',items : enumObj}, renderer:Grid.Format.enumRenderer(enumObj)},
  		            { title: '根目录',width:150,dataIndex: 'filePath',editor:{xtype : 'text'}},
  		            { title: '子目录通配符',width:150,dataIndex: 'filePathPattern',editor:{xtype : 'text'}},
  		            { title: '源文件通配符',width:150,dataIndex: 'filePattern',editor:{xtype : 'text'}},
  		            { title: '开始时间',width:150,dataIndex: 'startDate',editor : {xtype : 'date',datePicker : {showTime : true}},renderer : Grid.Format.datetimeRenderer},
  		            { title: '操作', width: 240,renderer:function(value,obj){
  		            	var editStr = '<span class="grid-command btn-del">删除</span>';
  		            	if(obj.sourceFileDirId){
  		            		editStr+='<span class="grid-command btn-del-scan">清除扫描记录</span><span class="grid-command btn-del-convert">清除转换记录</span>';
  		            		
  		            	}
  		              	return editStr;
  		            }}
  		          ];
  		store = new Store({
            url : '${pageContext.request.contextPath}/etl/procedure/querySource.do?stepId=${step.stepId}',
            autoLoad:true,
          });
  		var editing = new Grid.Plugins.CellEditing();
      var grid = new Grid.Grid({
            render:'#grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            plugins : [editing], //,Grid.Plugins.AutoFit
            // 底部工具栏
            tbar:{
             items:[{text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',
             			handler:function(){
             				var newData = {};
           			        store.add(newData);
           			        editing.edit(newData,'serverName');
						}
					}]
            },
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
             // pagingBar:true
            }
        });
 
        grid.render();
        
        grid.on('cellclick',function(ev){
  	      var sender = $(ev.domTarget); //点击的Dom
  	      if(sender.hasClass('btn-del')){
  	    	var selections = ev.record;
  	    	var ids = $("#ids").val() + (!(selections["sourceFileDirId"]) ? "" : (","+selections["sourceFileDirId"]));
  	    	$("#ids").val(ids);
  	      	store.remove(selections);
  	      }
  	      if(sender.hasClass('btn-del-scan')){
  	      	
  	    	var selections = ev.record;
  	    	var sourceFileDirId=selections["sourceFileDirId"];
  	    	if(sourceFileDirId){
  	    		BUI.Message.Confirm('确认清除扫描记录？',function(){
	  	    		$.ajax({
			  			type:'post',
			  			async: true,
			  			url:'${pageContext.request.contextPath}/etl/procedure/clearScanRecord.do',
			  			data:{sourceFileDirId:sourceFileDirId},
			  			dataType:'text',
			  			success:function(data){
			  				if(data=="1"){
	  			        		BUI.Message.Alert("清除成功!",function(){},"success");
	  			        		//history.back();
	  			        	}else{
	  			        		BUI.Message.Alert("清除失败!",function(){},"error");
	  			        	}
			  			}
			  		});
			  	},'question');
  	    	}else{
  	    		BUI.Message.Alert("无需清除",function(){},"error");
  	    	}
  	      }
  	      if(sender.hasClass('btn-del-convert')){
  	    	var selections = ev.record;
  	    	var sourceFileDirId=selections["sourceFileDirId"];
  	    	if(sourceFileDirId){
  	    		BUI.Message.Confirm('确认清除转换记录？',function(){
					$.ajax({
			  			type:'post',
			  			async: true,
			  			url:'${pageContext.request.contextPath}/etl/procedure/clearConvertRecord.do',
			  			data:{sourceFileDirId:sourceFileDirId,stepId:'${step.stepId}'},
			  			dataType:'text',
			  			success:function(data){
			  				if(data=="1"){
	  			        		BUI.Message.Alert("清除成功!",function(){},"success");
	  			        		
	  			        	}else{
	  			        		BUI.Message.Alert("清除失败!",function(){},"error");
	  			        	}
			  			}
			  		});
				},'question');
  	    		
  	    	}else{
  	    		BUI.Message.Alert("无需清除",function(){},"error");
  	    	}
  	      }
  	    });
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
  	
  	 $(document).ready(function(){
  		$.ajax({
  			type:'post',
  			async: false,
  			url:'${pageContext.request.contextPath}/etl/procedure/getfxList.do',
  			dataType:'json',
  			success:function(data){
  				for(var i = 0; i < data.length; i++){
  					enumObj[data[i].NAME] = data[i].NAME;
  				}
  				tables();
  			}
  		});
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
  				N_Form.on('beforesubmit',function(){
    		    	  var records = store.getResult();
    		          $("#listSourceFileDir").val(BUI.JSON.stringify(records));
    		      });
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
  			url:'${pageContext.request.contextPath}/etl/procedure/queryFinder.do?stepId=${step.stepId}',
  			dataType:'json',
  			success:function(data){
  				$("#ylx").val(data.sourceType);
  				$("#ysgs").val(data.compressPattern);
  			}
  		});
  				
  	 });
 
  </script>

<body>
</html> 