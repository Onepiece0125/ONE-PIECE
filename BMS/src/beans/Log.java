package beans;

import java.util.Date;

public class Log {

	public int id;				//������򼰵ڼ�������
	public String username;		//�û���
	public String bookid;		//����
	public Date time1;			//����ʱ��
	public Date time2;			//����ʱ��
	public String state;		//��״̬���ڽ裬�ѻ���
	
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
