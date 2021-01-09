package chatroom.client.utils;

import chatroom.communal.javabeans.OthersInformation;

/**
 * 用户信息交换类：
 *
 * @description: 两个类之间交换用户信息
 * @author: Vcatory
 * @date: 2021-01-05 17:35
 */

public class OthersSwap {

    /**
     * 基本属性区域：
     */

    private static OthersInformation others;

    /**
     * 私有化构造方法
     */

    private OthersSwap() {

    }

    /**
     * setter方法区域
     */

    public static void setOthers(OthersInformation others) {
        OthersSwap.others = others;
    }

    /**
     * getter方法区域
     */

    public static OthersInformation getOthers() {
        return OthersSwap.others;
    }

}
