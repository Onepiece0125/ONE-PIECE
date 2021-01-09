package chatroom.server;

import chatroom.server.connect.Connect;
import chatroom.server.utils.CommandProcess;
import chatroom.server.utils.JDBUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务端：
 *
 * @description: 接受处理客户端的请求
 * @author: Vcatory
 * @date: 2020-12-17 10:42
 */

public class Main {

    /**
     * 基本属性区域：
     */

    // 在线用户列表
    public static CopyOnWriteArrayList<Connect> connects = new CopyOnWriteArrayList<>();

    /**
     * traversalConnects方法：遍历在线用户
     */

    public static void traversalConnects() {
        System.out.println('\n' + "--在线用户列表：");
        if (connects.size() != 0) {
            for (Connect temp : connects) {
                System.out.println("--" + temp.getUserId());
            }
        } else {
            System.out.println("null");
        }
        System.out.println();
    }

    /**
     * main方法：启动服务器，并接受客户端的指令和数据，并加以处理
     *
     * @param args String[]
     */

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("--------------Server--------------");
        // 定义套接字服务器
        ServerSocket server = null;
        try {
            // 创建套接字服务器
            server = new ServerSocket(8888);
            // 连接数据库
            System.out.println("数据库连接：" + JDBUtils.createConnection("root", "123456"));
            // 死循环：永远等待用户请求
            while (true) {
                // 阻塞式接受客户端的请求
                Socket connect = server.accept();
                System.out.println("建立连接");
                // 创建客户端连接，并启动线程
                new Thread(new Connect(connect)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CommandProcess.close(server);
        }
    }

}


/*
 * 注：暂时废弃
 *
 * if (instruct.equals("register")) {
 *          System.out.println("注册");
 *          ObjectIO objectIO = new ObjectIO(socket);
 *          System.out.println(1);
 *          ArrayList<Object> list = new ArrayList<>();
 *          Object object;
 *          while ((object = objectIO.read()) != null) {
 *              list.add(object);
 *          }
 *          UserLogin userLogin = null;
 *          UserInformation userInformation = null;
 *          for (Object temp : list) {
 *              if (temp instanceof UserLogin) {
 *                  userLogin = (UserLogin) temp;
 *              }
 *              if (temp instanceof UserInformation) {
 *                  userInformation = (UserInformation) temp;
 *              }
 *          }
 *          objectIO.close();
 *          System.out.println(userLogin + "\n" + userInformation);
 *      }
 */

/* 注：暂时废弃
 * threadPool.submit(() -> {
 *     ObjectInputStream ois = null;
 *     try {
 *         ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
 *         while (true) {
 *             DataPacket dataPacket = null;
 *             Object object = ois.readObject();
 *             if (object instanceof DataPacket) {
 *                 dataPacket = (DataPacket) object;
 *             }
 *             if (dataPacket == null || !checkInstruct(dataPacket)) {
 *                 break;
 *             }
 *         }
 *     } catch (IOException | ClassNotFoundException e) {
 *         e.printStackTrace();
 *     } finally {
 *         CommandProcess.close(ois);
 *     }
 * });
 */