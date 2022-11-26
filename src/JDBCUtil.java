package com.eric.jdbc;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Author:Eric
 * Date:2022-10-23 21:56
 * Modified By:
 * Describe:������  ���Ǿ�̬����  ���췽��˽��
 */
public class JDBCUtil {

    private JDBCUtil(){};
    //��̬����飬����ؾ͵��ã�������ڴ����ж�μ���
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        String driver = bundle.getString("driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * �������ݿ�����
     * @return
     */
    public static Connection getConnection() throws SQLException{
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        String url = bundle.getString("url");
        String user = bundle.getString("user");
        String pwd = bundle.getString("pwd");

        return DriverManager.getConnection(url,user,pwd);
    }

    /**
     * ִ��DML���͵�sql����
     * @param
     * @param sql
     * @param objects
     * @return
     */
    public static boolean executeDMLWithValue(String sql, Object...objects){
        boolean flag = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            if (conn != null){
                //�����ֶ��ύ
                conn.setAutoCommit(false);
                //Ԥ�������
                pstmt = conn.prepareStatement(sql);
                //��sql��ֵ
                for (int i = 1; i<= objects.length; i++){
                    pstmt.setObject(i, objects[i-1]);
                }
                //ִ��sql
                pstmt.executeUpdate();
                conn.commit();
                flag =  true;
            }
        }catch (SQLException e){
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            close(null,pstmt,conn);
        }
        return flag;
    }

    /**
     * ִ������ע���DMLsql����
     * @param clazz
     * @param sql
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> executeDQL(Class<T> clazz, String sql, Object...objects) {
        Connection conn = null;
        Statement state = null;
        ResultSet res = null;
        ArrayList<T> returnList = null;
        try {

            returnList= new ArrayList<T>();
            conn = getConnection();
            if (conn != null){
                conn.setAutoCommit(false);
                state = conn.createStatement();

                res = state.executeQuery(sql);
                conn.commit();
                //��ȡԪ����
                ResultSetMetaData rsmd = res.getMetaData();
                int count = rsmd.getColumnCount();

                while (res.next()){
                    //��������T���͵Ķ���
                    T t = clazz.newInstance();

                    for (int i = 1; i <= count; i++){
                        //Ԫ�����е�����
                        //String columnName = rsmd.getColumnName(i); �����ǻ�ȡ��ṹ��
                        String columnName = rsmd.getColumnLabel(i);
                        //Ԫ�����е���value
                        Object columnValue = res.getObject(i);

                        //��ȡT���ж�Ӧ����
                        Field filed = t.getClass().getDeclaredField(columnName);

                        filed.setAccessible(true);
                        //��������ֵ��t�����Ӧ������
                        filed.set(t,columnValue);
                    }
                    returnList.add(t);
                }
            }

            return  returnList;
        }catch (Exception e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            close(res,state,conn);
        }
        return null;
    }


    /**
     * ִ�н�ֹsqlע���DQL��ѯ����
     * @param sql
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> executeDQLWithValue(Class<T> clazz, String sql, Object...objects){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            ArrayList<T> arrayList = new ArrayList<T>();
            //��ȡ����
            conn = getConnection();
            if (conn != null){
                conn.setAutoCommit(false);
                //Ԥ�������
                pstmt = conn.prepareStatement(sql);
                //��sql��ֵ
                for (int i = 1; i<= objects.length; i++){
                    pstmt.setObject(i, objects[i-1]);
                }
                //ִ��sql��ѯ
                res = pstmt.executeQuery();
                conn.commit();

                //��ȡ�������Ԫ����
                ResultSetMetaData rsmd = res.getMetaData();
                //��ȡ����
                int count = rsmd.getColumnCount();

                while (res.next()){
                    //��������
                    T t = clazz.newInstance();
                    //��ȡ���ݣ���������������޸�Ϊ��ȡ������
                    for (int i = 1; i <= count; i++){
                        //��ȡ�����Ԫ�����е�����
                        String columnName = rsmd.getColumnLabel(i);
                        //System.out.println(columnName);
                        //��ȡ�����Ԫ��������ֵ
                        Object columnValue = res.getObject(i);
                        //System.out.println(columnValue);

                        //���������������������Ӧ�����ԣ�
                        Field filed = clazz.getDeclaredField(columnName);
                        //����Ϊ���ж�Ӧ��ֵ
                        filed.setAccessible(true);  //ȡ��JAVA�����Լ��
                        filed.set(t,columnValue);
                    }
                    //��������ӵ��������
                    arrayList.add(t);
                }
                //���ؽ������
                return arrayList;
            }
        }catch (Exception e){
            if (conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            close(res,pstmt,conn);
        }

        return null;
    }


    /**
     * �ͷ���Դ
     * @param res  �����
     * @param stmt ���ݿ��������
     * @param conn ���ݿ����Ӷ���
     */
    public static void close(ResultSet res, Statement stmt, Connection conn){
        try {
            if (res != null){
                res.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (stmt != null){
                stmt.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}

