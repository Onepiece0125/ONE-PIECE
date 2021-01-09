package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.SQL;

public class Login extends JFrame{
	//控件定义
	private JLabel label1 = new JLabel("用户名");				//private JLable创建标签（label）“用户名”
	private JLabel label2 = new JLabel("密    码");				//创建标签（labe2）“密码”
	//label1.setForeground(Color.RED);
	
	
	private JTextField text1 = new JTextField();				//private JTextField创建text1 用户名文本框
	private JPasswordField text2 = new JPasswordField();		//text2密码文本框
	
	private JButton button1 = new JButton("登录");				//登录按钮
	private JButton button2 = new JButton("注册");				//注册按钮
	
	//构造方法
	Login(){						// 登录窗口的相关设置
		this.setTitle("登录");		//窗体标题
		this.setSize(400,400);		//窗体大小
		this.setLocation(200, 200);	//窗体坐标、位置
		this.setLayout(null);		//清空布局管理器
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		//setDefaultCloseOperation：设定窗口关闭时的行为（隐藏登录窗口）
		init();						//调用init方法
		this.add(label1);			//添加标签组件
		this.add(label2);
		this.add(button1);			//添加按钮组件
		this.add(button2);
		this.add(text1);			//添加文本框组件
		this.add(text2);
		this.setVisible(true);			//显示组件
	}
	
	//控件布局以及按钮的事件定义
	public void init() {							//init：初始化
		
		label1.setBounds(100, 100, 70, 30);			//标签的位置及大小
		label2.setBounds(100, 150, 70, 30);
		
		text1.setBounds(180, 100, 120, 30);			//文本框的位置及大小
		text2.setBounds(180, 150, 120, 30);
		
		button1.setBounds(100, 200, 100, 30);		//按钮的位置及大小
		button2.setBounds(220, 200, 100, 30);
		
		button1.addActionListener(new ActionListener() {				//button1的事件
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String username = text1.getText();				//text1输入的内容（用户名）给username
				String password = text2.getText();				//text2输入的内容（密码）给password
				String identify="";								//定义identify（鉴定）并赋值
			try {
					identify = SQL.Login(username, password);		//调用SOL包的login事件，对输入的内容进行比对判断身份并返回内容赋给identify
			} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(identify.equals("管理员")) {					//equals：比较
					dispose();									//关闭一个窗口（登录窗口）
					Main fMain =new Main();						//跳转到管理员界面
				}
				else if(identify.equals("读者")) {
					dispose();
					ReaderMain fMain =new ReaderMain(username);			//跳转到读者界面
				}
				else {
					JOptionPane.showMessageDialog(null, "账号或者密码错误");			//JOptionPane(对话框)向用户展示信息：账号或者密码错误
				}
			}
		});
		button2.addActionListener(new ActionListener() {				//button2的事件
			
			@Override
			public void actionPerformed(ActionEvent e) {				//actionPerformed:动作完成,ActionEvent:动作事件
				// TODO Auto-generated method stub
				dispose();										//关闭一个窗口（登录）
				Register f = new Register();					//跳转到注册界面
			}
		});
		
	}
	public static void main(String[] args) {
		new Login();
	}
}
