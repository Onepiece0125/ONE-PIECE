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

	private String username="";			//登录的用户名
	
		//控件
	
	//借还书
	private JLabel[] labels = new JLabel[5];
	private JTextField[] texts = new JTextField[5];
	private String[] names = new String[] { "编号", "书名", "作者", "出版社", "库存" };
	private String[] _names = new String[] { "编号", "还书时间"};		// 标签名
	private JTable table = new JTable();			//建一个表格
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane js = new JScrollPane(table);			//滚动控件
	
	private JButton find = new JButton("查找");					//创建按钮
	private JButton borrow = new JButton("借书");
	private JButton reback = new JButton("还书");
	private JButton findall = new JButton("查找全部");
	
	//自己的借书记录
	private JLabel[] labels2 = new JLabel[6];
	private JTextField[] texts2 = new JTextField[6];
	private String[] names2 = new String[] { "序号", "读者账号", "图书编号", "借书时间", "还书时间","状态" };
	private JTable table2 = new JTable();							//建一个表格
	private DefaultTableModel model2 = new DefaultTableModel();
	private JScrollPane js2 = new JScrollPane(table2);

	private JButton fin = new JButton("查找");					//创建按钮
	private JButton finall = new JButton("查找全部");
	
	private JPanel panel1 = new JPanel();		//建立两个面板
	private JPanel panel2 = new JPanel();
	
	private JMenuBar jbBar = new JMenuBar();	//退出
	private JMenu jm1 = new JMenu("退出");

	ReaderMain(String username) {
		this.username = username;
		this.setTitle("图书管理系统");		//窗口标题
		this.setSize(1500, 850);			//窗口大小
		this.setLocation(400, 100);			//窗口位置
		this.setLayout(null);				//清空布局管理器
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		////setDefaultCloseOperation：设定窗口关闭时的行为   退出
		init();
		setJMenuBar(jbBar);				//添加  退出
		this.add(panel1);
		this.add(panel2);
		this.setVisible(true);			//显示组件（窗口）
	}

	/**
	 *界面控制的布局，以及按钮功能的实现
	 */

	public void init() {
		jbBar.add(jm1);				//添加菜单（退出）
		panel1.setBounds(10,10,700,780);			//面板一二的位置
		panel1.setLayout(null);
		panel2.setBounds(710,10,700,780);
		panel2.setLayout(null);
		
		js.setBounds(200, 100, 500, 400);			//滚动控件的位置
		
		for (int i = 0; i < names.length; i++) {
			model.addColumn(names[i]);					//添加表头（表一）
		}
		
		//表一：标签，文本框的相关设置
		for (int i = 0; i < _names.length; i++) {
			
			labels[i] = new JLabel(_names[i]);
			labels[i].setBounds(230 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);

			texts[i] = new JTextField();
			texts[i].setBounds(300 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);
			
			panel1.add(labels[i]);
			panel1.add(texts[i]);
		}
		table.setModel(model);				//固定的一句话（表格）
		//按钮的位置及大小
		fin.setBounds(150, 600, 100, 30);
		borrow.setBounds(300,600,100,30);
		reback.setBounds(450, 600, 100, 30);
		finall.setBounds(600, 600, 100, 30);
		//按钮的添加
		panel1.add(fin);
		panel1.add(borrow);
		panel1.add(reback);
		panel1.add(finall);
		panel1.add(js);
		
		js2.setBounds(200, 100, 500, 400);
		
		for(int i=0;i<names2.length;i++) {				//添加表头（表二）
			model2.addColumn(names2[i]);
		}
	//表二：标签，文本框的相关设置
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
		
		panel2.add(js2);			//添加滚动控件
		panel2.add(find);			//添加查找按钮
		panel2.add(findall);		//添加查找全部按钮
		

		jm1.addMouseListener(new MouseListener() {			//退出
			
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
			public void mouseClicked(MouseEvent e) {			//退出
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null, "确认退出?", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
			}
		});
		
		//查找
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

		//借书
		borrow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Log log = new Log();
				log.username=username;
				log.bookid=texts[0].getText();
				log.time1 = new Date(System.currentTimeMillis());
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd"); 			//还书时间
				try {
					log.time2=simple.parse(texts[1].getText());
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				log.state = "在借";				//状态
				String sql=String.format("insert into log values(default,'%s','%s','%tF','%tF','%s')", log.username,log.bookid,log.time1,log.time2,log.state);
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "借书成功");
					}else {
						JOptionPane.showMessageDialog(null, "借书失败");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "借书失败");
				}
			}
		});
		
		//还书
		reback.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String bookid = texts[0].getText();
				String state="已还";
				String sql=String.format("update log set state='%s' where bookid=%s and username='%s'", state,bookid,username);
				try {
					if(SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "还书成功");
					}
					else {
						JOptionPane.showMessageDialog(null, "还书失败");
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "还书失败");
				}
			}
		});
		
		//查找全部
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
		
		//查找
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
		
		//查找全部
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
