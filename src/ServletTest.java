package com.eric.servlet;

import javax.servlet.*;
import java.io.IOException;

public class ServletTest implements Servlet{
	//ʵ��5������
	public void init(ServletConfig config) throws ServletException{
	
	}
	
	public void service(ServletRequest req, ServletResponse resp) 
	throws ServletException,IOException{
		System.out.println("My Servlet");
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