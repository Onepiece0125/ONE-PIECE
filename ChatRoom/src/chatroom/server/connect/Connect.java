package chatroom.server.connect;

import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.packet.FileDataPacket;
import chatroom.communal.packet.RefreshDataPacket;
import chatroom.communal.packet.UserDataPacket;
import chatroom.server.Main;
import chatroom.server.utils.CommandProcess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户连接端：
 *
 * @description: 处理服务器接受和发送消息
 * @author: Vcatory
 * @date: 2020-12-21 20:36
 */
public class Connect implements Runnable {

    /**
     * 基本属性区域
     */

    private final Socket connect;       // 客户端的连接
    private ObjectInputStream ois;      // 输入流
    private ObjectOutputStream oos;     // 输出流

    // 用户账号，唯一标识符
    private String userId;

    /**
     * 构造方法区域
     */

    public Connect(Socket connect) {
        this.connect = connect;
        try {
            ois = new ObjectInputStream(connect.getInputStream());
            oos = new ObjectOutputStream(connect.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter方法区域
     */

    public String getUserId() {
        return userId;
    }

    /**
     * read方法：接受一个对象
     *
     * @return Object  对象
     */

    private Object read() {
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * write方法：发送一个对象
     *
     * @param object Object 对象
     */

    public void write(Object object) {
        try {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close方法：下线并释放资源
     */

    private void close() {
        // 将该用户移除用户在线列表
        Main.connects.remove(this);
        // 遍历用户在线列表
        Main.traversalConnects();
        // 释放资源
        CommandProcess.close(oos, ois, connect);
    }

    /**
     * checkInstruct方法：检查用户数据包中的指令，并对数据包做出相应的处理
     *
     * @param userData DataPacket 用户数据包
     * @return boolean ture为继续 false为终止
     */

    public boolean checkUserDataPacket(UserDataPacket userData) {
        // 客户端申请退出请求
        if (userData.getInstruct() == InstructionSet.EXIT) {
            System.out.print("退出指令：");
            // 获取数据
            OthersInformation others = CommandProcess.updateIsOnline(this.userId, InstructionSet.OFF_LINE);
            if (others != null) {
                // 通知其他客户端
                Inform.lineInform(this.userId, new RefreshDataPacket(others, InstructionSet.INFORM));
            }
            // 打印结果
            System.out.println(others != null);
            return false;
        }
        // 客户端申请查询命令
        if (userData.getInstruct() == InstructionSet.QUERY_USER) {
            System.out.print("查询指令（用户查询）：");
            // 用户查询，true为存在
            boolean isExist = CommandProcess.processQueryUserLogin(userData.getUserLogin().getId());
            // 打印结果
            System.out.println(isExist);
            // 返回并处理查询结果
            write(!isExist);
        }
        // 客户端申请查询命令
        if (userData.getInstruct() == InstructionSet.QUERY_LOGIN) {
            System.out.print("查询指令（登录查询）：");
            // 用户查询，true为存在
            boolean isExist = CommandProcess.processQueryUserLogin(userData.getUserLogin().getId(),
                    userData.getUserLogin().getPassword());
            // 打印结果
            System.out.println(isExist);
            if (isExist) {
                // 设置唯一标识符
                this.userId = userData.getUserLogin().getId();
                // 将用户修改成在线状态，并写回数据库
                CommandProcess.updateIsOnline(this.userId, InstructionSet.ON_LINE);
                // 将该用户加入用户在线列表
                Main.connects.add(this);
                // 遍历用户在线列表
                Main.traversalConnects();
            }
            // 返回查询结果
            write(isExist);
        }
        // 客户端申请查询命令
        if (userData.getInstruct() == InstructionSet.QUERY_USER_INFORMATION_IH) {
            System.out.print("查询指令（信息查询）：");
            // 用户查询，true为存在
            boolean isExist = CommandProcess.processQueryUserInformation(userData.getUserInformation().getId(),
                    userData.getUserInformation().getPhoneNumber());
            // 打印结果
            System.out.println(isExist);
            // 返回并处理查询结果
            write(!isExist);
        }
        // 客户端申请获取信息
        if (userData.getInstruct() == InstructionSet.GET_INFORMATION) {
            System.out.print("获取信息指令：");
            // 获取结果
            UserDataPacket data = CommandProcess.processGetInformation(this.userId);
            // 打印结果
            System.out.println(data.getUserInformation() != null);
            // 返回结果
            write(data);
            // 创建更新数据包
            RefreshDataPacket refreshData = new RefreshDataPacket();
            // 将通知信息添加到客户端
            refreshData.setUser(
                    new OthersInformation(this.userId, data.getUserInformation().getName(), InstructionSet.ON_LINE));
            // 设置指令
            refreshData.setInstruct(InstructionSet.INFORM);
            // 通知其他客户端
            Inform.lineInform(this.userId, refreshData);
        }
        // 客服端申请注册请求
        if (userData.getInstruct() == InstructionSet.REGISTER) {
            System.out.print("注册指令：");
            System.out.println(CommandProcess.processRegister(userData.getUserLogin(),
                    userData.getUserInformation()));
        }
        // 客户端申请找回密码请求
        if (userData.getInstruct() == InstructionSet.RETRIEVE) {
            System.out.print("找回密码指令：");
            System.out.println(CommandProcess.processRetrieve(userData.getUserLogin()));
        }
        // 客户端申请群聊查询指令
        if (userData.getInstruct() == InstructionSet.QUERY_GROUP) {
            System.out.print("群聊查询指令：");
            // 群聊查询，true为存在
            boolean isExist = CommandProcess.processQueryGroup(userData.getGroupInformation());
            // 打印结果
            System.out.println(isExist);
            // 返回查询结果
            write(isExist);
        }
        return true;
    }

    /**
     * checkRefreshDataPacket方法：检查更新数据包中的指令，并对数据包做出相应的处理
     *
     * @param refreshData RefreshDataPacket 更新数据包
     */

    public void checkRefreshDataPacket(RefreshDataPacket refreshData) {
        // 客户端申请更新用户信息指令
        if (refreshData.getInstruct() == InstructionSet.UPDATE_USER_INFORMATION) {
            System.out.print("更新用户信息指令：");
            // 获取结果
            boolean isSucceed = CommandProcess.processUpdateUserInformation(refreshData.getUserInformation());
            System.out.println(isSucceed);
            if (isSucceed) {
                // 将指令设置为更新指令
                refreshData.setInstruct(InstructionSet.REFRESH);
                // 将数据包发送回去
                write(refreshData);
            }
        }
        // 客户端申请创建群聊指令
        if (refreshData.getInstruct() == InstructionSet.CREATE_GROUP) {
            System.out.print("创建群聊指令：");
            // 获取结果
            boolean isSucceed = CommandProcess.processCreateGroup(refreshData.getGroupInformation());
            System.out.println(isSucceed);
            if (isSucceed) {
                // 将指令设置为更新指令
                refreshData.setInstruct(InstructionSet.REFRESH);
                // 将数据包发送回去
                write(refreshData);
            }
        }
        // 客户端申请添加群聊指令
        if (refreshData.getInstruct() == InstructionSet.ADD_GROUP) {
            System.out.print("添加群聊指令：");
            // 获取群聊信息
            GroupInformation group = CommandProcess.processAddGroup(this.userId,
                    refreshData.getGroupInformation().getGroupId());
            if (group != null) {
                // 打印结果
                System.out.println("true");
                // 将群聊信息添加到数据包中
                refreshData.setGroupInformation(group);
                // 将指令设置为更新指令
                refreshData.setInstruct(InstructionSet.REFRESH);
                // 将数据包发送回去
                write(refreshData);
                // 将指令设置成通知指令
                refreshData.setInstruct(InstructionSet.INFORM);
                // 通知其他客户端
                Inform.addGroupInform(this.userId, refreshData);
            }
        }
        // 客户端申请添加好友指令
        if (refreshData.getInstruct() == InstructionSet.ADD_FRIEND) {
            System.out.print("添加好友指令：");
            // 获取好友信息
            OthersInformation others = CommandProcess.processAddFriend(this.userId, refreshData.getFriend().getId());
            if (others != null) {
                // 打印结果
                System.out.println("true");
                // 将好友信息添加到数据包中
                refreshData.setFriend(others);
                // 通知其他客户端
                Inform.addFriendInform(this, refreshData);
            }
        }
    }

    /**
     * checkFileDataPacket方法：处理文件数据包，解决群聊和私聊问题
     *
     * @param fileData FileDataPacket 文件数据包
     */

    private void checkFileDataPacket(FileDataPacket fileData) {
        // 私聊处理
        if (fileData.getReceiver().equals("root") || fileData.getReceiver().length() == 10) {
            // 遍历在线列表
            for (Connect connect : Main.connects) {
                // 判断账号
                if (connect.getUserId().equals(fileData.getReceiver())) {
                    // 发送数据
                    connect.write(fileData);
                }
            }
        }
        // 群聊处理
        if (fileData.getReceiver().length() == 11) {
            // 遍历在线列表
            for (Connect connect : Main.connects) {
                if (connect != this) {
                    // 发送数据
                    connect.write(fileData);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            // 读取数据
            Object obj = read();
            // 类型判断
            if (obj instanceof UserDataPacket) {
                // 用户信息处理
                if (!checkUserDataPacket((UserDataPacket) obj)) {
                    break;
                }
            } else {
                if (obj instanceof RefreshDataPacket) {
                    // 更新信息处理
                    checkRefreshDataPacket((RefreshDataPacket) obj);
                } else {
                    if (obj instanceof FileDataPacket) {
                        // 文件消息处理
                        checkFileDataPacket((FileDataPacket) obj);
                    } else {
                        // 终止线程
                        break;
                    }
                }
            }
        }
        // 释放资源
        close();
    }

}
