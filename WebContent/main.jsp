<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>ETL平台3.0</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
  <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
   <link href="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/css/main-min.css" rel="stylesheet" type="text/css" />
 </head>
 <body>

  <div class="header">
    
      <div class="dl-title">
        
          <span class="dl-title-text">中博ETL工具-分布式ETL平台软件V3.0</span>
      </div>

    <div class="dl-log">欢迎您，<span class="dl-log-user">${sessionScope.ETL_USER.username }</span><a href="###" title="退出系统" id="loginOut" class="dl-log-quit">[退出]</a>
    </div>
  </div>
   <div class="content">

	
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
   </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/scripts/jquery/jquery-1.8.0.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config.js"></script>

  <script>
    BUI.use('common/main',function(){
      var config = [{
          id:'menu', 
          homePage : 'index',
          menu:[{
              text:'基础信息配置',
              items:[
                {id:'index',text:'首页',href:'${pageContext.request.contextPath }/welcome.jsp',closeable : false},
                {id:'resource-menu',text:'资源配置',href:'${pageContext.request.contextPath }/content/etl/source_manager/resourceList.jsp'},
                {id:'cluser-menu',text:'集群配置',href:'${pageContext.request.contextPath }/content/etl/server_farm/cluserList.jsp'},
                {id:'node-menu',text:'节点配置',href:'${pageContext.request.contextPath }/content/etl/work_node/nodeList.jsp'}
              ]
            },{
              text:'运行监控',
              items:[
                {id:'task-running',text:'正在运行',href:'${pageContext.request.contextPath }/content/etl/task_manager/taskDoingList.jsp'},
                {id:'task-wait',text:'等待运行',href:'${pageContext.request.contextPath }/content/etl/task_manager/taskWaitList.jsp'},
                {id:'task-error',text:'运行异常',href:'${pageContext.request.contextPath }/content/etl/task_manager/taskErrorList.jsp'},
                {id:'task-his',text:'运行历史',href:'${pageContext.request.contextPath }/content/etl/task_manager/taskHisList.jsp'}
              ]
            },{
              text:'任务平台',
              items:[
                {id:'task-manager',text:'任务管理',href:'${pageContext.request.contextPath }/content/etl/task_manager/taskList.jsp'}
              ]
            }]
          }];
      new PageUtil.MainPage({
        modulesConfig : config
      });
    });
    $('#loginOut').click(function() {
         location.href = '${pageContext.request.contextPath}/etl/EtlLoginService/logout.do';

     })
  </script>
 </body>
</html>

