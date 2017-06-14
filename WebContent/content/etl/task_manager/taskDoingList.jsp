<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>正在运行列表</title>
    
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
		    max-height: 150px;
		    _height : 150px;
		  }
   	</style>
 </head>
 <body>
  
  <div class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal span24">
        <div class="row">
          <div class="control-group span8">
            <label class="control-label">任务名称：</label>
            <div class="controls">
              <input type="text" class="control-text" name="taskName" value="">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">步骤名称：</label>
            <div class="controls">
              <input type="text" class="control-text" name="stepName" value="">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">步骤类型：</label>
            <div class="controls" id="stepType">
          		<input type="hidden" id="stepTypeHide" value="" name="stepType">
          	</div>
          </div>
        </div>
        <div class="row">
          
          <div class="control-group span8">
            <label class="control-label">任务序列：</label>
            <div class="controls">
              <input type="text" class="control-text" name="taskSeries" value="">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">步骤序列：</label>
            <div class="controls" >
              <input type="text" class="control-text" name="stepSeries">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">上一步骤序列：</label>
            <div class="controls">
              <input type="text" class="control-text" name="prevStepSeries" value="">
            </div>
          </div>
        </div>
        <div class="row">
          
          <div class="control-group span8">
            <label class="control-label">服务器集群：</label>
            <div class="controls" id="serverCluserName">
              <input type="hidden" id="serverCluserNameHide" value="" name="serverClusterId">
            </div>
          </div>
          <div class="control-group span12">
            <label class="control-label">节点：</label>
            <div class="controls" >
              <input type="text" class="control-text" name="nodeCode">
            </div>
          </div>
          <div class="control-group span4">
            <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
          </div>
        </div>
      </form>
    </div>
    <div class="search-grid-container">
      <div id="grid"></div>
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
  	var Grid = BUI.Grid,
  		Store = BUI.Data.Store,
  		Select=BUI.Select,
  		columns = [
            { title: '步骤序列',width:80,dataIndex: 'cmdId',
            	renderer:function(value,obj){
            		
	            	var viewStr = '<span class="grid-command btn-edit" title="查看日志">'+value+'</span>';
	              return viewStr;
            	}
            },
            { title: '上一步骤序列', width:80, dataIndex: 'preCmdId'},
            { title: '任务序列', width:80, dataIndex: 'taskSeries'},
            //{ title: '步骤ID', width:80, dataIndex: 'stepId',display:'none'},
            { title: '步骤名称', width:80, dataIndex: 'stepName'},
            { title: '步骤类型', width:80, dataIndex: 'stepTypeName'},
            { title: '任务名称', width:100, dataIndex: 'sceneName'},
            { title: '运行位置', width:100, dataIndex: 'runPosition'},
            { title: '调度时间', width:100, dataIndex: 'dispatchDate'},
            { title: '开始时间', width:100, dataIndex: 'startDate'}/*,
            { title: '操作', width: 150, dataIndex: 'resourceId',renderer:function(value,obj){
            
            	var editStr = '<span class="grid-command btn-edit" title="编辑资源">编辑</span>',
            	delStr='<span class="grid-command btn-del">删除</span>';
            	//alert(openEditStr);
              return editStr+delStr;
            }}*/
          ];
    var store = new Store({
            url : '${pageContext.request.contextPath}/etl/task/taskDoingPageQuery.do',
            autoLoad:false,
            pageSize:10
          }),
        grid = new Grid.Grid({
            render:'#grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            plugins : [Grid.Plugins.CheckSelection], //勾选插件、自适应宽度插件,Grid.Plugins.AutoFit
            // 底部工具栏
            tbar:{
             items:[{text : '<i class="icon-remove"></i>全部停止',btnCls : 'button button-small',
             			handler:function(){
             				BUI.Message.Alert('暂未开发！',function(){},"info");
						}
					},
		            {text : '<i class="icon-remove"></i>停止',btnCls : 'button button-small',handler : stopTask}]
            },
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
              pagingBar:true
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
	        jQuery("#logTaskSeries").val(record.taskSeries),
	    	jQuery("#logStepSeries").val(record.cmdId),
	    	jQuery("#logNodeCode").val(record.runPosition),
	    	logDialog.show();
	        loadLog();
	      }
	    });
 		
 		 //停止任务
	    function stopTask(){
	      var selections = grid.getSelection();
	      stopItems(selections);
	    }
	    
	    function stopItems(items){
	      var ids = [];
	      BUI.each(items,function(item){
	        ids.push(item.stepId+"/"+item.taskSeries+"_"+item.cmdId);
	      });
	      if(ids.length){
	        BUI.Message.Confirm('确认要停止选中任务？',function(){
		          $.ajax({
		            url : '${pageContext.request.contextPath}/etl/task/stopDoingTask.do',
		            dataType : 'text',
		            method:"post",
		            cache: false,
		            data:{taskPath:ids+""},
		            success : function(data){
		              if(data==1){ //删除成功
		              	BUI.Message.Alert("停止成功",function(){
		              		form.submit();
		              	},"success");
		              }else{ //删除失败
		                BUI.Message.Alert('停止失败！',function(){},"error");
		              }
		            }
	        	});
	        },'question');
	      }
	    }
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
    	
    	var stepTypeSelect = new BUI.Select.Select({
	        render:'#stepType',
	        valueField:'#stepTypeHide',
	        store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/procedure/queryStepTypeList.do',
		      autoLoad : true,
	      		params : {"needAll":"1"}
		    })
	      });
    	stepTypeSelect.render();
    	
    	/*var stepTypeSelect = new BUI.Select.Suggest({
	        render:'#stepType',
	        valueField:'#stepTypeHide',
	        name:'stepType',
	        store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/procedure/queryStepTypeList.do',
		      autoLoad : true
		    })
	      });
    	stepTypeSelect.render();*/
    	var serverCluster = new Select.Select({
          render:'#serverCluserName',
          valueField:'#serverCluserNameHide',
          store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/node/queryServerClusterList.do',
		      autoLoad : true,
		      params : {"needAll":"1"}
		    })
        });
    	serverCluster.render();
 		
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
        
        jQuery("#btnSearch").click(function(){
        	form.submit();
        });
        
        form.submit();
        
        
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
</script>

</body>
</html>  
