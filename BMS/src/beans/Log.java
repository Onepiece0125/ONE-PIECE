package beans;

import java.util.Date;

public class Log {

	public int id;				//借书次序及第几个借书
	public String username;		//用户名
	public String bookid;		//书编号
	public Date time1;			//借书时间
	public Date time2;			//还书时间
	public String state;		//书状态（在借，已还）
	
	public Object[] tObjects() {
		Object[] objects = new Object[6];
		objects[0]=id;
		objects[1]=username;
		objects[2]=bookid;
		objects[3]=time1;
		objects[4]=time2;
		objects[5]=state;
		return objects;
	}
}
