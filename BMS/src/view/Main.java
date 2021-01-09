package view;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
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

public class Main extends JFrame {					//Main�̳�JFrame
	
	private JLabel[] labels = new JLabel[5];
	private JTextField[] texts = new JTextField[5];
	private String[] names = new String[] { "���", "����", "����", "������", "���" };		//��ͷ
	private JTable table = new JTable();						//��һ�����
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane js = new JScrollPane(table);			//js�����ؼ�
	private JButton add = new JButton("����");					//�������ӡ�ɾ�����޸ġ����ҡ���ա�����ȫ����ť
	private JButton del = new JButton("ɾ��");
	private JButton upd = new JButton("�޸�");
	private JButton fin = new JButton("����");
	private JButton cle = new JButton("���");
	private JButton finall = new JButton("����ȫ��");
	
	
	private JLabel[] labels2 = new JLabel[6];
	private JTextField[] texts2 = new JTextField[6];
	private String[] names2 = new String[] { "���", "�����˺�", "ͼ����", "����ʱ��", "����ʱ��","״̬" };			//��ͷ
	private JTable table2 = new JTable();						//��һ�����
	private DefaultTableModel model2 = new DefaultTableModel();
	private JScrollPane js2 = new JScrollPane(table2);
	private JButton find = new JButton("����");					//�������ҡ�����ȫ����ť
	private JButton findall = new JButton("����ȫ��");
	
	
	private JPanel panel1 = new JPanel();						//������壨������壩
	private JPanel panel2 = new JPanel();
	
	private JMenuBar jbBar = new JMenuBar();					//�˵���
	private JMenu jm1 = new JMenu("ͼ����Ϣ����");				//�����˵�һ��ͼ����Ϣ����
	private JMenu jm2 = new JMenu("������Ϣ����");				//�����˵�����������Ϣ����
	private JMenu jm3 = new JMenu("�˳�");						//�����˵������˳���
	
	Main() {
		this.setTitle("ͼ�����ϵͳ");						//�������
		this.setSize(800, 850);								//�����С
		this.setLocation(400, 100);							//��������
		this.setLayout(null);								//��ղ��ֹ�����
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		//setDefaultCloseOperation���趨���ڹر�ʱ����Ϊ   �˳�
		init();
		setJMenuBar(jbBar);
		this.add(panel1);							//������һ����
		this.add(panel2);
		panel2.setVisible(false);
		this.setVisible(true);						//��ʾ���
	}
	
	
	// �õ��������������
	public Book getbook() {
		Book book = new Book();									//�������������д��book
		book.number = texts[0].getText();						//���
		book.name = texts[1].getText();							//����
		book.author = texts[2].getText();						//����
		book.publisher = texts[3].getText();					//������
		try {
			book.count = Integer.parseInt(texts[4].getText());	//���
		} catch (Exception e) {
			// TODO: handle exception
		}
		return book;
	}
	
	/**
	 *������ƵĲ��֣��Լ���ť���ܵ�ʵ��
	 */
	public void init() {
		jbBar.add(jm1);			//��Ӳ˵�һ
		jbBar.add(jm2);			//��Ӳ˵���
		jbBar.add(jm3);			//��Ӳ˵���
		
		panel1.setBounds(10,10,780,780);		//���һ������λ�ü���С
		panel1.setLayout(null);
		panel2.setBounds(10,10,780,780);
		panel2.setLayout(null);
		
		js.setBounds(100, 100, 600, 400);		//�����ؼ���λ��
		
		//ͼ����Ϣ��������ǩ�Լ��ı����λ�ü���С
		for (int i = 0; i < names.length; i++) {		
			model.addColumn(names[i]);				//��ӱ�ͷ
			labels[i] = new JLabel(names[i]);		//��ǩ�Ľ���������
			labels[i].setBounds(130 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);

			texts[i] = new JTextField();			//�½��ı���
			texts[i].setBounds(200 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);
			
			panel1.add(labels[i]);					//��ǩ��ӵ����
			panel1.add(texts[i]);					//�ı�����ӵ����
		}
		table.setModel(model);

		add.setBounds(150, 650, 100, 30);
		del.setBounds(350, 650, 100, 30);
		upd.setBounds(550, 650, 100, 30);
		fin.setBounds(150, 700, 100, 30);
		cle.setBounds(350, 700, 100, 30);
		finall.setBounds(550, 700, 100, 30);
		
		panel1.add(add);		//���ӣ�ɾ�����޸ģ����ң���գ�����ȫ���������������
		panel1.add(del);
		panel1.add(upd);
		panel1.add(fin);
		panel1.add(cle);
		panel1.add(finall);
		panel1.add(js);
		
		js2.setBounds(100, 100, 600, 400);
		
		for(int i=0;i<names.length;i++) {			//��ӱ�ͷ
			model2.addColumn(names2[i]);
		}
		for (int i = 1; i < 3; i++) {
			
			labels2[i] = new JLabel(names2[i]);
			labels2[i].setBounds(230 + (i-1) % 3 * 200, 550 + (i-1)  / 3 * 50, 100, 30);

			texts2[i] = new JTextField();
			texts2[i].setBounds(300 + (i-1)  % 3 * 200, 550 + (i-1)  / 3 * 50, 100, 30);
			
			panel2.add(labels2[i]);
			panel2.add(texts2[i]);
		}
		table2.setModel(model2);
		find.setBounds(250, 600, 100, 30);
		findall.setBounds(400, 600, 100, 30);
		
		panel2.add(js2);
		panel2.add(find);							//��ť�������
		panel2.add(findall);
		
	//����
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("insert into book values('%s','%s','%s','%s','%d')", book.number, book.name,
						book.author, book.publisher, book.count);
				try {
					if (SQL.Query(sql)) {				//ִ��sql��仰
						JOptionPane.showMessageDialog(null, "���ӳɹ�");
					} else {
						JOptionPane.showMessageDialog(null, "����ʧ��");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "����ʧ��");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "����ʧ��");
				}
			}
		});
		
	//ɾ��
		del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("delete from book where number='%s'", book.number);
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
					} else {
						JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
				}
			}
		});

	//�޸�
		upd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				Book book = getbook();
				String sql = String.format("update book set name='%s',author='%s',publisher='%s',count='%d' where number='%s'", 
						book.name,book.author,book.publisher,book.count,book.number);
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "�޸ĳɹ�");
					} else {
						JOptionPane.showMessageDialog(null, "�޸�ʧ��");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "�޸�ʧ��");
				} catch (SQLException e1) {
					// TODO Auto-generated catch b
					JOptionPane.showMessageDialog(null, "�޸�ʧ��");
				}

			}
		});

	//����
		fin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				Book book = getbook();
				String sql = String.format("select * from book where number='%s'",book.number);
				try {
					List<Book> data = SQL.sel(sql);		//�鵽����Ϣ�浽���ϣ�list��Book
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
	//���
		cle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("delete from book");
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "��ճɹ�");
					} else {
						JOptionPane.showMessageDialog(null, "���ʧ��");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "���ʧ��");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "���ʧ��");
				}
			}
		});

	//����ȫ��
		finall.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				Book book = getbook();
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
	
		jm1.addMouseListener(new MouseListener() {			//�˵�һͼ����Ϣ����
			
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
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				panel1.setVisible(true);
				panel2.setVisible(false);
			}
		});
	
		jm2.addMouseListener(new MouseListener() {		//�˵���������Ϣ����
			
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
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				panel1.setVisible(false);
				panel2.setVisible(true);
			}
		});
	
		jm3.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null, "ȷ���˳�?", "ȷ��", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
			}
		});
		
		
		//������Ϣ����˵��Ĳ���
	//����
		find.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String username=texts2[1].getText();
				String bookid = texts2[2].getText();
				
				String sql =String .format("select * from log where username='%s' or bookid='%s'", username,bookid);
				try {
					List<Log> data = SQL.sel_log(sql);
					model2.setRowCount(0);
					for(int i=0;i<data.size();i++) {
						model2.addRow(data.get(i).tObjects());		//��ѯ������Ϣȫ����ʾ����
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
				String username=texts2[1].getText();
				String bookid = texts2[2].getText();
				
				String sql =String .format("select * from log");
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
		new Main();
	}
}
