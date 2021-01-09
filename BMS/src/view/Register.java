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
	//�ؼ�����
	private JLabel label1 = new JLabel("�û���");
	private JLabel label2 = new JLabel("��    ��");
	
	private JTextField text1 = new JTextField();
	private JTextField text2 = new JTextField();
	
	private JButton button1 = new JButton("ע��");
	private JButton button2 = new JButton("ȡ��");
	Register(){
		this.setTitle("ע��");
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
	
	//�ؼ������Լ���ť���¼�����
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
				//�õ����������
				String username = text1.getText();
				String password = text2.getText();
				String identify="����";
				String sql = String.format("insert into user values('%s','%s','%s')", username,password,identify);			//�����������ݱ�дSQL���
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "ע��ɹ�");
					}
					else {
						JOptionPane.showMessageDialog(null, "ע��ʧ��");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "ע��ʧ��");			//�쳣�򷵻ء�ע��ʧ�ܡ�
				}
				
			}
		});
		
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				Login fLogin = new Login();			//ȡ��ע���򷵻ص�¼����
			}
		});
	}
	public static void main(String[] args) {
		new Register();
	}
}
