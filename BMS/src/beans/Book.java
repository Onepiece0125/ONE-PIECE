package beans;

public class Book {
	public String number;		//编号
	public String name;			//书名
	public String author;		//作者
	public String publisher;	//出版社
	public int count;			//库存
	
	public Object[] tObjects() {
		Object[] ob= new Object[5];
		ob[0]=number;
		ob[1]=name;
		ob[2]=author;
		ob[3]=publisher;
		ob[4]=count;
		return ob;
	}
}
