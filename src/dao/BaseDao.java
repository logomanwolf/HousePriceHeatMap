package dao;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.jdbc.Statement;

import javax.naming.*;

public class BaseDao {

	DataSource dataSource;
    //数据库地址“jdbc:mysql://服务器域名:端口号/数据库名称”
    private String url = "jdbc:mysql://127.0.0.1:3306/fivecrowdsourcing?useUnicode=true&characterEncoding=utf-8";
    //用户名
    private String user = "root";
    //用户密码
    private String pwd = "123456";
    //数据库链接对象
    private java.sql.Connection conn;
    //数据库命令执行对象
    private PreparedStatement pstmt;
    //数据库返回结果
    private java.sql.ResultSet rs;
    
    public BaseDao(){
    	try {
			Context context = new InitialContext();
			dataSource = (DataSource) context
					.lookup("java:comp/env/jdbc/fivecrowdsourcing");
		} catch (NamingException ne) {
			System.out.println("Exception:" + ne);
		}
    }
//    //静态代码块
//    static{
//        //1、加载驱动
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            
//            e.printStackTrace();
//        }
//    }
    
    //2、创建连接
    public Connection getConnection() throws Exception {
		conn=dataSource.getConnection();
		return conn;
	}
    
    //执行读操作方法
    public java.sql.ResultSet executeQuery(String query,
                    List<Object> params) {
        
        try {
        	getConnection();
            //3、创建命令执行对象
            pstmt = conn.prepareStatement(query);
            //4、执行
            if(params!=null && params.size()>0){
                for(int i=0;i<params.size();i++){
                    pstmt.setObject(i+1, params.get(i));
                }
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return rs;
    }
    
    //执行写操作方法
    public int executeUpdate(String query,
            List<String> params){
        int result = 0;
        
        try {
        	getConnection();
            //3、创建命令执行对象
            pstmt = conn.prepareStatement(query);
            //4、执行
            if(params!=null && params.size()>0){
                for(int i=0;i<params.size();i++){
                    pstmt.setString(i+1, params.get(i));
                }
            }
            //5、处理结果
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
            //6、释放资源
            this.close();
        }
        return result;
    }

    
    //关闭资源
    public void close(){        
            try {
                if(rs!=null){
                    rs.close();
                    rs = null;
                }
                if(pstmt!=null){
                    pstmt.close();
                    pstmt = null;
                }
                if(conn!=null){
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        }

}