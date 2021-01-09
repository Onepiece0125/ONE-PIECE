package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Driver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import beans.Book;
import beans.Log;

public class SQL {
	public static String Mysqlstr = "jdbc:mysql://localhost:3306/system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	public static String Mysqldriver = "com.mysql.jdbc.Driver";
	public static String name = "root";				//获得数据库的名字与密码
	public static String password = "root";

	//链接数据库
	public static Connection getConnection() {
		Connection con = null;
		try {
//			Driver driver = new Driver();
			Class.forName(Mysqldriver);			//加载驱动
//			DriverManager.registerDriver(My);
			con = DriverManager.getConnection(Mysqlstr, name, password);				//链接
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return con;
		}
	}
	
	//判断输入的账号是否错误，账号身份
	public static String Login(String username,String password) throws SQLException {
		//System.out.println("in");
		Connection con = getConnection();				//连接数据库
		//System.out.println("out"+con);
		String sql = "select identify from user where username=? and password=?";		//sql 语句进行查询用户名和密码
		PreparedStatement pstm = con.prepareStatement(sql);
		pstm.setString(1, username);					//数字1代表第一个问号
		pstm.setString(2, password);					//数字2代表第二个问号
		String reString ="";
		ResultSet rs = pstm.executeQuery();				//执行sql语句之后的结果反馈给rs
		while(rs.next()) {
			reString=  rs.getString(1);						//指针指向第一行
		}
		pstm.close();
		con.close();				//关闭数据库
		return reString;
	}
	
	//清空
	public static boolean Query(String sql) throws SQLException {
		Connection con = getConnection();					//连接数据库
		Statement st = con.createStatement();
		boolean flag = st.execute(sql);
		st.close();
		con.close();
		return !flag;
		
	}
	
	//查找
	public static List<Book> sel(String sql) throws SQLException{
		Connection con = getConnection();				//连接数据库
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);			//执行sql语句之后的结果反馈给rs
		List<Book> data = new ArrayList<>();
		while(rs.next()) {
			Book book =new Book();					//把在数据库中查询到的信息存入Book中
			book.number = rs.getString(1);			
			book.name = rs.getString(2);
			book.author = rs.getString(3);
			book.publisher = rs.getString(4);
			book.count = rs.getInt(5);
			data.add(book);
		}
		rs.close();
		st.close();
		con.close();
		return data;					//返回查询到的信息
	}

	//查找全部
	public static List<Log> sel_log(String sql) throws SQLException{
		Connection con = getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		List<Log> data = new ArrayList<>();
		while(rs.next()) {
			Log log =new Log();
			log.id = rs.getInt(1);
			log.username = rs.getString(2);
			log.bookid = rs.getString(3);
			log.time1 = rs.getDate(4);
			log.time2=rs.getDate(5);
			log.state = rs.getString(6);
			data.add(log);
		}
		rs.close();
		st.close();
		con.close();
		return data;
	}
}
