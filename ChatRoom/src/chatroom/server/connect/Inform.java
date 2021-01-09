package chatroom.server.connect;

import chatroom.communal.InstructionSet;
import chatroom.communal.packet.RefreshDataPacket;
import chatroom.server.Main;

/**
 * @description: 处理服务器对客户端的通知
 * @author: Vcatory
 * @date: 2021-01-04 19:24
 */
public class Inform {

    /**
     * 私有化构造方法
     */

    private Inform() {

    }

    /**
     * addFriendInform方法：处理添加好友的通知
     *
     * @param connect     Connect           当前客户端
     * @param refreshData RefreshDataPacket 用户数据包
     */

    public static void addFriendInform(Connect connect, RefreshDataPacket refreshData) {
        // 将指令设置为更新指令
        refreshData.setInstruct(InstructionSet.REFRESH);
        // 将数据包发送给自己
        connect.write(refreshData);
        // 获取好友信息的账号
        String friendId = refreshData.getFriend().getId();
        // 将用户信息替换好友信息
        refreshData.setFriend(refreshData.getUser());
        // 遍历在线列表
        for (Connect temp : Main.connects) {
            // 判断在线用户账号是否为好友账号
            if (temp.getUserId().equals(friendId)) {
                // 将数据包发送给好友
                temp.write(refreshData);
            }
        }
    }

    /**
     * addGroupInform方法：处理添加群聊的通知
     *
     * @param userId      String            用户账号
     * @param refreshData RefreshDataPacket 更新数据包
     */

    public static void addGroupInform(String userId, RefreshDataPacket refreshData) {
        // 遍历在线列表
        for (Connect connect : Main.connects) {
            // 判断给客户端的账号是否为本客户端账号
            if (!connect.getUserId().equals(userId)) {
                // 将数据包发送给客户端
                connect.write(refreshData);
            }
        }
    }

    /**
     * onlineInform方法：用户更改登录状态通知，通知其他所有在线客户端
     *
     * @param userId      String            用户账号
     * @param refreshData RefreshDataPacket 数据包
     */

    public static void lineInform(String userId, RefreshDataPacket refreshData) {
        // 遍历在线列表
        for (Connect connect : Main.connects) {
            // 判断给客户端的账号是否为本客户端账号
            if (!connect.getUserId().equals(userId)) {
                // 将数据包发送给客户端
                connect.write(refreshData);
            }
        }

    }

}
