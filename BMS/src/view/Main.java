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

public class Main extends JFrame {					//Main继承JFrame
	
	private JLabel[] labels = new JLabel[5];
	private JTextField[] texts = new JTextField[5];
	private String[] names = new String[] { "编号", "书名", "作者", "出版社", "库存" };		//表头
	private JTable table = new JTable();						//建一个表格
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane js = new JScrollPane(table);			//js滚动控件
	private JButton add = new JButton("增加");					//创建增加、删除、修改、查找、清空、查找全部按钮
	private JButton del = new JButton("删除");
	private JButton upd = new JButton("修改");
	private JButton fin = new JButton("查找");
	private JButton cle = new JButton("清空");
	private JButton finall = new JButton("查找全部");
	
	
	private JLabel[] labels2 = new JLabel[6];
	private JTextField[] texts2 = new JTextField[6];
	private String[] names2 = new String[] { "序号", "读者账号", "图书编号", "借书时间", "还书时间","状态" };			//表头
	private JTable table2 = new JTable();						//建一个表格
	private DefaultTableModel model2 = new DefaultTableModel();
	private JScrollPane js2 = new JScrollPane(table2);
	private JButton find = new JButton("查找");					//创建查找、查找全部按钮
	private JButton findall = new JButton("查找全部");
	
	
	private JPanel panel1 = new JPanel();						//创建面板（表格的面板）
	private JPanel panel2 = new JPanel();
	
	private JMenuBar jbBar = new JMenuBar();					//菜单栏
	private JMenu jm1 = new JMenu("图书信息管理");				//创建菜单一“图书信息管理”
	private JMenu jm2 = new JMenu("借书信息管理");				//创建菜单二“借书信息管理”
	private JMenu jm3 = new JMenu("退出");						//创建菜单三“退出”
	
	Main() {
		this.setTitle("图书管理系统");						//窗体标题
		this.setSize(800, 850);								//窗体大小
		this.setLocation(400, 100);							//窗体坐标
		this.setLayout(null);								//清空布局管理器
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		//setDefaultCloseOperation：设定窗口关闭时的行为   退出
		init();
		setJMenuBar(jbBar);
		this.add(panel1);							//添加面板一、二
		this.add(panel2);
		panel2.setVisible(false);
		this.setVisible(true);						//显示组件
	}
	
	
	// 得到界面输入的内容
	public Book getbook() {
		Book book = new Book();									//所有输入的内容写进book
		book.number = texts[0].getText();						//书号
		book.name = texts[1].getText();							//书名
		book.author = texts[2].getText();						//作者
		book.publisher = texts[3].getText();					//出版社
		try {
			book.count = Integer.parseInt(texts[4].getText());	//库存
		} catch (Exception e) {
			// TODO: handle exception
		}
		return book;
	}
	
	/**
	 *界面控制的布局，以及按钮功能的实现
	 */
	public void init() {
		jbBar.add(jm1);			//添加菜单一
		jbBar.add(jm2);			//添加菜单二
		jbBar.add(jm3);			//添加菜单三
		
		panel1.setBounds(10,10,780,780);		//面板一、二的位置及大小
		panel1.setLayout(null);
		panel2.setBounds(10,10,780,780);
		panel2.setLayout(null);
		
		js.setBounds(100, 100, 600, 400);		//滚动控件的位置
		
		//图书信息管理界面标签以及文本框的位置及大小
		for (int i = 0; i < names.length; i++) {		
			model.addColumn(names[i]);				//添加表头
			labels[i] = new JLabel(names[i]);		//标签的建立与命名
			labels[i].setBounds(130 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);

			texts[i] = new JTextField();			//新建文本框
			texts[i].setBounds(200 + i % 3 * 200, 550 + i / 3 * 50, 100, 30);
			
			panel1.add(labels[i]);					//标签添加到面板
			panel1.add(texts[i]);					//文本框添加到面板
		}
		table.setModel(model);

		add.setBounds(150, 650, 100, 30);
		del.setBounds(350, 650, 100, 30);
		upd.setBounds(550, 650, 100, 30);
		fin.setBounds(150, 700, 100, 30);
		cle.setBounds(350, 700, 100, 30);
		finall.setBounds(550, 700, 100, 30);
		
		panel1.add(add);		//增加，删除，修改，查找，清空，查找全部，滚动加入面板
		panel1.add(del);
		panel1.add(upd);
		panel1.add(fin);
		panel1.add(cle);
		panel1.add(finall);
		panel1.add(js);
		
		js2.setBounds(100, 100, 600, 400);
		
		for(int i=0;i<names.length;i++) {			//添加表头
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
		panel2.add(find);							//按钮加入面板
		panel2.add(findall);
		
	//增加
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("insert into book values('%s','%s','%s','%s','%d')", book.number, book.name,
						book.author, book.publisher, book.count);
				try {
					if (SQL.Query(sql)) {				//执行sql这句话
						JOptionPane.showMessageDialog(null, "增加成功");
					} else {
						JOptionPane.showMessageDialog(null, "增加失败");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "增加失败");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "增加失败");
				}
			}
		});
		
	//删除
		del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("delete from book where number='%s'", book.number);
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "删除成功");
					} else {
						JOptionPane.showMessageDialog(null, "删除失败");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "删除失败");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "删除失败");
				}
			}
		});

	//修改
		upd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				Book book = getbook();
				String sql = String.format("update book set name='%s',author='%s',publisher='%s',count='%d' where number='%s'", 
						book.name,book.author,book.publisher,book.count,book.number);
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "修改成功");
					} else {
						JOptionPane.showMessageDialog(null, "修改失败");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "修改失败");
				} catch (SQLException e1) {
					// TODO Auto-generated catch b
					JOptionPane.showMessageDialog(null, "修改失败");
				}

			}
		});

	//查找
		fin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				Book book = getbook();
				String sql = String.format("select * from book where number='%s'",book.number);
				try {
					List<Book> data = SQL.sel(sql);		//查到的信息存到集合（list）Book
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
	//清空
		cle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Book book = getbook();
				String sql = String.format("delete from book");
				try {
					if (SQL.Query(sql)) {
						JOptionPane.showMessageDialog(null, "清空成功");
					} else {
						JOptionPane.showMessageDialog(null, "清空失败");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "清空失败");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "清空失败");
				}
			}
		});

	//查找全部
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
	
		jm1.addMouseListener(new MouseListener() {			//菜单一图书信息管理
			
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
	
		jm2.addMouseListener(new MouseListener() {		//菜单二借书信息管理
			
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
				int result = JOptionPane.showConfirmDialog(null, "确认退出?", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(result == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
			}
		});
		
		
		//借书信息管理菜单的操作
	//查找
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
						model2.addRow(data.get(i).tObjects());		//查询到的信息全部显示出来
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
