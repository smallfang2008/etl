package com.zbiti.etl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbiti.etl.extend.smo.IEtlUserService;
import com.zbiti.etl.extend.vo.EtlUser;

/**
 * @author yhp
 * EtlUserACTION
 * 2014-5-20
 */
@Controller
@RequestMapping("/etl/EtlLoginService")
public class EtlLoginServiceController
{
	@Autowired(required = true)
	private IEtlUserService iEtlUserService;
	
	@RequestMapping("")
	public String index()
	{
		return "login";
	}
	
	@RequestMapping("/login")
	public void login(HttpServletRequest request, 
			HttpServletResponse response, HttpSession session, EtlUser user,String imgValidate) throws Exception{
		//response.getWriter().write(user.getUsername()+"\t"+user.getPassword());
		String sessionValidate=(String)session.getAttribute("validate");
//		System.out.println(sessionValidate);
		if(sessionValidate==null||imgValidate==null||!imgValidate.equals(sessionValidate)){
			response.getWriter().write("4");
			return;
		}
		
		EtlUser _user=iEtlUserService.getByUsername(user.getUsername());
		String flag="1";//登录成功
		if(_user==null){
			flag="2";//账号不存在
		}else{
			if(!(_user.getPassword()==null?"":_user.getPassword()).equals(user.getPassword())){
				flag="3";
			}else{
				//登录成功
				session.setAttribute("ETL_USER", _user);
			}
		}
		response.getWriter().write(flag);
	}
	
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, 
			HttpServletResponse response, HttpSession session) throws Exception{
		//response.getWriter().write(user.getUsername()+"\t"+user.getPassword());
		session.removeAttribute("ETL_USER");
		response.getWriter().write("<script language='javascript'>window.top.location.href='"+request.getContextPath()+"/login.jsp'</script>");
	}
	

	@RequestMapping("/updatePassword")
	public void updatePassword(HttpServletRequest request, 
			HttpServletResponse response, HttpSession session,String newpass) throws Exception{
		//response.getWriter().write(user.getUsername()+"\t"+user.getPassword());
		EtlUser user=(EtlUser)session.getAttribute("ETL_USER");
		if(user!=null){
			user.setPassword(newpass);
			iEtlUserService.update(user);
		}

		response.getWriter().write(newpass);
	}
}
