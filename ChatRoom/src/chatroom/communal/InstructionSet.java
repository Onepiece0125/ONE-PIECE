package chatroom.communal;

/**
 * 指令集合类：
 *
 * @description: 定义一些常用的指令
 * @author: Vcatory
 * @date: 2020-12-22 9:10
 */
public class InstructionSet {

    // 私有化构造方法
    private InstructionSet() {

    }

    /**
     * 指令集合
     */

    // 查询用户指令
    public static final int QUERY_USER = 1;
    // 查询群聊指令
    public static final int QUERY_GROUP = 2;
    // 登录指令
    public static final int QUERY_LOGIN = 3;
    // 注册指令
    public static final int REGISTER = 4;
    // 刷新指令
    public static final int REFRESH = 5;
    // 找回密码指令（账号和电话号码）
    public static final int QUERY_USER_INFORMATION_IH = 6;
    // 创建群聊指令
    public static final int CREATE_GROUP = 7;
    // 更新用户信息指令
    public static final int UPDATE_USER_INFORMATION = 8;
    // 查询用户信息（账号和电话号码）
    public static final int RETRIEVE = 9;
    // 获取信息指令（用户信息）
    public static final int GET_INFORMATION = 10;
    // 添加群聊指令
    public static final int ADD_GROUP = 11;
    // 添加好友指令
    public static final int ADD_FRIEND = 12;
    // 通知指令
    public static final int INFORM = 13;
    // 退出指令
    public static final int EXIT = 0;

    // 数据类型指令（字符）
    public static final int CHARACTER = 18;
    // 数据类型指令（非字符）
    public static final int NO_CHARACTER = 19;
    // 登录状态：离线
    public static final int OFF_LINE = 20;
    // 登录状态：在线
    public static final int ON_LINE = 21;
    // 文件状态：发送
    public static final int SEND = 25;
    // 文件状态：接受
    public static final int RECEIVE = 26;

    // 消息状态：新消息
    public static final int NEW_MESSAGE = 30;
    // 消息状态：旧消息
    public static final int OLD_MESSAGE = 31;

}
