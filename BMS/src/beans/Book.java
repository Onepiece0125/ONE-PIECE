package beans;

public class Book {
	public String number;		//���
	public String name;			//����
	public String author;		//����
	public String publisher;	//������
	public int count;			//���
	
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
