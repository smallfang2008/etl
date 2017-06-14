<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>节点列表</title>
    
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
    <div class="row">
      <form id="searchForm" class="form-horizontal">
        <div class="row span20">
          <div class="control-group span10">
            <label class="control-label">节点编码：</label>
            <div class="controls">
              <input type="text" class="control-text" name="nodeCode" value="">
            </div>
          </div>
          <div class="control-group span10">
            <label class="control-label">FTP服务名称：</label>
            <div class="controls">
              <input type="text" class="control-text" name="serverName" value="">
            </div>
          </div>
        </div>
        <div class="row span20">
          <div class="control-group span10">
            <label class="control-label">IP地址：</label>
            <div class="controls">
              <input type="text" class="control-text" name="ipAddress" value="">
            </div>
          </div>
          <div class="control-group span10">
            <label class="control-label">集群：</label>
            <div class="controls" id="serverCluserName">
              <input type="hidden" id="serverCluserNameHide" value="-" name="serverClusterId">
            </div>
          </div>
        </div>
        <div class="offset2 doc-content">
            <button type="button" id="btnSearch" class="button button-primary" style="margin-top: 35px;">搜索</button>
        </div>
        <div></div>
      </form>
    </div>
    <div class="search-grid-container">
      <div id="node_grid"></div>
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
            { title: '节点编码',width:100,dataIndex: 'nodeCode'},
            { title: '集群', width:100, dataIndex: 'serverCluster',renderer:function(value,obj){
					if (!!value) {
						return value.serverClusterName;
					}
            	}},
            { title: 'FTP服务名称',width:100, dataIndex: 'serverName'},
            { title: 'IP地址',width:100, dataIndex: 'ipAddress'},
            { title: '日志目录',width:100,dataIndex: 'logPath'},
            { title: '最大任务数', width: 100, dataIndex: 'maxTasks'},
            { title: '是否获取集群任务',width:100, dataIndex: 'isGetClusterTask',renderer:function(value,obj){
				var getClusterTaskFlag = "否";
				if (!!value && "1" == value) {
					getClusterTaskFlag = "是";
				}
				return getClusterTaskFlag;
            }},
            { title: '操作', width: 150, dataIndex: 'nodeCode',renderer:function(value,obj){
            
            	var editStr = '<span class="grid-command btn-edit" title="编辑节点">编辑</span>',
            	delStr='<span class="grid-command btn-del">删除</span>';
            	//alert(openEditStr);
              return editStr+delStr;
            }}
          ];
    var store = new Store({
            url : '${pageContext.request.contextPath}/etl/node/queryNodeByPage.do',
            autoLoad:false,
            pageSize:10
          });
      var grid = new Grid.Grid({
            render:'#node_grid',
            loadMask: true,
            forceFit:true,
            columns : columns,
            store: store,
            plugins : [Grid.Plugins.CheckSelection], //勾选插件、自适应宽度插件,Grid.Plugins.AutoFit
            // 底部工具栏
            tbar:{
             items:[{text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',
             			handler:function(){
             				location.href="${pageContext.request.contextPath}/content/etl/work_node/addNode.jsp";
						}
					},
		            {text : '<i class="icon-remove"></i>删除',btnCls : 'button button-small',handler : delFunction}]
            },
            // 顶部工具栏
            bbar : {
              //items 也可以在此配置
              // pagingBar:表明包含分页栏
              pagingBar:true
            }
        });
 
        grid.render();
        
        //监听事件，删除一条记录
	    grid.on('cellclick',function(ev){
	      var sender = $(ev.domTarget); //点击的Dom
	      if(sender.hasClass('btn-del')){
	        var record = ev.record;
	        delItems([record]);
	      }
	      if(sender.hasClass('btn-edit')){
	        var record = ev.record;
	        //delItems([record]);
	        //alert(record.resourceId);
	        location.href='${pageContext.request.contextPath}/etl/node/toEditNode.do?nodeCode='+record.nodeCode;
	        //location.href='www.baidu.com?';
	        
	      }
	    });
 		
 		 //删除操作
	    function delFunction(){
	      var selections = grid.getSelection();
	      delItems(selections);
	    }
	    
	    function delItems(items){
	      var ids = [];
	      BUI.each(items,function(item){
	        ids.push(item.nodeCode);
	      });
	      if(ids.length){
	        BUI.Message.Confirm('确认要删除选中的记录么？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/node/delNode.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{nodeCode:ids.join(",")},
	            success : function(data){
	              if(data==1){ //删除成功
	              	BUI.Message.Alert("删除成功",function(){
	              		form.submit();
	              	},"success");
	              }else{ //删除失败
		            data = "2" == data ? "删除节点存在被使用！" : data;
	                BUI.Message.Alert('删除失败！<br>失败信息：'+data,function(){},"error");
	              }
	            }
	        });
	        },'question');
	      }
	    }
	    
	    var select = new Select.Select({
          render:'#serverCluserName',
          valueField:'#serverCluserNameHide',
          store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/node/queryServerClusterList.do',
		      autoLoad : true,
		      params : {"needAll":"1"}
		    })
          /*items:[
	          {text:'FTP',value:'1'},
	          {text:'DB',value:'2'}
	        ]*/
        });
    	select.render();
 		
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
</script>

<body>
</html>  
