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
		//��ν�һ����Ϣֱ���������������棿
		//��Ҫʹ��ServletResponse�ӿ�
		//Response��ʾ��Ӧ���ӷ�������������������ݽ�����Ӧ��
		
		
		PrintWriter out = resp.getWriter();
		
		out.print("Print Message To Browser");
		
		//����һ�����������������ַ����������
		//������������Ҫ����ˢ�£�Ҳ����Ҫ���ǹرգ���Щ��Tomcat��ά��
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