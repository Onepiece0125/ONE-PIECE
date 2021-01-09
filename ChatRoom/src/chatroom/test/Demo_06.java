package chatroom.test;

import chatroom.communal.InstructionSet;
import chatroom.communal.packet.UserDataPacket;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.javabeans.UserLogin;
import chatroom.server.utils.CommandProcess;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 测试类：
 *
 * @description: 向服务器发送数据包
 * @author: Vcatory
 * @date: 2020-12-21 20:02
 */
public class Demo_06 {

    public static void main(String[] args) {
        System.out.println("--------------Client--------------");
        Socket socket = null;
        // ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 与服务器建立链接
            socket = new Socket("localhost", 8888);
            // 创建对象流
            // ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            // 创建数据
            UserLogin userLogin = null;
            UserInformation userInformation = null;
            try {
                userLogin = new UserLogin("root", "123456");
                userInformation = new UserInformation("root", "Vcatory", 19, "男",
                        new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 创建数据包
            UserDataPacket userDataPacket = new UserDataPacket(userLogin, userInformation, InstructionSet.REGISTER);
            // 发送数据包
            // objectIO.write(dataPacket);
            oos.writeObject(userDataPacket);
            oos.writeObject(new UserDataPacket(InstructionSet.EXIT));
            // 打印数据
            System.out.println(userLogin + "\n" + userInformation);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源和关闭连接
            // if (objectIO != null) {
            //     objectIO.sendClose();
            // }
            CommandProcess.close(oos, socket);
        }
    }

}
