package chatroom.client.terminal;

import chatroom.communal.packet.FileDataPacket;
import chatroom.communal.packet.RefreshDataPacket;
import chatroom.communal.packet.UserDataPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 发送端：
 *
 * @description: 为客户端发送数据
 * @author: Vcatory
 * @date: 2020-12-27 20:26
 */
public class SendingEnd {

    /**
     * 基本属性区域：
     */

    private ObjectOutputStream oos;     //输出端

    /**
     * 构造方法区域：
     */

    public SendingEnd(OutputStream oos) {
        try {
            this.oos = new ObjectOutputStream(oos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter方法区域：
     */

    public ObjectOutputStream getObjectOutputStream() {
        return oos;
    }

    /**
     * writeUserData方法：向服务器发送一个用户数据包
     *
     * @param userData UserDataPacket 用户数据包
     */

    public void writeUserData(UserDataPacket userData) {
        try {
            // 发送数据
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writeUserData方法：向服务器发送一个更新数据包
     *
     * @param refreshData RefreshDataPacket 更新数据包
     */

    public void writeRefreshData(RefreshDataPacket refreshData) {
        try {
            // 发送数据
            oos.writeObject(refreshData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writeFileData方法：向服务器发送一个文件数据包
     *
     * @param fileData FileDataPacket 文件数据包
     */

    public void writeFileData(FileDataPacket fileData) {
        try {
            // 发送数据
            oos.writeObject(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
