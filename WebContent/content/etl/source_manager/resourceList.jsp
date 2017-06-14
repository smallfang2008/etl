<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>资源列表</title>
    
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
      <form id="searchForm" class="form-horizontal span24">
        <div class="row">
          <div class="control-group span12">
            <label class="control-label">资源类型：</label>
            <div class="controls" id="resourceType">
              <input type="hidden" id="resourceTypeHide" value="" name="resourceType">
            </div>
          </div>
          <div class="control-group span12">
            <label class="control-label">资源名称：</label>
            <div class="controls">
              <input type="text" class="control-text" name="resourceName" value="">
            </div>
          </div>
        </div>
        <div class="row">
          
          <div class="control-group span12">
            <label class="control-label">主机名：</label>
            <div class="controls" >
              <input type="text" class="control-text" name="hostName">
            </div>
          </div>
          <div class="span8 offset2">
            <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
          </div>
        </div>
        <!-- 
        <div class="row">
          <div class="control-group span9">
            <label class="control-label">入学时间：</label>
            <div class="controls">
              <input type="text" class=" calendar" name="startDate"><span> - </span><input name="endDate" type="text" class=" calendar">
            </div>
          </div>
          <div class="span3 offset2">
            <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
          </div>
        </div>
       	 -->
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
            { title: '资源类型',width:80,dataIndex: 'resourceType',
            	renderer:function(value,obj){
	                return value.resourceTypeName;
	            }
            },
            { title: '资源名称', width:150, dataIndex: 'resourceName'},
            { title: '主机名',width:100, dataIndex: 'hostName'},
            { title: '账号',width:100,dataIndex: 'userName', showTip: true },
            { title: '密码',width:100, dataIndex: 'password'},
            { title: '操作', width: 150, dataIndex: 'resourceId',renderer:function(value,obj){
            
            	var editStr = '<span class="grid-command btn-edit" title="编辑资源">编辑</span>',
            	delStr='<span class="grid-command btn-del">删除</span>';
            	//alert(openEditStr);
              return editStr+delStr;
            }}
          ];
    var store = new Store({
            url : '${pageContext.request.contextPath}/etl/resource/resourceServicePageQuery.do',
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
             items:[{text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',
             			handler:function(){
             				location.href="${pageContext.request.contextPath}/content/etl/source_manager/editResource.jsp";
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
	        location.href='${pageContext.request.contextPath}/etl/resource/editResource.do?resourceId='+record.resourceId;
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
	        ids.push(item.resourceId);
	      });
	      if(ids.length){
	        BUI.Message.Confirm('确认要删除选中的记录么？',function(){
	          $.ajax({
	            url : '${pageContext.request.contextPath}/etl/resource/deleteResource.do',
	            dataType : 'text',
	            method:"post",
	            cache: false,
	            data:{resourceId:ids+""},
	            success : function(data){
	              if(data==1){ //删除成功
	              	BUI.Message.Alert("删除成功",function(){
	              		form.submit();
	              	},"success");
	              }else{ //删除失败
	                BUI.Message.Alert('删除失败！',function(){},"error");
	              }
	            }
	        });
	        },'question');
	      }
	    }
	    
	    var select = new Select.Select({
          render:'#resourceType',
          valueField:'#resourceTypeHide',
          store:new BUI.Data.Store({
		      url : '${pageContext.request.contextPath}/etl/resource/qryresource.do',
		      autoLoad : true
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
