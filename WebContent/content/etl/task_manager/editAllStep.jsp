<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
    <title>步骤管理-统一编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/page-min.css" rel="stylesheet" type="text/css" />  
   
 </head>
 <body>
  <div style="padding: 0px 30px" class="container">
    <h3>步骤管理-统一编辑</h3>
    <hr>
    <form id ="N_Form" class="form-horizontal" action="${pageContext.request.contextPath}/etl/procedure/editAllStep.do" method="post">
	    <div class="row">
	       <div class="search-grid-container">
              <div id="grid"></div>
              <input type="hidden" name="step" id="step" value=""/>
           </div>
	    </div>
	    
	    <div class="row">
	          <div class="span12 offset2" align="center">
	            <button type="submit" id="saveButton" class="button button-primary">保存</button>
	            &nbsp;&nbsp;
	            <button type="button" id="cancelButton" class="button button-primary">取消</button>
	          </div>
	    </div>
	</form>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
  
      var enumObj;
	  $(document).ready(function(){
		  $.ajax({
	            url : '${pageContext.request.contextPath}/etl/procedure/queryyxlxList.do',
	            dataType : 'json',
	            method:"post",
	            async: false,
	            success : function(data){
	            	enumObj = data;
	            }
	         });
	   });
	  
	  var N_Form = new BUI.Form.HForm({
	      srcNode : '#N_Form',
	      submitType : 'ajax',
	      dataType:'text',
	      callback : function(data){
	      	if(data=="1"){
	      		BUI.Message.Alert("步骤编辑成功!",function(){
						location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=" + ${param.sceneId};
					},"success");
	      		//history.back();
	      	}else{
	      		BUI.Message.Alert("步骤编辑失败!","error");
	      	}
	      }
	   }).render();
  
	  var Grid = BUI.Grid,
		Store = BUI.Data.Store,
		Select=BUI.Select,
		runTypeObj = {"0" : "进程","1" : "线程"},
		columns = [
          { title: '序号',width:100,dataIndex: 'step', editor:{xtype : 'text'}},
          { title: '步骤类型',width:100,dataIndex: 'stepType', renderer:function(v,o){
          	if(!!v){
        		return v.stepTypeName;
        	}
          }},
          { title: '步骤名称',width:100,dataIndex: 'stepName', editor:{xtype : 'text'}},
          //{ title: '步骤运行类型',width:100,dataIndex: 'runPositionType', xtype : 'text', rules:{required:true}},
          { title: '步骤运行位置',width:100,dataIndex: 'runPosition', editor : {xtype :'select',items : enumObj},
        	  renderer:function(value,obj){
        		  var rst = {};
        		  BUI.each(enumObj[obj.runPositionType],function(item){
        			  if(item.value == value){  
        				  rst = item;           
        			  }
        		  });
        		return rst.text;
          	}},
          { title: '上一步骤序号(多个步骤用","分割)',width:140,dataIndex: 'previousStep', editor:{xtype : 'text'}},
          { title: '并发数',width:100,dataIndex: 'threadNum', editor:{xtype : 'text'}},
          { title: '最大内存',width:100,dataIndex: 'memMax', editor:{xtype : 'text'}},
          { title: '最小内存',width:100,dataIndex: 'memMin', editor:{xtype : 'text'}},
          { title: '运行方式',width:100,dataIndex: 'runType', editor : {xtype :'select',items : runTypeObj}, renderer:Grid.Format.enumRenderer(runTypeObj)}
          
	    ];
	  
	  var store = new Store({
		  url : '${pageContext.request.contextPath}/etl/procedure/queryProcedure.do?sceneId=${param.sceneId}',
          autoLoad:true,
        }),
      editing = new Grid.Plugins.CellEditing(),
      grid = new Grid.Grid({
          render:'#grid',
          loadMask: true,
          forceFit:true,
          columns : columns,
          store: store,
          plugins : [editing], 
          // 底部工具栏
          tbar:{
           
          },
          // 顶部工具栏
          bbar : {
            //items 也可以在此配置
            // pagingBar:表明包含分页栏
            //pagingBar:true
          }
      });
	  
      grid.render();
      
      editing.on('editorshow',function(ev){
          var editor = ev.editor,
          record = editing.get('record'),
          field = editor.get('field'); 
          if(field.get('name') == 'runPosition'){ 
              var aValue = record.runPositionType; 
			  field.set('items',enumObj[aValue]);
              field.set('value',record.runPosition); 
          }
        });
      
      N_Form.on('beforesubmit',function(){
    	  var records = store.getResult();
          $("#step").val(BUI.JSON.stringify(records));
      });
      
      $("#cancelButton").click(function(){
    	  location.href="${pageContext.request.contextPath}/etl/procedure/toEditProcedure.do?sceneId=" + ${param.sceneId};
		});
      
  </script>
 <body>
</html>  
