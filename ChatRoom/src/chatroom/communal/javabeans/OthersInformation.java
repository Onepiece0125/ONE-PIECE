package chatroom.communal.javabeans;

import chatroom.communal.InstructionSet;

import java.io.Serializable;

/**
 * 其他人信息
 *
 * @description: 存储好友以及群聊成员的基本信息
 * @author: Vcatory
 * @date: 2021-01-04 8:30
 */

public class OthersInformation implements Serializable {

    /**
     * 基本属性区域
     */

    private String id;          // 用户账号
    private String name;        // 用户名称
    private int line;       // 登陆状态

    /**
     * 构造方法区域
     */

    public OthersInformation() {
    }

    public OthersInformation(String id) {
        this.id = id;
    }

    public OthersInformation(String id, String name, int line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }

    /**
     * setter方法区域
     */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLine(int line) {
        this.line = line;
    }

    /**
     * getter方法区域
     */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    /**
     * checkLine方法：检查登录状态
     *
     * @return String 登陆状态
     */

    private String checkLine() {
        // 登陆状态：离线
        if (line == InstructionSet.OFF_LINE) {
            return "离线";
        }
        // 登陆状态：在线
        if (line == InstructionSet.ON_LINE) {
            return "在线";
        }
        return "未知";
    }

    @Override
    public String toString() {
        return "\t" + "用户账号：" + id + "\t" +
                "用户名称：" + name + "\t" +
                "登录状态：" + checkLine() + "\t";
    }
}
