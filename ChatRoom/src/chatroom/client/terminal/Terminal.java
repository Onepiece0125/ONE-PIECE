package chatroom.client.terminal;

import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.packet.UserDataPacket;
import chatroom.server.utils.CommandProcess;

import java.io.IOException;
import java.net.Socket;

/**
 * 终端：
 *
 * @description: 封装客户端的发送端和接收端等一些信息
 * @author: Vcatory
 * @date: 2020-12-28 8:22
 */
public class Terminal {

    /**
     * 基本属性区域：
     */

    private String userId;              // 唯一标识
    private Socket client;              // 客户端
    private SendingEnd sendingEnd;      // 发送端
    private ReceivingEnd receivingEnd;  // 接收端

    // 用户信息
    private UserInformation userInformation;
    // 线程运行状态
    private boolean isRunning = true;

    /**
     * 构造方法区域：
     */

    public Terminal() {
        createTerminal();
    }

    /**
     * setter方法区域：
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * getter方法区域：
     */

    public String getUserId() {
        return userId;
    }

    public Socket getClient() {
        return client;
    }

    public SendingEnd getSendingEnd() {
        return sendingEnd;
    }

    public ReceivingEnd getReceivingEnd() {
        return receivingEnd;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * createTerminal方法：生成终端
     */

    private void createTerminal() {
        try {
            client = new Socket("localhost", 8888);
            sendingEnd = new SendingEnd(client.getOutputStream());
            receivingEnd = new ReceivingEnd(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close方法：释放资源
     */

    public void close() {

        // 向服务器申请退出请求
        sendingEnd.writeUserData(new UserDataPacket(InstructionSet.EXIT));
        // 关闭连接
        CommandProcess.close(sendingEnd.getObjectOutputStream(),
                receivingEnd.getObjectInputStream(), client);
    }

}

/*
 * 注意：暂时废弃
// 判断文件路径
// if (MessageList.getFile() != null) {
//     // 保存数据
//     saveFile();
// }
    private void saveFile() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(MessageList.getFile()));
            ChatRecord record = new ChatRecord();
            record.setFileList(MessageList.getFileList());
            oos.writeObject(record);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/
