package com.eric.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletTest implements Servlet{
	//ʵ��5������
	public void init(ServletConfig config) throws ServletException{
	
	}
	
	public void service(ServletRequest req, ServletResponse resp) 
	throws ServletException,IOException{
		
		//����ҳ����html����
		//������Ӧ��������������ͨ�ı�����html����,�þ����д�������󴴽�֮ǰ
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