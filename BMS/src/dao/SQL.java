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
	public static String name = "root";				//������ݿ������������
	public static String password = "root";

	//�������ݿ�
	public static Connection getConnection() {
		Connection con = null;
		try {
//			Driver driver = new Driver();
			Class.forName(Mysqldriver);			//��������
//			DriverManager.registerDriver(My);
			con = DriverManager.getConnection(Mysqlstr, name, password);				//����
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return con;
		}
	}
	
	//�ж�������˺��Ƿ�����˺����
	public static String Login(String username,String password) throws SQLException {
		//System.out.println("in");
		Connection con = getConnection();				//�������ݿ�
		//System.out.println("out"+con);
		String sql = "select identify from user where username=? and password=?";		//sql �����в�ѯ�û���������
		PreparedStatement pstm = con.prepareStatement(sql);
		pstm.setString(1, username);					//����1�����һ���ʺ�
		pstm.setString(2, password);					//����2����ڶ����ʺ�
		String reString ="";
		ResultSet rs = pstm.executeQuery();				//ִ��sql���֮��Ľ��������rs
		while(rs.next()) {
			reString=  rs.getString(1);						//ָ��ָ���һ��
		}
		pstm.close();
		con.close();				//�ر����ݿ�
		return reString;
	}
	
	//���
	public static boolean Query(String sql) throws SQLException {
		Connection con = getConnection();					//�������ݿ�
		Statement st = con.createStatement();
		boolean flag = st.execute(sql);
		st.close();
		con.close();
		return !flag;
		
	}
	
	//����
	public static List<Book> sel(String sql) throws SQLException{
		Connection con = getConnection();				//�������ݿ�
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);			//ִ��sql���֮��Ľ��������rs
		List<Book> data = new ArrayList<>();
		while(rs.next()) {
			Book book =new Book();					//�������ݿ��в�ѯ������Ϣ����Book��
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
		return data;					//���ز�ѯ������Ϣ
	}

	//����ȫ��
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
