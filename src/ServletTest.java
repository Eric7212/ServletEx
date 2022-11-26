package com.eric.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletTest implements Servlet{
	//实现5个方法
	public void init(ServletConfig config) throws ServletException{
	
	}
	
	public void service(ServletRequest req, ServletResponse resp) 
	throws ServletException,IOException{
		
		//向网页发送html代码
		//设置响应的内容类型是普通文本或者html代码,该句必须写在流对象创建之前
		resp.setContentType("text/html");
		
		PrintWriter out = resp.getWriter();
		
		out.print("<a href = 'http://baidu.com'>This is a super link</a>");
	} 
	
	public void destroy(){
	
	}
	
	public String getServletInfo(){
		return "";
	}
	
	public ServletConfig getServletConfig(){
		return null;
	}
}