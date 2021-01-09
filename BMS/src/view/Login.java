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
	//�ؼ�����
	private JLabel label1 = new JLabel("�û���");				//private JLable������ǩ��label�����û�����
	private JLabel label2 = new JLabel("��    ��");				//������ǩ��labe2�������롱
	//label1.setForeground(Color.RED);
	
	
	private JTextField text1 = new JTextField();				//private JTextField����text1 �û����ı���
	private JPasswordField text2 = new JPasswordField();		//text2�����ı���
	
	private JButton button1 = new JButton("��¼");				//��¼��ť
	private JButton button2 = new JButton("ע��");				//ע�ᰴť
	
	//���췽��
	Login(){						// ��¼���ڵ��������
		this.setTitle("��¼");		//�������
		this.setSize(400,400);		//�����С
		this.setLocation(200, 200);	//�������ꡢλ��
		this.setLayout(null);		//��ղ��ֹ�����
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		//setDefaultCloseOperation���趨���ڹر�ʱ����Ϊ�����ص�¼���ڣ�
		init();						//����init����
		this.add(label1);			//��ӱ�ǩ���
		this.add(label2);
		this.add(button1);			//��Ӱ�ť���
		this.add(button2);
		this.add(text1);			//����ı������
		this.add(text2);
		this.setVisible(true);			//��ʾ���
	}
	
	//�ؼ������Լ���ť���¼�����
	public void init() {							//init����ʼ��
		
		label1.setBounds(100, 100, 70, 30);			//��ǩ��λ�ü���С
		label2.setBounds(100, 150, 70, 30);
		
		text1.setBounds(180, 100, 120, 30);			//�ı����λ�ü���С
		text2.setBounds(180, 150, 120, 30);
		
		button1.setBounds(100, 200, 100, 30);		//��ť��λ�ü���С
		button2.setBounds(220, 200, 100, 30);
		
		button1.addActionListener(new ActionListener() {				//button1���¼�
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String username = text1.getText();				//text1��������ݣ��û�������username
				String password = text2.getText();				//text2��������ݣ����룩��password
				String identify="";								//����identify������������ֵ
			try {
					identify = SQL.Login(username, password);		//����SOL����login�¼�������������ݽ��бȶ��ж���ݲ��������ݸ���identify
			} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(identify.equals("����Ա")) {					//equals���Ƚ�
					dispose();									//�ر�һ�����ڣ���¼���ڣ�
					Main fMain =new Main();						//��ת������Ա����
				}
				else if(identify.equals("����")) {
					dispose();
					ReaderMain fMain =new ReaderMain(username);			//��ת�����߽���
				}
				else {
					JOptionPane.showMessageDialog(null, "�˺Ż����������");			//JOptionPane(�Ի���)���û�չʾ��Ϣ���˺Ż����������
				}
			}
		});
		button2.addActionListener(new ActionListener() {				//button2���¼�
			
			@Override
			public void actionPerformed(ActionEvent e) {				//actionPerformed:�������,ActionEvent:�����¼�
				// TODO Auto-generated method stub
				dispose();										//�ر�һ�����ڣ���¼��
				Register f = new Register();					//��ת��ע�����
			}
		});
		
	}
	public static void main(String[] args) {
		new Login();
	}
}
