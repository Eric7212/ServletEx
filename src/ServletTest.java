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
		//如何将一个信息直接输出到浏览器上面？
		//需要使用ServletResponse接口
		//Response表示响应：从服务器向浏览器发送数据叫做响应。
		
		
		PrintWriter out = resp.getWriter();
		
		out.print("Print Message To Browser");
		
		//这是一个输出流，负责输出字符串到浏览器
		//这个输出流不需要我们刷新，也不需要我们关闭，这些由Tomcat来维护
		//out.flush();
		//out.close();
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