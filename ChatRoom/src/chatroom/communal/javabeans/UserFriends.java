package chatroom.communal.javabeans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户好友：
 *
 * @description: 记录一个用户的好友
 * @author: Vcatory
 * @date: 2021-01-03 11:01
 */

public class UserFriends implements Serializable {

    /**
     * 基本属性区域：
     */

    private final String userId;      // 用户账号
    // 用户好友
    private final ArrayList<OthersInformation> friends = new ArrayList<>();


    /**
     * 构造方法区域
     */

    public UserFriends(String userId) {
        this.userId = userId;
    }

    /**
     * getter方法区域：
     */

    public String getUserId() {
        return userId;
    }

    public ArrayList<OthersInformation> getFriends() {
        return friends;
    }

    /**
     * traversalFriends方法：遍历好友列表
     *
     * @return String
     */

    private String traversalFriends() {
        StringBuilder sb = new StringBuilder();
        if (friends.size() != 0) {
            for (OthersInformation friend : friends) {
                sb.append("\n\t").append(friend);
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\t" + "好友列表：" + traversalFriends() + "\t";
    }
}
