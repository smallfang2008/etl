<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>任务管理-历史</title>
    
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
     <h3>任务管理-历史</h3>
     <hr>
    <div class="row">
      <form id="searchForm" class="form-horizontal span24">
         <div class="row span20">
	        <div class="control-group span10">
	           <label class="control-label">任务名称：</label><span class="detail-text">${scene.name}</span>
	        </div>
	        <div class="control-group span10">
	           <label class="control-label">集群：</label><span class="detail-text">${scene.serverCluster.serverClusterName}</span>
	        </div>
         </div>
        <div></div>
      </form>
    </div>
    <br>
    <br>
    <div>
       <button id="cancelButton" class="button button-primary">返回</button>
       <button type="button" id="refresh" class="button button-primary">刷新</button>
    </div>
    <div class="search-grid-container">
      <div id="grid"></div>
    </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  
    $("#cancelButton").click(function(){
    	location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
	 });
    
    $("#refresh").click(function(){
    	//location.href="${pageContext.request.contextPath}/etl/task/queryTaskHistory.do?sceneId=${scene.sceneId}";
    	form.submit();
	 });
     
  	var Grid = BUI.Grid,
  		Store = BUI.Data.Store,
  		Select=BUI.Select,
  		columns = [
            { title: '任务序列',width:80,dataIndex: 'taskSeries'},
            { title: '调度时间',width:80, dataIndex: 'dispatchTime', renderer:Grid.Format.datetimeRenderer},
            { title: '结束时间',width:80, dataIndex: 'endTime', renderer:Grid.Format.datetimeRenderer},
            { title: '运行状态',width:50,dataIndex: 'taskStatus', renderer:function(v,o){
            	if(v == 0){
            		return "正在运行";
            	}else if(v ==1){
            		return "用户请求停止";
            	}else if(v ==2){
            		return "异常停止";
            	}else if(v ==3){
            		return "运行成功";
            	}else if(v ==4){
            		return "进程僵死";
            	}else if(v ==5){
            		return "执行超时";
            	} 
            }},
            { title: '运行结果',width:100,dataIndex: 'taskResult'},
            { title: '周期', width: 60, dataIndex: 'scene', renderer:function(v,o){
            	if(!!v){
            		return v.croneExpression;
            	}
            }},
            { title: '操作', width: 60, dataIndex: 'nodeCode',renderer:function(value,obj){
            	var editStr = '<span class="grid-command btn-stepHistory" title="编辑节点">查看步骤</span>';
              return editStr;
            }}
          ];
    var store = new Store({
    	url : '${pageContext.request.contextPath}/etl/task/queryTaskHistory.do?sceneId=${scene.sceneId}',
            autoLoad:true,
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
            //tbar:{
            //},
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
              pagingBar:true
            }
        });
 
        grid.render();
        
        //查看步骤按钮
	    grid.on('cellclick',function(ev){
	      var sender = $(ev.domTarget); //点击的Dom
	      if(sender.hasClass('btn-stepHistory')){
	        var record = ev.record;
	        //location.href='${pageContext.request.contextPath}/content/etl/task_manager/stepHistory.jsp?taskSeries='+record.taskSeries+'&sceneId='+ record.scene.sceneId;
	        location.href='${pageContext.request.contextPath}/etl/task/toqueryStepHistory.do?taskSeries='+record.taskSeries;
	      }
	    });
 		
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
        
        //form.submit();
  </script>
 <body>
</html>  
