package chatroom.client;

import chatroom.client.launchs.LoginLaunch;
import chatroom.client.terminal.Singleton;
import javafx.application.Application;

/**
 * 客户端：
 *
 * @description: 向服务器发送请求
 * @author: Vcatory
 * @date: 2020-12-17 10:42
 */

public class Main {

    public static void main(String[] args) {
        System.out.println("--------------Client--------------");
        // 获取终端
        Singleton.get();
        // 加载登录窗口
        Application.launch(LoginLaunch.class, args);
    }
}
