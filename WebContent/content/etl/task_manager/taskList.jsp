<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>任务管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/page-min.css" rel="stylesheet" type="text/css" />  
   
 </head>
 <body>
  <div style="padding: 15px 30px;" class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal">
        <div class="row">
          <div class="control-group span12">
            <label class="control-label">任务名称：</label>
            <div class="controls">
              <input type="text" class="control-text" name="name" value="">
            </div>
          </div>
          <div class="control-group span12">
            <label class="control-label">集群：</label>
            <div class="controls" id="serverCluser">
              <input type="hidden" id="serverCluserHide" value="" name="serverCluster">
            </div>
          </div>
        </div>
        <div class="row">
         <div class="control-group span12">
            <label class="control-label">是否在用：</label>
            <div class="controls" id="sceneState">
              <input type="hidden" id="sceneStateHide" value="" name="sceneState">
            </div>
          </div>
          <div class="control-group span12">
            <label class="control-label">描述：</label>
            <div class="controls">
              <input type="text" class="control-text" name="notes" value="">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span12">
            <label class="control-label">运行状态：</label>
            <div class="controls" id="taskStatus">
              <input type="hidden" id="taskStatusHide" value="" name="taskStatus">
            </div>
          </div>
           <div class="control-group span12">
            <label class="control-label">运行结果：</label>
            <div class="controls">
              <input type="text" class="control-text" name="taskResult" value="">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span12">
            <label class="control-label">是否启动：</label>
            <div class="controls" id="startStatus">
              <input type="hidden" id="startStatusHide" value="" name="startStatus">
            </div>
          </div>
          <div class="control-group span12">
            <label class="control-label">最近执行时间：</label>
            <div class="controls">
              <input style="width: 145px" type="text" class="calendar calendar-time" dateMask="yyyy-MM-dd HH:mm:ss" name="beginTime">
                                          至
              <input style="width: 145px" type="text" class="calendar calendar-time" dateMask="yyyy-MM-dd HH:mm:ss" name="endTime">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="span12 offset2">
            <button style="text-align: right" type="button" id="addTask" class="button button-primary">增加任务</button>
            &nbsp;&nbsp;
            <button style="text-align: right" type="button" id="queryTask" class="button button-primary">搜索</button>
          </div>
        </div>
      </form>
    </div>
    <div class="search-grid-container">
      <div id="grid"></div>
    </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  	var Grid = BUI.Grid,
  		Store = BUI.Data.Store,
  		Select=BUI.Select,
  		columns = [
            { title: '任务名称',width:100,dataIndex: 'scene', renderer:function(value,obj){
            	   if (!!value) {
				      return value.name;
				   }
	            }
            },
            { title: '任务描述',width:100,dataIndex: 'scene', renderer:function(value,obj){
           	   if (!!value) {
  				      return value.notes;
  				   }
  	            }
            },
            { title: '是否在用',width:45,dataIndex: 'scene', renderer:function(value,obj){
         	   if (!!value) {
	         		  if("0" == value.sceneStatus){
	         			   return "在用";
	         		  }
	         		  if("1" == value.sceneStatus){
	         			   return "停用";
	         		   }
				   }
	            }
            },
            { title: '是否启动',width:45,dataIndex: 'scene', renderer:function(value,obj){
          	       if (!!value) {
 	         		  if(value.startStatus == "1"){
 	         			  return "未启动";
 	         		  }else if(value.startStatus == "2"){
 	         			  return "启动中";
 	         		  }else if(value.startStatus == "3"){
 	         			  return "启动失败" + ":" + value.startLog;
 	         		  }else if(value.startStatus == "4"){
 	         			  return "已启动";
 	         		  }
 				   }
 	            }
             },
            { title: '集群',width:70,dataIndex: 'scene', renderer:function(value,obj){
          	   if (!!value && !!value.serverCluster) {
 				      return value.serverCluster.serverClusterName;
 				   }
 	            }
            },
            { title: '最近执行时间',width:90,dataIndex: 'dispatchTime', renderer:Grid.Format.datetimeRenderer},
            { title: '运行状态',width:50, dataIndex: 'taskStatus', renderer:function(value,obj){
         		  if(value == '0'){
         			   return "正在运行";
         		  }else if(value == '1'){
         			   return "用户请求停止";
         		  }else if(value == '2'){
       			   return "异常停止";
       		     }else if(value == '3'){
      			   return "运行成功";
      		     }else if(value == '4'){
      			   return "进程僵死";
      		     }else if(value == '5'){
      			   return "执行超时";
      		     }
	           }
            },
            { title: '运行结果',width:100, dataIndex: 'taskResult'},
            { title: '周期',width:70,dataIndex: 'scene', renderer:function(value,obj){
           	   if (!!value) {
  				      return value.croneExpression;
  				   }
  	            }
            },
            { title: '操作', width: 120, dataIndex: 'scene',renderer:function(v,obj){
            
            	var editTask = '<span class="grid-command btn-editTask" title="编辑资源">编辑</span>',
            	editStep='<span class="grid-command btn-editStep">步骤编辑</span>',
            	hisStr='<span class="grid-command btn-taskHistory">历史</span>',
            	singleRun='<span class="grid-command btn-singleRun">单次运行</span>',
            	timedRun='<span class="grid-command btn-timedRun">定时运行</span>',
            	stop='<span class="grid-command btn-stop">终止</span>',
            	delTask='<span class="grid-command btn-delTask">删除</span>';
            	
            	if(v.startStatus!="4"){
            		return editTask+editStep+hisStr+singleRun+timedRun+delTask;
            	}
            	if(v.startStatus=="4"){
            		return editTask+editStep+hisStr+singleRun+stop+delTask;
            	}
            }}
          ];
    var store = new Store({
            url : '${pageContext.request.contextPath}/etl/task/queryTaskByPage.do',
            autoLoad:false,
            pageSize:10
          }),
        grid = new Grid.Grid({
            render:'#grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            plugins : [], //勾选插件、自适应宽度插件
            // 底部工具栏
            tbar:{
             
            },
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
              pagingBar:true
            }
        });
 
        grid.render();
        
        //编辑、步骤编辑等按钮操作
	    grid.on('cellclick',function(ev){
	      var sender = $(ev.domTarget); //点击的Dom
	      if(sender.hasClass('btn-editStep')){
	        var record = ev.record;
	        location.href='${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId='+record.scene.sceneId;
	      }
	      if(sender.hasClass('btn-editTask')){
	        var record = ev.record;
	        location.href='${pageContext.request.contextPath}/etl/task/toEditTask.do?sceneId='+record.scene.sceneId;
	      }
	      // 历史
	      if(sender.hasClass('btn-taskHistory')){
		        var record = ev.record;
		        location.href='${pageContext.request.contextPath}/etl/task/toqueryTaskHistory.do?sceneId='+record.scene.sceneId;
		  }
	      // 删除
	      if(sender.hasClass('btn-delTask')){
		        var record = ev.record;
		        delFunction(record.scene.sceneId);
		        //location.href='${pageContext.request.contextPath}/etl/task/deleteTask.do?sceneId='+record.scene.sceneId;
		  }
	      // 单次运行
	      if(sender.hasClass('btn-singleRun')){
		        var record = ev.record;
		        singleRunFunction(record.scene.sceneId);
		  }
	      // 定时运行
	      if(sender.hasClass('btn-timedRun')){
		        var record = ev.record;
		        timedRunFunction(record.scene.sceneId);
		  }
	     // 终止
	      if(sender.hasClass('btn-stop')){
		        var record = ev.record;
		        stopFunction(record.scene.sceneId);
		  }
	    });
        
        // 删除
	    function delFunction(sceneId){
	        BUI.Message.Confirm('确认要删除选中的记录么？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/task/deleteTask.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{sceneId:sceneId},
	            success : function(data){
	              if(data == 1){ //删除成功
	              	BUI.Message.Alert("删除成功",function(){
	              		form.submit();
	              	},"success");
	              }else if(data == -1){ //删除失败
	            	  BUI.Message.Alert("任务删除失败,请先删除对应的步骤!","error");
	              }else if(data == -2){
	            	  BUI.Message.Alert("任务已启动，请先终止任务!","error");
	              } else{
	            	  BUI.Message.Alert("任务删除失败!","error");
	              }
	            }
	        });
	        },'question');
		    }
        
	     // 单次运行
	     function singleRunFunction(sceneId){
	        BUI.Message.Confirm('确认执行单次运行？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/task/singleRun.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{sceneId:sceneId},
	            success : function(data){
	              if(data == 1){ 
	              	BUI.Message.Alert("单次执行设置成功!",function(){
	              		form.submit();
	              	},"success");
	              }else{
	            	  BUI.Message.Alert("单次执行设置失败!","error");
	              }
	            }
	        });
	        },'question');
		    }
	     
	     // 定时运行
	     function timedRunFunction(sceneId){
	        BUI.Message.Confirm('确认执行定时运行？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/task/timedRun.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{sceneId:sceneId},
	            success : function(data){
	              if(data == 1){ 
	              	BUI.Message.Alert("定时执行设置成功!",function(){
	              		form.submit();
	              	},"success");
	              }else{
	            	  BUI.Message.Alert("定时执行设置失败!","error");
	              }
	            }
	        });
	        },'question');
		 }
	     
	     // 终止
	     function stopFunction(sceneId){
	        BUI.Message.Confirm('确认执行终止？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/task/stop.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{sceneId:sceneId},
	            success : function(data){
	              if(data == 1){ 
	              	BUI.Message.Alert("终止成功!",function(){
	              		form.submit();
	              	},"success");
	              }else{
	            	  BUI.Message.Alert("终止失败!","error");
	              }
	            }
	        });
	        },'question');
		}
	    
	    // 集群下拉
	    var selectCluster = new Select.Select({
	          render:'#serverCluser',
	          valueField:'#serverCluserHide',
	          store:new BUI.Data.Store({
			      url : '${pageContext.request.contextPath}/etl/node/queryServerClusterList.do',
			      autoLoad : true,
			    })
	        });
	    selectCluster.render();
    	
    	// 状态选择下拉
    	var selectState = new Select.Select({
            render:'#sceneState',
            valueField:'#sceneStateHide',
            items:[
   	          {text:'在用',value:'0'},
   	          {text:'停用',value:'1'}
   	        ]
           });
    	selectState.render();
    	
    	// 运行结果下拉
    	var selectResult = new Select.Select({
            render:'#taskStatus',
            valueField:'#taskStatusHide',
            items:[
   	          {text:'正在运行',value:'0'},
   	          {text:'用户请求停止',value:'1'},
   	          {text:'异常停止',value:'2'},
   	          {text:'运行成功',value:'3'},
   	          {text:'进程僵死',value:'4'},
   	          {text:'执行超时',value:'5'}
   	        ]
           });
    	selectResult.render();
    	
    	// 是否启动下拉
    	var selectStartStatus = new Select.Select({
            render:'#startStatus',
            valueField:'#startStatusHide',
            items:[
   	          {text:'未启动',value:'1'},
   	          {text:'启动中',value:'2'},
   	          {text:'启动失败',value:'3'},
   	          {text:'已启动',value:'4'}
   	        ]
           });
    	selectStartStatus.render();
 		
        //创建表单，表单中的日历，不需要单独初始化
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
        
        jQuery("#queryTask").click(function(){
        	form.submit();
        });
        
        jQuery("#addTask").click(function(){
        	location.href='${pageContext.request.contextPath}/content/etl/task_manager/addTask.jsp';
        });
        
        form.submit();
  </script>
 <body>
</html>  
