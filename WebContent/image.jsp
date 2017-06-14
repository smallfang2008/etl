<%@ page contentType="image/jpeg"%>
<%@ page import="javax.imageio.*"%>

<jsp:useBean id="image" scope="session" class="com.zbiti.common.imagetool.ImageTool" />
<%
	
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", 0);
	
	ImageIO.write(image.creatImage(), "JPEG", response.getOutputStream());
	out.clear();
	out = pageContext.pushBody();
	request.getSession().setAttribute("validate",image.getSRand());
	//request.getSession().setAttribute("imageTool",image);
%>
