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
 * Describe:工具类  都是静态方法  构造方法私有
 */
public class JDBCUtil {

    private JDBCUtil(){};
    //静态代码块，类加载就调用，避免放在代码中多次加载
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
     * 建立数据库连接
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
     * 执行DML类型的sql操作
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
                //设置手动提交
                conn.setAutoCommit(false);
                //预编译程序
                pstmt = conn.prepareStatement(sql);
                //向sql传值
                for (int i = 1; i<= objects.length; i++){
                    pstmt.setObject(i, objects[i-1]);
                }
                //执行sql
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
     * 执行允许注入的DMLsql操作
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
                //获取元数据
                ResultSetMetaData rsmd = res.getMetaData();
                int count = rsmd.getColumnCount();

                while (res.next()){
                    //创建对象T类型的对象
                    T t = clazz.newInstance();

                    for (int i = 1; i <= count; i++){
                        //元数据中的列名
                        //String columnName = rsmd.getColumnName(i); 该行是获取表结构名
                        String columnName = rsmd.getColumnLabel(i);
                        //元数据中的列value
                        Object columnValue = res.getObject(i);

                        //获取T类中对应属性
                        Field filed = t.getClass().getDeclaredField(columnName);

                        filed.setAccessible(true);
                        //将列名赋值到t对象对应属性中
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
     * 执行禁止sql注入的DQL查询操作
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
            //获取连接
            conn = getConnection();
            if (conn != null){
                conn.setAutoCommit(false);
                //预编译程序
                pstmt = conn.prepareStatement(sql);
                //向sql传值
                for (int i = 1; i<= objects.length; i++){
                    pstmt.setObject(i, objects[i-1]);
                }
                //执行sql查询
                res = pstmt.executeQuery();
                conn.commit();

                //获取结果集的元数据
                ResultSetMetaData rsmd = res.getMetaData();
                //获取列数
                int count = rsmd.getColumnCount();

                while (res.next()){
                    //创建对象
                    T t = clazz.newInstance();
                    //读取数据，并将对象的属性修改为读取的数据
                    for (int i = 1; i <= count; i++){
                        //获取结果集元数据中的列名
                        String columnName = rsmd.getColumnLabel(i);
                        //System.out.println(columnName);
                        //获取结果集元数据中列值
                        Object columnValue = res.getObject(i);
                        //System.out.println(columnValue);

                        //将对象中与结果集列名相对应的属性，
                        Field filed = clazz.getDeclaredField(columnName);
                        //复制为类中对应的值
                        filed.setAccessible(true);  //取消JAVA的语言检查
                        filed.set(t,columnValue);
                    }
                    //将对象添加到结果集中
                    arrayList.add(t);
                }
                //返回结果集合
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
     * 释放资源
     * @param res  结果集
     * @param stmt 数据库操作对象
     * @param conn 数据库连接对象
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

