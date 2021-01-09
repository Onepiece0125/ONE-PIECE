package chatroom.communal.packet;

import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.javabeans.UserInformation;

import java.io.Serializable;

/**
 * @description:
 * @author: Vcatory
 * @date: 2021-01-04 17:16
 */
public class RefreshDataPacket implements Serializable {

    /**
     * 基本属性区域
     */

    // 用户信息，用于更新用户信息
    private UserInformation userInformation;
    // 用户信息，用于添加好友
    private OthersInformation user;
    // 好友信息，用于添加好友
    private OthersInformation friend;
    // 群聊信息，用于查询，创建群聊和添加
    private GroupInformation groupInformation;
    // 操作指令
    private int instruct;


    /**
     * 构造方法区域
     */

    public RefreshDataPacket() {
    }

    public RefreshDataPacket(OthersInformation user, int instruct) {
        this.user = user;
        this.instruct = instruct;
    }

    public RefreshDataPacket(UserInformation userInformation, int instruct) {
        this.userInformation = userInformation;
        this.instruct = instruct;
    }

    public RefreshDataPacket(OthersInformation user, OthersInformation friend, int instruct) {
        this.user = user;
        this.friend = friend;
        this.instruct = instruct;
    }

    public RefreshDataPacket(GroupInformation groupInformation, int instruct) {
        this.groupInformation = groupInformation;
        this.instruct = instruct;
    }

    /**
     * setter方法区域
     */

    public void setGroupInformation(GroupInformation groupInformation) {
        this.groupInformation = groupInformation;
    }

    public void setFriend(OthersInformation friend) {
        this.friend = friend;
    }

    public void setUser(OthersInformation user) {
        this.user = user;
    }

    public void setInstruct(int instruct) {
        this.instruct = instruct;
    }

    /**
     * getter方法区域
     */

    public OthersInformation getUser() {
        return user;
    }

    public OthersInformation getFriend() {
        return friend;
    }

    public GroupInformation getGroupInformation() {
        return groupInformation;
    }

    public int getInstruct() {
        return instruct;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

}
