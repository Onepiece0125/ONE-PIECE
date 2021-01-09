package view;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.SQL;

public class Register extends JFrame{
	//控件定义
	private JLabel label1 = new JLabel("用户名");
	private JLabel label2 = new JLabel("密    码");
	
	private JTextField text1 = new JTextField();
	private JTextField text2 = new JTextField();
	
	private JButton button1 = new JButton("注册");
	private JButton button2 = new JButton("取消");
	Register(){
		this.setTitle("注册");
		this.setSize(400,400);
		this.setLocation(200, 200);
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		init();
		this.add(label1);
		this.add(label2);
		this.add(button1);
		this.add(button2);
		this.add(text1);
		this.add(text2);
		this.setVisible(true);
	}
	
	//控件布局以及按钮的事件定义
	public void init() {
		
		label1.setBounds(100, 100, 70, 30);
		label2.setBounds(100, 150, 70, 30);
		
		text1.setBounds(180, 100, 120, 30);
		text2.setBounds(180, 150, 120, 30);
		
		button1.setBounds(100, 200, 100, 30);
		button2.setBounds(220, 200, 100, 30);
		
		button1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//得到输入的内容
				String username = text1.getText();
				String password = text2.getText();
				String identify="读者";
				String sql = String.format("insert into user values('%s','%s','%s')", username,password,identify);			//根据输入内容编写SQL语句
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "注册成功");
					}
					else {
						JOptionPane.showMessageDialog(null, "注册失败");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "注册失败");			//异常则返回“注册失败”
				}
				
			}
		});
		
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				Login fLogin = new Login();			//取消注册则返回登录界面
			}
		});
	}
	public static void main(String[] args) {
		new Register();
	}
}
