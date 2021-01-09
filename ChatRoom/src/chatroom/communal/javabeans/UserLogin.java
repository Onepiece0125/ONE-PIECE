package chatroom.communal.javabeans;

import java.io.Serializable;

/**
 * 登录信息：
 *
 * @description: 保存一个用户的登录信息
 * @author: Vcatory
 * @date: 2020-12-20 19:23
 */

public class UserLogin implements Serializable {

    /**
     * 基本属性区域
     */

    private final String id;                // 用户账号
    private String password;                // 用户密码
    private int isOnline;                   // 用户在线

    public UserLogin(String id) {
        this.id = id;
    }

    public UserLogin(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public UserLogin(String id, String password, int isOnline) {
        this.id = id;
        this.password = password;
        this.isOnline = isOnline;
    }

    /**
     * getter方法区域
     */

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getIsOnline() {
        return isOnline;
    }

    // 生成登录状态
    private String createIsOnline() {
        // 离线状态
        if (isOnline == 0) {
            return "离线";
        }
        // 在线状态
        if (isOnline == 1) {
            return "在线";
        }
        // 不确定
        return "未知";
    }

    @Override
    public String toString() {
        return "\t" + "账号：" + id + "\t"
                + "密码：" + password + "\t"
                + "登录状态：" + createIsOnline() + "\t";
    }

}
