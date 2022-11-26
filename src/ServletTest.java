package com.eric.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import com.eric.jdbc.JDBCUtil;
import com.eric.test.T_ACT;
import java.util.*;
import java.lang.*;

public class ServletTest implements Servlet{
	//ʵ��5������
	public void init(ServletConfig config) throws ServletException{
	
	}
	
	public void service(ServletRequest req, ServletResponse resp) 
	throws ServletException,IOException{
		
		resp.setContentType("text/html");
		
		PrintWriter out = resp.getWriter();
		
		//�������ݿ�����
		String sql = "select * from t_act";
		
		ArrayList<T_ACT> list = JDBCUtil.executeDQLWithValue(T_ACT.class,sql);
        Iterator<T_ACT> iterator = list.iterator();
        while (iterator.hasNext()){
            T_ACT t = iterator.next();
            out.print("actno:" + t.actno + "," + "balance:" +  t.balance + "<br>");
        }
		
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