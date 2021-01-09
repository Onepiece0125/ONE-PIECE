package view;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import beans.Book;
import beans.Log;
import dao.SQL;

public class ReaderMain extends JFrame {

	private String username="";			//��¼���û���
	
		//�ؼ�
	
	//�軹��
	private JLabel[] labels = new JLabel[5];
	private JTextField[] texts = new JTextField[5];
	private String[] names = new String[] { "���", "����", "����", "������", "���" };
	private String[] _names = new String[] { "���", "����ʱ��"};		// ��ǩ��
	private JTable table = new JTable();			//��һ�����
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane js = new JScrollPane(table);			//�����ؼ�
	
	private JButton find = new JButton("����");					//������ť
	private JButton borrow = new JButton("����");
	private JButton reback = new JButton("����");
	private JButton findall = new JButton("����ȫ��");
	
	//�Լ��Ľ����¼
	private JLabel[] labels2 = new JLabel[6];
	private JTextField[] texts2 = new JTextField[6];
	private String[] names2 = new String[] { "���", "�����˺�", "ͼ����", "����ʱ��", "����ʱ��","״̬" };
	private JTable table2 = new JTable();							//��һ�����
	private DefaultTableModel model2 = new DefaultTableModel();
	private JScrollPane js2 = new JScrollPane(table2);

	private JButton fin = new JButton("����");					//������ť
	private JButton finall = new JButton("����ȫ��");
	
	private JPanel panel1 = new JPanel();		//�����������
	private JPanel panel2 = new JPanel();
	
	private JMenuBar jbBar = new JMenuBar();	//�˳�
	private JMenu jm1 = new JMenu("�˳�");

	ReaderMain(String username) {
		this.username = username;
		this.setTitle("ͼ�����ϵͳ");		//���ڱ���
		this.setSize(1500, 850);			//���ڴ�С
		this.setLocation(400, 100);			//����λ��
		this.setLayout(null);				//��ղ��ֹ�����
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		////setDefaultCloseOperation���趨���ڹر�ʱ����Ϊ   �˳�
		init();
		setJMenuBar(jbBar);				//���  �˳�
		this.add(panel1);
		this.add(panel2);
		this.setVisible(true);			//��ʾ��������ڣ�
	}

	/**
	 *������ƵĲ��֣��Լ���ť���ܵ�ʵ��
	 */

	public void init() {
		jbBar.add(jm1);				//��Ӳ˵����˳���
		panel1.setBounds(10,10,700,780);			//���һ����λ��
		panel1.setLayout(null);
		panel2.setBounds(710,10,700,780);
		panel2.setLayout(null);
		
		js.setBounds(200, 100, 500, 400);			//�����ؼ���λ��
		
		for (int i = 0; i < names.length; i++) {
			model.addColumn(names[i]);					//��ӱ�ͷ����һ��
		}
		
		//��һ����ǩ���ı�����������
		for (int i = 0; i < _names.length; i++) {
			
			labels[i] = new JLabel(_names[i]);
			labels[i].setBounds(230 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);

			texts[i] = new JTextField();
			texts[i].setBounds(300 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);
			
			panel1.add(labels[i]);
			panel1.add(texts[i]);
		}
		table.setModel(model);				//�̶���һ�仰�����
		//��ť��λ�ü���С
		fin.setBounds(150, 600, 100, 30);
		borrow.setBounds(300,600,100,30);
		reback.setBounds(450, 600, 100, 30);
		finall.setBounds(600, 600, 100, 30);
		//��ť�����
		panel1.add(fin);
		panel1.add(borrow);
		panel1.add(reback);
		panel1.add(finall);
		panel1.add(js);
		
		js2.setBounds(200, 100, 500, 400);
		
		for(int i=0;i<names2.length;i++) {				//��ӱ�ͷ�������
			model2.addColumn(names2[i]);
		}
	//�������ǩ���ı�����������
		for (int i = 2; i < 3; i++) {
			
			labels2[i] = new JLabel(names2[i]);
			labels2[i].setBounds(150 + (i-1) % 3 * 200, 550 + (i-1)  / 3 * 50, 100, 30);

			texts2[i] = new JTextField();
			texts2[i].setBounds(250 + (i-1)  % 3 * 200, 550 + (i-1)  / 3 * 50, 100, 30);
			
			panel2.add(labels2[i]);
			panel2.add(texts2[i]);
		}
		table2.setModel(model2);
		find.setBounds(350, 600, 100, 30);
		findall.setBounds(500, 600, 100, 30);
		
		panel2.add(js2);			//��ӹ����ؼ�
		panel2.add(find);			//��Ӳ��Ұ�ť
		panel2.add(findall);		//��Ӳ���ȫ����ť
		

		jm1.addMouseListener(new MouseListener() {			//�˳�
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {			//�˳�
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null, "ȷ���˳�?", "ȷ��", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
			}
		});
		
		//����
		fin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String number=texts[0].getText();
				String sql = String.format("select * from book where number='%s'",number);
				try {
					List<Book> data = SQL.sel(sql);
					model.setRowCount(0);
					for(int i=0;i<data.size();i++) {
						model.addRow(data.get(i).tObjects());
					}
				}
				catch (SQLException e1) {
					// TODO: handle exception
				}
				
			}
		});

		//����
		borrow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Log log = new Log();
				log.username=username;
				log.bookid=texts[0].getText();
				log.time1 = new Date(System.currentTimeMillis());
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd"); 			//����ʱ��
				try {
					log.time2=simple.parse(texts[1].getText());
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				log.state = "�ڽ�";				//״̬
				String sql=String.format("insert into log values(default,'%s','%s','%tF','%tF','%s')", log.username,log.bookid,log.time1,log.time2,log.state);
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "����ɹ�");
					}else {
						JOptionPane.showMessageDialog(null, "����ʧ��");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "����ʧ��");
				}
			}
		});
		
		//����
		reback.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String bookid = texts[0].getText();
				String state="�ѻ�";
				String sql=String.format("update log set state='%s' where bookid=%s and username='%s'", state,bookid,username);
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "����ɹ�");
					}
					else {
						JOptionPane.showMessageDialog(null, "����ʧ��");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "����ʧ��");
				}
			}
		});
		
		//����ȫ��
		finall.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String sql = String.format("select * from book");
				try {
					List<Book> data = SQL.sel(sql);
					model.setRowCount(0);
					for(int i=0;i<data.size();i++) {
						model.addRow(data.get(i).tObjects());
					}
				}
				catch (SQLException e1) {
					// TODO: handle exception
				}
			}
		});
		
		//����
		find.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String bookid= texts2[2].getText();
				
				String sql =String.format("select * from log where username='%s' and bookid='%s'", username,bookid);
				try {
					List<Log> data = SQL.sel_log(sql);
					model2.setRowCount(0);
					for(int i=0;i<data.size();i++) {
						model2.addRow(data.get(i).tObjects());
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//����ȫ��
		findall.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String sql =String .format("select * from log where username='%s'",username);
				try {
					List<Log> data = SQL.sel_log(sql);
					model2.setRowCount(0);
					for(int i=0;i<data.size();i++) {
						model2.addRow(data.get(i).tObjects());
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
	}
	public static void main(String[] args) {
		new ReaderMain("123");
	}
}
