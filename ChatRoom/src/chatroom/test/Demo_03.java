package chatroom.test;

import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.javabeans.UserLogin;
import chatroom.server.utils.JDBUtils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 测试类：
 *
 * @description: 测试JDBUtils类的insertUser方法和java.util.date对象转化成java.sql.date对象
 * @author: Vcatory
 * @date: 2020-12-20 14:53
 */
public class Demo_03 {

    public static void main(String[] args) {
        UserLogin userLogin = null;
        UserInformation userInformation = null;
        try {
            userLogin = new UserLogin("root", "123456");
            userInformation = new UserInformation("root", "Vcatory", 19, "男",
                    new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20").getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert userInformation != null;
        System.out.println(JDBUtils.insertUserLogin(userLogin));
        System.out.println(JDBUtils.insertUserInformation(userInformation));
        System.out.println(userInformation.getBirthday());
    }

}
