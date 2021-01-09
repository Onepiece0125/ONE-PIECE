package chatroom.test;

import java.util.regex.Pattern;

/**
 * 测试类：
 *
 * @description: 测试正则表达式
 * @author: Vcatory
 * @date: 2021-01-02 16:35
 */
public class Demo_04 {

    public static void main(String[] args) {
        System.out.println(Pattern.matches("\\d{11}", "17680143850"));
        System.out.println(Pattern.matches(
                "[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}",
                "3108183112@qq.com"));
    }

}
