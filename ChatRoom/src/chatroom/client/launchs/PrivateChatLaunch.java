package chatroom.client.launchs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 私聊窗口加载类
 *
 * @description: 加载和关闭私聊窗口
 * @author: Vcatory
 * @date: 2021-01-05 16:00
 */

public class PrivateChatLaunch extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 获取Parent对象
        Parent root = FXMLLoader.load(getClass().getResource("/chatroom/client/fxml/FX_PrivateChat.fxml"));
        // 设置标题
        primaryStage.setTitle("私聊");
        // 设置Icon图标
        primaryStage.getIcons().add(new Image("file:src/chatroom/client/images/ChatRoom.png"));
        // 设置顶层容器
        primaryStage.setScene(new Scene(root));
        // 设置窗口大小不可变
        primaryStage.setResizable(false);
        // 显示窗口
        primaryStage.show();
    }

}
