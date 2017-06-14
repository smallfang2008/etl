<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>步骤新增</title>
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
  	<h3>步骤新增</h3>
  	<hr>
	<form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/procedure/addStepfx.do" method="post">
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">步骤类型：</label>
				<div class="controls">
					<input name="scene.sceneId" type="hidden" value="${param.rwid }" />
					<input name="stepType.stepTypeId" type="hidden" value="${param.lxid }" /><span class="detail-text"> ${sessionScope.steptypemap[param.lxid] }</span>
				</div>
			</div>
			<div class="control-group span16">
				<label class="control-label"><s>*</s>步骤序号：</label>
				<div class="controls">
					<input name="step" type="text" data-rules="{required:true,number:true,bh:'${param.index }'}" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span16">
				<label class="control-label"><s>*</s>上一步：</label>
				<div class="controls bui-form-group"  style="width: 480px;height: auto;" id="previousStepid" data-rules="{checkRange:1}" data-messages="{checkRange:'至少勾选一项！'}" >
		        </div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label"><s>*</s>步骤名称：</label>
				<div class="controls">
					<input name="stepName" type="text" data-rules="{required:true}" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label"><s>*</s>运行位置类型：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwzlx" name="runPositionType" onchange="loadwz()">
						<option value="1">服务器集群</option>
						<option value="2">指定服务器</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label"><s>*</s>运行位置：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwz" name="type">
					</select>
					<input type="hidden" id="yxwza" value="" name=""/>
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">源类型：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="yxwzlx" name="sourceType" onchange="loadwz()">
						<option value="0">追加</option>
						<option value="1" selected="selected">不追加</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span16">
				<label class="control-label">压缩格式：</label>
				<div class="controls">
					<select data-rules="{required:true}" id="ysgs" name="compressPattern" onchange="loadwz()">
						<option value="0">不压缩</option>
						<option value="1">gz压缩</option>
						<option value="2">tar.gz压缩</option>

					</select>
				</div>
			</div>
		</div>
		<div class="row">
        	<div class="span20 offset3 control-row-auto">
          		<div id="grid"></div>
	          	<input type="hidden" name="listSourceFileDir"  id="listSourceFileDir">
	          	<input type="hidden" name="ids" id="ids">
	        </div>
      	</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">内存最大值：</label>
				<div class="controls">
					<input name="memMax" type="text" value="128" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">内存最小值：</label>
				<div class="controls">
					<input name="memMin" type="text" value="64" class="input-normal control-text">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">并发量：</label>
				<div class="controls">
					<input name="threadNum" type="text" value="1" class="input-normal control-text">
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">运行方式：</label>
				<div class="controls">
					<select name="runType">
						<option value="0">进程</option>
<!--						<option value="1">线程</option>-->
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="control-group span8">
				<label class="control-label">是否等待前置步骤执行完成：</label>
				<div class="controls">
					<select name="isWaitPre">
						<option value="0" selected="selected">否</option>
						<option value="1">是</option>
					</select>
				</div>
			</div>
			<div class="control-group span8">
				<label class="control-label">创建人：</label>
				<div class="controls">
					<input name="createOp" type="text" class="input-normal control-text"/>
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
  		location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=${param.rwid }";
  	});
  	
  	function loadwz(){
  		var lx = $("#yxwzlx").val();
  		var wz = $("#yxwz");
  		var wza = $("#yxwza");
  		if (lx == 1){
  			wz.attr("name","serverCluster.serverClusterId");
  			wza.attr("name","node.nodeCode");
  		} else {
  			wz.attr("name","node.nodeCode");
  			wza.attr("name","serverCluster.serverClusterId");
  		}
  		var data = lxdata[lx];
  		wz.html("");
		for(var i = 0; i < data.length; i++){
			wz.append('<option value="'+data[i].value+'">'+data[i].text+'</option>');
		}	
  	}
  	
  	function conteion(str,arr){
 		for (var i = 0; i < arr.length; i++){
 			if (str == arr[i]) {
 				return true;
 			}
 		}
 		return false;
 	}
  	
  	function tables(){
  		var columns = [
  		            { title: '源服务器',width:220,dataIndex: 'serverName',editor : {xtype :'select',items : enumObj}, renderer:Grid.Format.enumRenderer(enumObj)},
  		            { title: '根目录',width:150,dataIndex: 'filePath',editor:{xtype : 'text'}},
  		            { title: '子目录通配符',width:150,dataIndex: 'filePathPattern',editor:{xtype : 'text'}},
  		            { title: '源文件通配符',width:150,dataIndex: 'filePattern',editor:{xtype : 'text'}},
  		            { title: '开始时间',width:150,dataIndex: 'startDate',editor : {xtype : 'date',datePicker : {showTime : true}},renderer : Grid.Format.datetimeRenderer},
  		            { title: '操作', width: 150,renderer:function(value,obj){
  		            	var editStr = '<span class="grid-command btn-del">删除</span>';
  		              	return editStr;
  		            }}
  		          ];
  		/*var store = new Store({
            url : '${pageContext.request.contextPath}/etl/procedure/queryProcedure.do?sceneId=${scene.sceneId}',
            autoLoad:true,
          });*/
  		store = new Store({
            data : []
          });
  		var editing = new Grid.Plugins.CellEditing();
      var grid = new Grid.Grid({
            render:'#grid',
            loadMask: false,
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
            }/*,
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
             // pagingBar:true
            }*/
        });
 
        grid.render();
        
        grid.on('cellclick',function(ev){
  	      var sender = $(ev.domTarget); //点击的Dom
  	      if(sender.hasClass('btn-del')){
  	    	var selections = ev.record;
  	    	/*var ids = $("#ids").val() + (selections[0]["sourceFileDirId"] == "" ? "" : (","+selections[0]["sourceFileDirId"]));
  	    	$("#ids").val(ids);*/
  	      	store.remove(selections);
  	      }
  	    });
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
  			data:{sceneId:'${param.rwid }'},
  			dataType:'json',
  			success:function(data){
  				var html = '<label class="checkbox"><input name="previousStep" type="checkbox" value="-1"/>无</label>';
  				for (var i = 0; i < data.length; i++) {
					html = html + '<label class="checkbox"><input name="previousStep" type="checkbox" class="bui-form-field-checkbox bui-form-check-field bui-form-field" aria-disabled="false" value="'+data[i].STEP_TYPE_ID+'" />'+data[i].STEP_TYPE_NAME+'</label>';
				}
  				/*
  				if(data.length == 0){
  					html = '<label class="checkbox"><input name="previousStep" type="checkbox" value="-1" checked="checked"/>无</label>';
  				} else {
  					html = html + '<label class="checkbox"><input name="previousStep" type="checkbox" value="-1"/>无</label>';
  				}*/
  				$("#previousStepid").html(html);
  				BUI.use('bui/form',function(Form){
  					Form.Rules.add({
  				        name : 'bh',  //规则名称
  				        msg : '请填写除{0}数字以外的编号。',//默认显示的错误信息
  				        validator : function(value,baseValue,formatMsg){ //验证函数，验证值、基准值、格式化后的错误信息
  				        	  var bv = baseValue.split(',')
	  				          if(value && conteion(value,bv)){
	  				            return formatMsg;
	  				          }
  				        }
  				      }); 
	  				var N_Form = new Form.HForm({
	  			        srcNode : '#N_Form',
	  			        type:'post',
	  			        submitType : 'ajax',
	  			        callback : function(data){
	  			        	if(data=="1"){
	  			        		BUI.Message.Alert("步骤创建成功!",function(){
	  			        			location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=${param.rwid }";
	  							},"success");
	  			        		//history.back();
	  			        	}else{
	  			        		BUI.Message.Alert("步骤创建失败!<br>",function(){},"error");
	  			        	}
	  			          
	  			        }
	  			     }).render();
	  				N_Form.on('beforesubmit',function(){
	  		    	  var records = store.getResult();
	  		          $("#listSourceFileDir").val(BUI.JSON.stringify(records));
	  		      	});
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
  	 });
 	
  </script>

<body>
</html> 