package com.zbiti.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionFilter implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//System.out.println("do filter");
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		//System.out.println(req.getRequestURI());
		if(req.getRequestURI().endsWith("/login.jsp")
				||req.getRequestURI().endsWith("/login.do")
				||req.getRequestURI().endsWith("/image.jsp")){
			//System.out.println("登录页面，pass");
			chain.doFilter(request, response);
			//req.getSession().setAttribute("userinfo", 1);
			return;
		}
		if(req.getSession().getAttribute("ETL_USER")==null){
			//System.out.println("session过期，跳到登录页面");
			if (req.getHeader("x-requested-with") != null&& req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				//System.out.println("ajax request");
				res.addHeader("sessionstatus", "timeOut");                 
				res.addHeader("loginPath", req.getContextPath()+"/login.jsp");                 
				chain.doFilter(request, response);// 不可少，否则请求会出错             
			}else{
//				res.sendRedirect(req.getContextPath()+"/login.jsp");
				res.getWriter().write("<script language='javascript'>window.top.location.href='"+req.getContextPath()+"/login.jsp'</script>");
			}
		}else{
			//System.out.println("有session，继续访问");
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
