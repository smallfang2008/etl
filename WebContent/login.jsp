<%@ page language="java" isELIgnored="false" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head> 
    <title>调度平台登录</title> 
    <link href="<%=request.getContextPath()%>/styles/css/jquery/loadmask/jquery.loadmask.css" type="text/css" rel="stylesheet" />
    <script src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/scripts/jquery/jquery.loadmask.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/scripts/jquery/jquery.form.js" type="text/javascript"></script>
    <style> 
        .fll{float: left;} 
        .flr{float: right;} 
        body{ 
        	background: url("<%=request.getContextPath()%>/images/top_bg.jpg") no-repeat;
        	background-size:100%;
        } 
        .login{ 
            /*position: absolute;*/ 
            /*top: 40%;*/ 
            /*left: 50%;*/ 
            /*margin-top:  -150px;*/ 
            /*margin-left: -140px;*/ 
            margin: 50px auto 100px; 
            width: 290px; 
            height: 380px; 
            overflow: hidden; 
            /*background: #333;*/ 
        } 
  
        .loginbox{ 
            font-family: 'Segoe UI', 'Microsoft Yahei', Arial, Simsun, sans-serif, "宋体"; 
            font-size: 14px; 
            padding:20px 25px 0 25px; 
        } 
        .loginboxtag{ 
            height: 30px; 
            line-height: 30px; 
            padding-left: 2px; 
            color: #fbfbfb; 
            cursor: default; 
            user-select: none; -moz-user-select: none;  -webkit-user-select: none;  -ms-user-select: none; 
        } 
        .loginboxinput{ 
            height: 30px; 
        } 
        .imageValidate{ 
            vertical-align: middle;
            float: right;
        } 
        input[type="text"], input[type="password"], textarea{  
            padding-left: 20px; 
			padding-right:20px;
            color: #0e8c3c; 
            outline: 0px; 
            height: 26px; 
			line-height:26px;
            width: 200px; 
            border: 1px solid #ccc; 
            /*border-radius: 3px;*/ 
            transition: all .2s;-webkit-transition: all .2s;-moz-transition: all .2s; 
        } 
  
        input[type="text"] { 
            background: url(img/iconman.png) no-repeat #f8faf8; 
        } 
        input[type="password"] { 
            background: url(img/iconlock.png) no-repeat #f8faf8; 
        } 
        input[type="text"]:focus, input[type="password"]:focus, textarea:focus {  
            border: 1px solid #c8c8c8; 
            background-color: #f3f7f3; 
            /*box-shadow:inset 0 0 3px rgba(40,140,210,1);*/ 
            /*background-color: #444;*/ 
        } 
  
        .loginboxbtn{ 
            overflow: auto;zoom: 1; 
            height: 40px; 
            padding-top: 10px; 
  
        } 
        input[type="checkbox"]{ 
            margin: 0; 
            margin-right: 10px; 
        } 
        .loginboxbtn .rem{ 
            font-size: 12px; 
            padding-top: 12px; 
            user-select: none; -moz-user-select: none;  -webkit-user-select: none;  -ms-user-select: none; 
        } 
        .loginboxbtn .rem span label{ 
            /*display: inline-block; 
            margin-top: -5px;*/ 
            color: #fbfbfb; 
            position: relative; 
            top: -2px; 
            cursor: default; 
        } 
        .btn{ 
            display: inline-block; 
            width: 80px; 
            height: 30px; 
            line-height: 30px; 
            text-align: center; 
            /*background: #0e9c4c;*/ 
            background: #46Ae00; 
            color: #fff; 
            /*border-radius: 5px;*/ 
            box-shadow: 0 0 1px rgba(0,0,0,0.3); 
            cursor: pointer; 
            transition: all .1s;-webkit-transition: all .1s;-moz-transition: all .1s; 
            user-select: none; -moz-user-select: none;  -webkit-user-select: none;  -ms-user-select: none; 
        } 
        .btn:hover{ 
            /*background: #0e8c3c;*/ 
            background: #339b00; 
        } 
        .btn:active{ 
            /*background: #0e7c2c;*/ 
            background: #288f00; 
        } 
    </style> 
    
	<script type="text/javascript">  
	$(".login").fadeIn(700); 
	//改善表单输入用户体验 
	$(function () { 
	    $('input:text:first').focus(); 
	  
	    //回车聚焦下个输入框 
	    /*var $inp = $('input:text'); 
	    $inp.bind('keydown', function (e) { 
	        var key = e.which; 
	        if (key == 13) { 
	            e.preventDefault(); 
	            $("input:password:first").focus(); 
	        } 
	    }); 
	  
	    //回车触发按钮点击 
	    var $pwd = $('input:password:first'); 
	    $pwd.bind('keydown', function (e) { 
	        var key = e.which; 
	        if (key == 13) { 
	            e.preventDefault(); 
	            $("#submit").trigger("click"); 
	        } 
	    }); */
	  	$(document).bind('keydown', function (e) { 
	        var key = e.which; 
	        if (key == 13) { 
	            e.preventDefault(); 
	            $("#submit").trigger("click"); 
	        } 
	    });
	    // 
	    $("#submit").click(function() { 
	        //$("#loginform").submit();
	        if($("#username").val()==""){
	        	$("#msg").show();
	        	$("#msg").html("* 账号不能为空！");
	        	return;
	        }
	        if($("#password").val()==""){
	        	$("#msg").show();
	        	$("#msg").html("* 密码不能为空！");
	        	return;
	        }
	        if($("#imgValidate").val()==""){
	        	
	        	$("#msg").show();
	        	$("#msg").html("* 验证码不为空！");
	        	//$("#imageValidate").attr("src",$("#imageValidate").src);
	        	return;
	        }
	        $(".login").mask("登录中...");
			$('#loginform').ajaxSubmit( {   
			    target : '#formSubDiv',   
			    url : '${pageContext.request.contextPath}/etl/EtlLoginService/login.do', 
			    type:"POST",
				contentType:"application/x-www-form-urlencoded;charset=UTF-8",
				dataType: "text",   
			    error : function() {   
			     alert('登录出错！') ;  
			     $(".login").unmask(); 
			    },
			    success: function(data){
			     	$(".login").unmask(); 
			      	//alert(data)
			      	if(data=="4"){
			        	$("#msg").show();
			        	$("#msg").html("* 验证码不对！请重新输入");
	        			$("#imageValidate").attr("src",'${pageContext.request.contextPath}/image.jsp?t='+parseInt(10000*Math.random()));
			      		return;
			      	}
			      	if(data!="1"){
	        			$("#msg").html("* 账号或密码错误！");
	        			$("#password").val("");
			      		$("#msg").show();
			      	}else{
			      		location.href="${pageContext.request.contextPath}/main.jsp";
			      		return;
			      	}
	        		$("#imageValidate").attr("src",'${pageContext.request.contextPath}/image.jsp?t='+parseInt(10000*Math.random()));
			      	
			    } 
		   	});  
	    }); 
	}); 
	</script>
</head> 
<body> 
    <div class="login"> 
        <div class="angel"> 
            <div class="loginbox"> 
                <form name="loginform" id="loginform" action="post"> 
                    <div class="loginboxtag" style="text-align: center;font-size: 18px">中博分布式ETL平台软件V3.0</div> 
                    <div class="loginboxtag" style="text-align: center;font-size: 16px"></div> 
                    <div class="loginboxtag" style="text-align: center;font-size: 15px"></div> 
                    <div class="loginboxtag">用户名：</div> 
                    <div class="loginboxinput"><input name="username" id="username" type="text"/></div> 
                    <div class="loginboxtag">密码：</div> 
                    <div class="loginboxinput"><input type="password" id="password" name="password"/></div>
                    <div class="loginboxtag">验证码：</div> 
                    <div class="loginboxinput"><input type="text" id="imgValidate" name="imgValidate" style="width: 100px;"/><img class="imageValidate" onclick="this.src='${pageContext.request.contextPath}/image.jsp?t='+parseInt(10000*Math.random())" id="imageValidate" src="${pageContext.request.contextPath}/image.jsp"/></div>
                    <div class="loginboxtag" id="msg" style="color: red;display: none;">*&nbsp;&nbsp;账号或密码错误！</div>  
                    <div class="loginboxbtn"> 
                        <div class="fll rem"><input id="rem" type="checkbox"/><span><label for="rem">记住登录状态</label></span></div> 
                        <div id="submit" class="flr btn">登入</div> 
                    </div> 
                    <div id="formSubDiv"></div>
                </form> 
            </div> 
        </div> 
    </div> 
 <!--             <div> 
        <iframe src="http://www.qiyeyouju.com/index.php?m=content&a=index&id=20" frameborder="0"></iframe> 
    </div> -->
    <div style="clear: both;"></div>
    <div style="margin: 100px auto 20px; width:360px;font-family: 'Segoe UI', 'Microsoft Yahei', Arial, Simsun, sans-serif, '宋体';font-size: 14px;color: #0d179a;">中博信息技术研究院有限公司 Copyright 2016 版权所有。</div>
</body> 
</html> 
  
  