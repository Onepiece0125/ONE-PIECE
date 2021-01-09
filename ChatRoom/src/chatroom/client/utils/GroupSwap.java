package chatroom.client.utils;

import chatroom.communal.javabeans.GroupInformation;

/**
 * 群聊信息交换类：
 *
 * @description: 两个类之间交换群聊信息
 * @author: Vcatory
 * @date: 2021-01-07 10:10
 */
public class GroupSwap {

    /**
     * 基本属性区域
     */

    private static GroupInformation group;

    /**
     * 私有化构造方法
     */

    private GroupSwap() {

    }

    /**
     * setter方法区域
     */

    public static void setGroup(GroupInformation group) {
        GroupSwap.group = group;
    }

    /**
     * getter方法区域
     */

    public static GroupInformation getGroup() {
        return group;
    }

}
