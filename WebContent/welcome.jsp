<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>
 <head>
  	<title>欢迎首页</title>
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
      <!--    -->
      <h3>欢迎进入ETL</h3>
      <hr/>
       <div class="row">
        <div class="form-actions offset1">
        	本次ETL整改简述：
        	<br/>
        		1.任务定时不再作为单独的步骤处理，定时表达式配置在任务中，由主节点管理定时器的装载和移除等操作
        	<br/>
        		2.步骤配置指定服务器集群或者某个节点，由服务器集群包含的服务器竞争获取任务，服务器将优先获取配置到本机上的任务
        	<br/>
        		3.日志打印剔除大量垃圾日志，日志内容更清晰，日志文件更小，保存时间更久
        	<br/>
        		4.优化任务流转过程中的各种编码问题，包括FTP编码、文件编码、入库编码等
        	<br/>
        		5.界面风格、结构调整
        	<br/>
        		........
        	<br/>
        	<br/>
          	运行ETL的服务器分主从节点，当前主节点为：<span id="masterNode" style="color: red"></span>
          	<br/>
          	<br/>
          	分布式后台运行程序会将配置缓存到Redis中，因缓存了部分集合数据，不易做实时清理
<!--          	修改某一配置会牵涉一系列缓存变动，不适合做实时清理，-->
<!--          	如：根据节点获取步骤，然后获取任务，修改任一步骤都有可能刷新所有步骤相关的缓存-->
			<br/>
          	因此修改配置之后如需配置生效需手动<button type="button" class="button" id="RedisClearButton">清除缓存</button>
          	<br/>
        	<br/>
          	2017.03更新：
          	<br/>
          	添加步骤：远程调用shell命令、oracle数据导出;
          	优化步骤文件上传：支持文件目录动态化
          	<br/>
          	2017.04更新：
          	<br/>
          	优化步骤文件上传：支持文件名可拼接日期，支持上传前是否压缩，解决多文件传输间隔过长FTP连接丢失问题;
          	<br/>
          	优化转换步骤：当文件追加时，保留原有文件名追加随机码，当文件不追加时，不再追加随机码;
        </div>
      </div>
  </div>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath }/styles/bui-bootstrap/assets/js/config-min.js"></script>
  <script type="text/javascript">
    jQuery(document).ready(function(){
    	loadWelcomeInfo();
    });
    
    function loadWelcomeInfo(){
    	jQuery.ajax({
    		url:'${pageContext.request.contextPath}/etl/welcome/loadWelcomeInfo.do',
    		type:'post',
    		data:{},
    		dataType:'json',
    		success:function(data){
    			jQuery("#masterNode").html(data.masterNode);
    		}
    	});
    }
    
    jQuery("#RedisClearButton").click(function(){
    	BUI.Message.Confirm('确认清除Redis缓存么?',function(){
	    	jQuery.ajax({
	    		url:'${pageContext.request.contextPath}/etl/welcome/clearRedisCache.do',
	    		type:'post',
	    		data:{},
	    		dataType:'text',
	    		success:function(data){
	    			if(data=="1"){
		        		BUI.Message.Alert("清除成功!",function(){
							
						},"success");
		        	}else{
		        		BUI.Message.Alert("清除失败!",function(){},"error");
		        	}
	    		}
	    	});
	    },'question');
    });
  </script>

</body>
</html> 