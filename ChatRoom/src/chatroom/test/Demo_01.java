package chatroom.test;

import chatroom.server.utils.JDBUtils;

/**
 * 测试类：
 *
 * @description: 测试数据库的连接和用户查询
 * @author: Vcatory
 * @date: 2020-12-17 9:44
 */
public class Demo_01 {

    public static void main(String[] args) {
        // 用户查询（方法一）
        System.out.println(JDBUtils.queryUserLogin("root"));
        // 用户查询（方法二）
        System.out.println(JDBUtils.queryUserLogin("root", "654321"));
        System.out.println(JDBUtils.queryUserLogin("lang", "123456"));
        System.out.println(JDBUtils.queryUserLogin("root", "123456"));
        // 释放资源
        JDBUtils.closingResource();
    }

}
