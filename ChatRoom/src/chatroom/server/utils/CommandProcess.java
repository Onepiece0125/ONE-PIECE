package chatroom.server.utils;

import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.*;
import chatroom.communal.packet.UserDataPacket;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 命令处理类：
 *
 * @description: 处理服务器接受到的指令
 * @author: Vcatory
 * @date: 2020-12-20 19:21
 */
public class CommandProcess {

    // 私有化构造方法
    private CommandProcess() {

    }

    /**
     * processQueryUser方法：处理查询用户指令，根据用户账号，查询用户是否存在
     *
     * @param userID String 用户账号
     * @return boolean ture为存在
     */

    public static boolean processQueryUserLogin(String userID) {
        return JDBUtils.queryUserLogin(userID);
    }

    /**
     * processQueryUser方法：处理查询用户指令，根据用户账号和密码，查询用户是否存在
     *
     * @param userID  String 用户账号
     * @param userPwd String 用户密码
     * @return boolean true为通过
     */
    public static boolean processQueryUserLogin(String userID, String userPwd) {
        return JDBUtils.queryUserLogin(userID, userPwd);
    }

    /**
     * * processQueryUserInformation方法：处理查询用户指令，根据用户账号和电话号码，查询用户是否存在
     *
     * @param userID      String 用户账号
     * @param phoneNumber String 电话号码
     * @return boolean true为存在
     */

    public static boolean processQueryUserInformation(String userID, String phoneNumber) {
        return JDBUtils.queryUserInformation(userID, phoneNumber);
    }

    /**
     * processRegister方法：处理注册指令，分别将登录信息和用户信息插入数据库
     *
     * @param userLogin       UserLogin       登录信息
     * @param userInformation UserInformation 注册信息
     * @return boolean true为成功
     */

    public static boolean processRegister(UserLogin userLogin, UserInformation userInformation) {
        return JDBUtils.insertUserLogin(userLogin) && JDBUtils.insertUserInformation(userInformation);
    }

    /**
     * processRetrieve方法：处理找回密码指令，修改数据库中的登录信息
     *
     * @param userLogin UserLogin   登录信息
     * @return boolean true为成功
     */

    public static boolean processRetrieve(UserLogin userLogin) {
        return JDBUtils.updateUserLogin(userLogin);
    }

    /**
     * processGetInformation方法：处理获取信息指令，从数据库中获取用户信息，好友列表和群聊信息
     *
     * @param userId String 用户账号
     * @return DataPacket 数据包
     */

    public static UserDataPacket processGetInformation(String userId) {
        // 创建数据包
        UserDataPacket userData = new UserDataPacket();
        // 获取用户信息，并添加到数据包中
        userData.setUserInformation(JDBUtils.getUserInformation(userId));
        // 创建好友列表
        UserFriends friends = new UserFriends(userId);
        // 遍历好友账号列表
        for (String temp : JDBUtils.getUserFriends(userId)) {
            // 获取好友信息
            friends.getFriends().add(JDBUtils.getOthersInformation(temp));
        }
        // 将好友列表加入到数据包
        userData.setUserFriends(friends);
        // 创建群聊信息
        ArrayList<GroupInformation> groups = new ArrayList<>();
        // 获取群聊信息（群主）
        JDBUtils.getGroups(
                new OthersInformation(userId, userData.getUserInformation().getName(), InstructionSet.ON_LINE), groups);
        // 获取群主信息（成员）
        for (String temp : JDBUtils.getGroups(userId)) {
            groups.add(JDBUtils.getGroup(temp));
        }
        // 遍历群聊信息
        for (GroupInformation group : groups) {
            // 获取成员列表
            JDBUtils.getNumbers(group);
        }
        // 将群聊信息添加到数据包
        userData.setGroups(groups);
        // 返回数据包
        return userData;
    }

    /**
     * processUpdateUserInformation方法：处理更新用户信息指令，在数据库中更新用户信息
     *
     * @param userInformation UserInformation 用户信息
     * @return boolean true为成功
     */

    public static boolean processUpdateUserInformation(UserInformation userInformation) {
        return JDBUtils.updateUserInformation(userInformation);
    }

    /**
     * processCreateGroup方法：处理创建群聊指令，向数据库插入一条群聊信息记录
     *
     * @param groupInformation GroupInformation 群聊信息
     * @return boolean true为成功
     */

    public static boolean processCreateGroup(GroupInformation groupInformation) {
        return JDBUtils.insertGroupInformation(groupInformation);
    }

    /**
     * processQueryGroup方法：处理查询群聊指令，在数据库中查询该账号是否存在
     *
     * @param groupInformation GroupInformation 群聊信息
     * @return boolean true为存在
     */

    public static boolean processQueryGroup(GroupInformation groupInformation) {
        return JDBUtils.queryGroupInformation(groupInformation.getGroupId());
    }

    /**
     * processAddGroup方法：处理添加群聊指令，向数据库插入一条群聊关系记录
     *
     * @param userId  String 用户账号
     * @param groupId String 群聊账号
     * @return GroupInformation 群聊信息
     */

    public static GroupInformation processAddGroup(String userId, String groupId) {
        // 判断是否添加成功
        if (JDBUtils.queryGroupInformation(groupId) && JDBUtils.insertGroupRelationship(groupId, userId)) {
            // 获取群聊信息
            GroupInformation group = JDBUtils.getGroup(groupId);
            // 获取成员信息
            JDBUtils.getNumbers(group);
            // 返回结果
            return group;
        }
        return null;
    }

    /**
     * processAddFriend方法：处理添加好友指令，向数据库中插入一条好友关系记录
     *
     * @param userId   String 用户账号
     * @param friendId String 好友账号
     * @return OthersInformation 好友信息
     */

    public static OthersInformation processAddFriend(String userId, String friendId) {
        // 判断是否添加成功
        if (JDBUtils.queryUserLogin(friendId) && JDBUtils.insertFriendRelationship(userId, friendId)
                && JDBUtils.insertFriendRelationship(friendId, userId)) {
            return JDBUtils.getOthersInformation(friendId);
        }
        return null;
    }

    /**
     * updateIsOnline方法：修改登录状态，用于登录和下线
     *
     * @param userId   String 用户账号
     * @param isOnline int    登录状态 20为离线 21为在线
     * @return OthersInformation 用户信息
     */

    public static OthersInformation updateIsOnline(String userId, int isOnline) {
        // 判断是否修改成功
        if (JDBUtils.updateUserLogin(userId, isOnline)) {
            // 返回结果
            return JDBUtils.getOthersInformation(userId);
        }
        return null;
    }

    /**
     * close方法：释放资源
     *
     * @param targets Closeable...  需要释放资源的对象
     */

    public static void close(Closeable... targets) {
        for (Closeable target : targets) {
            if (target != null) {
                try {
                    target.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
