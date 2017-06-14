<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>步骤列表</title>
    
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
	    max-height: 100px;
	    _height : 100px;
	  }
	</style>
 </head>
 <body>
   
  <div style="padding: 0px 30px" class="container">
  	<h3>步骤列表</h3>
  	<hr>
    <div class="row">
        <div class="row span16">
          <div class="control-group span8">
            <label class="control-label">任务名称：</label><span class="detail-text">${scene.name}</span>
          </div>
          <div class="control-group span8">
            <label class="control-label">集群：</label><span class="detail-text">${scene.serverCluster.serverClusterName}</span>
          </div>
        </div>
     </div>
    <div class="row">
        <div class="row span16">
          <div class="control-group span8">
            <label class="control-label">周期：</label><span class="detail-text">${scene.croneExpression}</span>
          </div>
          <div class="control-group span8">
            <label class="control-label">任务描述：</label><span class="detail-text">${scene.notes }</span>
          </div>
        </div>
     </div>
        <div></div>
         <div  class="search-grid-container">
	      <div id="node_grid"></div>
	    </div>
	    <br />
	    <button type="button" class="button" id="cancelButton">返回</button>
    </div>
   
    <div id="lxcontent" class="hide">
      <form id="form" class="form-horizontal">
        <div class="row">
          <div class="control-group span9">
            <label class="control-label"> 请选择步骤类型：</label>
	            <div class="controls" id="stepType">
	          		<input type="hidden" id="stepTypeHide" value="" name="stepType">
	          	</div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  	$("#cancelButton").click(function(){
		location.href="${pageContext.request.contextPath}/content/etl/task_manager/taskList.jsp";
	});
  	var sybArr = ${syb};
  	var Grid = BUI.Grid,
  		Store = BUI.Data.Store,
  		Select=BUI.Select,
  		Overlay=BUI.Overlay,
  		columns = [
            //{ title: 'NO',width:100,dataIndex: 'step'},
            { title: '步骤编号',width:100,dataIndex: 'step'},
            /*{ title: '步骤编号', width:100, dataIndex: 'serverCluster',renderer:function(value,obj){
					if (!!value) {
						return value.serverClusterName;
					}
            	}},*/
            { title: '步骤名称',width:100,dataIndex: 'stepName'},
            { title: '步骤类型',width:100, dataIndex: 'stepType',renderer:function(v,o){
            	if(!!v){
            		return v.stepTypeName;
            	}
            }},
            { title: '任务名称',width:100, dataIndex: 'scene',renderer:function(value,obj){
				if (!!value) {
					return value.name;
				}
        	}},
        	{ title: '上一步',width:100, dataIndex: 'previousStep',renderer:function(value,obj){
				if (!!value) {
        			var s = '';
        			var arrlist = value.split(',');
        			for (var i = 0; i < arrlist.length; i++) {
						s =  s + ','+ (!!sybArr[arrlist[i]]?sybArr[arrlist[i]]:'已删除');
					}
					return arrlist.length > 0 ? s.substr(1) : s;
				}
        	}},
            { title: '工作节点', width: 100, dataIndex: 'node',renderer:function(v,o){
            	if(!!v){
            		return v.nodeCode;
            	}
            }},
            { title: '操作', width: 150, dataIndex: 'nodeCode',renderer:function(value,obj){
            
            	var editStr = '<span class="grid-command btn-edit" title="编辑节点">编辑</span>',
            	delStr='<span class="grid-command btn-del">删除</span>';
              return editStr+delStr;
            }}
          ];
  	var select1 = new BUI.Select.Select({
        render:'#stepType',
        valueField:'#stepTypeHide',
        width:200,
        store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/procedure/queryStepTypeList.do',
		      autoLoad : true
		    })
      });
  	var dialog = new Overlay.Dialog({
        title:'增加',
        width:400,
        height:180,
        contentId:'lxcontent',
        success:function () {
        	var vs = select1.getSelectedValue().split("j-");
          	this.close();
          	var index = '';
          	var i = 0;
          	for (var sb in sybArr) {
          		if (sb != -1) {
					index = index + ',' + sb;
					i++;
          		}
			}
          	index = i > 0?index.substr(1) : index;
          	location.href="${pageContext.request.contextPath}/content/etl/task_manager/add"+vs[1] + ".jsp?lxid=" + vs[0] + "&rwid=" + ${scene.sceneId} + "&index="+index;
        }
      });
    var store = new Store({
            url : '${pageContext.request.contextPath}/etl/procedure/queryProcedure.do?sceneId=${scene.sceneId}',
            autoLoad:true
          });
      var grid = new Grid.Grid({
            render:'#node_grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            plugins : [Grid.Plugins.CheckSelection], //勾选插件、自适应宽度插件 ,Grid.Plugins.AutoFit
            // 底部工具栏
            tbar:{
             items:[{text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',
             			handler:function(){
             				dialog.show();
						}
					},{text : '<i class="icon-plus"></i>统一编辑',btnCls : 'button button-small',
             			handler:function(){
             				location.href='${pageContext.request.contextPath}/content/etl/task_manager/editAllStep.jsp?sceneId=${scene.sceneId}';
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
        
        //监听事件，删除一条记录
	    grid.on('cellclick',function(ev){
	      var sender = $(ev.domTarget); //点击的Dom
	      if(sender.hasClass('btn-del')){
	        var record = ev.record;
	        //console.log(record);
	        delItems(record);
	      }
	      if(sender.hasClass('btn-edit')){
	        var record = ev.record;
	        //delItems([record]);
	        //alert(record.resourceId);
	        location.href='${pageContext.request.contextPath}/etl/procedure/toEditStep.do?stepId='+record.stepId;
	        //location.href='www.baidu.com?';
	        
	      }
	    });
 		
 		 //删除操作
	    function delFunction(){
	      var selections = grid.getSelection();
	      delItems(selections);
	    }
	    
	    function delItems(items){
	        BUI.Message.Confirm('确认要删除么？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/procedure/del'+ items.stepType.controllerMapping +'.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{stepId:items.stepId},
	            success : function(data){
	              if(data==1){ //删除成功
	              	BUI.Message.Alert("删除成功",function(){
	              		location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=${scene.sceneId}"
	              	},"success");
	              }else{ //删除失败
	                BUI.Message.Alert('删除失败！<br>失败信息：'+data,function(){},"error");
	              }
	            }
	        });
	        },'question');
	    }
 		
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
        
	    select1.render();
       // form.submit();
</script>

<body>
</html>  
