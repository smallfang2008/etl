<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>任务管理-历史-查看步骤</title>
    
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
     <h3>任务管理-历史-查看步骤</h3>
     <hr>
     <div>
       <button id="returnButton" class="button button-primary">返回</button>
       <button style="margin-right: 100px;" type="button" id="refresh" class="button button-primary">刷新</button>
     </div>
    <div class="search-grid-container">
      <div id="grid">
      </div>
    </div>
    
    <div id="logContent" class="hide">
      <form id="searchForm" class="form-horizontal span24">
   		<div class="row">
         
         <div class="control-group span8">
           <label class="control-label span2">编码：</label>
           <div class="controls" id="encoding">
             <input type="hidden" id="encodingHide" value="UTF-8" name="encoding">
           </div>
         </div>
         <div class="control-group span8">
           <label class="control-label">字节数：</label>
           <div class="controls" >
           	<input type="hidden" name="logStepSeries" id="logStepSeries">
           	<input type="hidden" name="logTaskSeries" id="logTaskSeries">
           	<input type="hidden" name="logNodeCode" id="logNodeCode">
           	
             <input type="text" class="control-text" id="logSize" name="size" value="8000">
           </div>
         </div>
         <div class="control-group span2">
           <button  type="button" id="btnRefresh" class="button button-primary">刷新</button>
         </div>
       </div>
       <div class="row">
       		<div class="bordered span-width span19" id="logInner" style="height: 300px;overflow-y:auto; ">
       			
       		</div>
       </div>
      </form>
       
    </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  
    $("#returnButton").click(function(){
    	location.href="${pageContext.request.contextPath}/etl/task/toqueryTaskHistory.do?sceneId=${sceneIns.scene.sceneId}";
	 });
    
    $("#refresh").click(function(){
    	//location.href="${pageContext.request.contextPath}/etl/task/queryTaskHistory.do?sceneId=${scene.sceneId}";
    	form.submit();
	 });
     
  	var Grid = BUI.Grid,
  		Store = BUI.Data.Store,
  		Select=BUI.Select,
  		columns = [
            { title: '步骤序号', width:60,dataIndex: 'step', renderer:function(v,o){
            	if(!!v){
             	   return v.step;
             	}
             }},
            { title: '步骤序列',  width:120, dataIndex: 'stepSeries', renderer:function(value,obj){
            	  var viewStr = '<span class="grid-command btn-edit" title="查看日志">'+value+'</span>';
                  return viewStr;
        	   }
            },
            { title: '任务序列',  width:120, dataIndex: 'sceneIns', renderer:function(v,o){
            	if(!!v){
               	   return v.taskSeries;
               	}
               }},
            { title: '任务名称',  width:80, dataIndex: 'sceneIns', renderer:function(v,o){
            	if(!!v && !!v.scene){
              	   return v.scene.name;
              	}
              }},
            { title: '步骤名称', width:80,dataIndex: 'step', renderer:function(v,o){
               	if(!!v){
              	   return v.stepName;
              	}
            }},
            { title: '步骤类型',  width:60,dataIndex: 'step', renderer:function(v,o){
            	if(!!v && !!v.stepType){
              	   return v.stepType.stepTypeName;
              	}
              }},
            { title: '运行位置', width:140,dataIndex: 'runPosition'},
            { title: '调度时间', width:140,dataIndex: 'dispatchTime', renderer:Grid.Format.datetimeRenderer},
            { title: '开始时间', width:140,dataIndex: 'startTime', renderer:Grid.Format.datetimeRenderer},
            { title: '结束时间', width:140,dataIndex: 'endTime', renderer:Grid.Format.datetimeRenderer},
            { title: '运行状态', width:80,dataIndex: 'status', renderer:function(v,o){
            	if(v == '0'){
      			   return "正在运行";
      		    }else if(v == '1'){
      			   return "用户请求停止";
      		    }else if(v == '2'){
    			   return "异常停止";
    		    }else if(v == '3'){
   			       return "运行成功";
   		        }else if(v == '4'){
   			      return "进程僵死";
   		        }else if(v == '5'){
   			      return "执行超时";
   		        }
            }},
            { title: '运行结果',  width:140,dataIndex: 'execInfo'}           
          ];
    var store = new Store({
    	url : '${pageContext.request.contextPath}/etl/task/queryStepHistory.do?taskSeries=${sceneIns.taskSeries}',
            autoLoad:true,
            pageSize:10
          }),
        grid = new Grid.Grid({
            render:'#grid',
            loadMask: true,
            //forceFit:true,
            columns : columns,
            store: store,
           // plugins : [], //勾选插件、自适应宽度插件
           
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
              pagingBar:true
            },itemStatusFields : { //设置数据跟状态的对应关系
                disabled : 'disabled',
                read : 'readed' //如果readed : true,则附加 bui-grid-row-read 样式
              }
        });
 
        grid.render();
        
        grid.on('cellclick',function(ev){
  	      var sender = $(ev.domTarget); //点击的Dom
  	      if(sender.hasClass('btn-del')){
  	        var record = ev.record;
  	        delItems([record]);
  	      }
  	      if(sender.hasClass('btn-edit')){
  	      	jQuery("#logInner").html("");
  	        var record = ev.record;
  	        jQuery("#logTaskSeries").val(record.sceneIns.taskSeries),
  	    	jQuery("#logStepSeries").val(record.stepSeries),
  	    	jQuery("#logNodeCode").val(record.runPosition),
  	    	logDialog.show();
  	        loadLog();
  	      }
  	    });
        
        var encodingSelect = new BUI.Select.Select({
	        render:'#encoding',
	        valueField:'#encodingHide',
	        //width:100,
	        items:[
	          {text:'UTF-8',value:'UTF-8'},
	          {text:'GBK',value:'GBK'},
	          {text:'ISO8859-1',value:'ISO8859-1'}
	        ]
	      });
    	encodingSelect.render();
        
        var logDialog = new BUI.Overlay.Dialog({
            title:'日志查看',
            width:800,
            height:400,
            contentId:'logContent',
            success:function () {
              this.close();
            }
          });
        
        
        function loadLog(){
        	jQuery.ajax({
	    		url:'${pageContext.request.contextPath}/etl/task/loadLog.do',
	    		type:'post',
	    		data:{
	    			taskSeries:jQuery("#logTaskSeries").val(),
	    			stepSeries:jQuery("#logStepSeries").val(),
	    			nodeCode:jQuery("#logNodeCode").val(),
	    			encoding:jQuery("#encodingHide").val(),
	    			size:jQuery("#logSize").val()
	    		},
	    		dataType:'text',
	    		success:function(data){
	    			jQuery("#logInner").html(data);
	    		}
	    	});
        }
        
        jQuery("#btnRefresh").click(function(){
        	loadLog();
        });
        
        var form = new BUI.Form.HForm({
            srcNode : '#searchForm'
          }).render();
   
          form.on('beforesubmit',function(ev) {
            //序列化成对象
            var obj = form.serializeToObject();
            obj.start = 0; //返回第一页
            store.load(obj);
            return false;
          });
        
        //form.submit();
  </script>
 <body>
</html>  
