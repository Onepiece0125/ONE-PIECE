package chatroom.client.list;

import java.util.ArrayList;

/**
 * 在线成员列表
 *
 * @description: 保存在线成员列表
 * @author: Vcatory
 * @date: 2021-01-07 10:54
 */

public class OnlineList {

    /**
     * 基本属性区域
     */

    private static ArrayList<String> online = new ArrayList<>();

    /**
     * 私有化构造方法
     */

    private OnlineList() {

    }

    /**
     * setter方法
     */

    public static void setOnline(ArrayList<String> online) {
        OnlineList.online = online;
    }

    /**
     * getter方法
     */

    public static ArrayList<String> getOnline() {
        return online;
    }

}
