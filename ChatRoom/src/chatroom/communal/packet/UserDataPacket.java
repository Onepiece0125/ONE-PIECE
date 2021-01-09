package chatroom.communal.packet;

import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.UserFriends;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.javabeans.UserLogin;

import java.io.Serializable;
import java.util.List;

/**
 * 用户数据包：
 *
 * @description: 用于服务器和客服端之间，接受和发送用户数据
 * @author: Vcatory
 * @date: 2020-12-21 19:03
 */
public class UserDataPacket implements Serializable {

    /**
     * 基本数据区域
     */

    private UserLogin userLogin;                // 登录信息
    private UserInformation userInformation;    // 用户信息
    private UserFriends userFriends;            // 好友列表
    private List<GroupInformation> groups;      // 群聊信息列表
    // 添加账号，用于添加好友或群聊
    private String id;
    // 群聊信息，用于查询和创建群聊
    private GroupInformation groupInformation;
    // 操作指令
    private int instruct;

    /**
     * 构造方法区域
     */

    public UserDataPacket() {
    }

    public UserDataPacket(int instruct) {
        this.instruct = instruct;
    }

    public UserDataPacket(String id, int instruct) {
        this.id = id;
        this.instruct = instruct;
    }

    public UserDataPacket(UserLogin userLogin, int instruct) {
        this.userLogin = userLogin;
        this.instruct = instruct;
    }

    public UserDataPacket(UserInformation userInformation, int instruct) {
        this.userInformation = userInformation;
        this.instruct = instruct;
    }

    public UserDataPacket(GroupInformation groupInformation, int instruct) {
        this.groupInformation = groupInformation;
        this.instruct = instruct;
    }

    public UserDataPacket(UserLogin userLogin, UserInformation userInformation, int instruct) {
        this.userLogin = userLogin;
        this.userInformation = userInformation;
        this.instruct = instruct;
    }

    /**
     * setter方法区域
     */

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public void setUserFriends(UserFriends userFriends) {
        this.userFriends = userFriends;
    }

    public void setGroups(List<GroupInformation> groups) {
        this.groups = groups;
    }

    /**
     * getter方法区域
     */

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public int getInstruct() {
        return instruct;
    }

    public UserFriends getUserFriends() {
        return userFriends;
    }

    public List<GroupInformation> getGroups() {
        return groups;
    }

    public String getId() {
        return id;
    }

    public GroupInformation getGroupInformation() {
        return groupInformation;
    }

    /**
     * traversalGroups方法：遍历群聊信息
     *
     * @return String
     */

    private String traversalGroups() {
        StringBuilder sb = new StringBuilder();
        if (groups.size() != 0) {
            sb.append(groups.get(0));
            for (int i = 1; i < groups.size(); i++) {
                sb.append("\n").append(groups.get(i));
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    /**
     * check方法：检查对象是否为null
     *
     * @param obj Object 检查对象
     * @return String 对象为null，返回空串
     */

    private String check(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    @Override
    public String toString() {
        return check(userLogin) + "\n\n" + check(userInformation) + "\n\n" + check(userFriends) + "\n\n" + traversalGroups();
    }
}


