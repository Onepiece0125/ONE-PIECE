package chatroom.client.launchs;

import chatroom.client.terminal.Singleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 主界面加载类：
 *
 * @description: 加载和关闭主界面
 * @author: Vcatory
 * @date: 2020-12-23 19:24
 */
public class MainStageLaunch extends Application {

    public void start(Stage primaryStage) throws Exception {
        // 获取Parent对象
        Parent root = FXMLLoader.load(getClass().getResource("/chatroom/client/fxml/FX_MainStage.fxml"));
        // 设置标题
        primaryStage.setTitle("ChatRoom");
        // 设置Icon图标
        primaryStage.getIcons().add(new Image("file:src/chatroom/client/images/ChatRoom.png"));
        // 设置顶层容器
        primaryStage.setScene(new Scene(root));
        // 设置窗口大小不可变
        primaryStage.setResizable(false);
        // 设置关闭事件
        primaryStage.setOnCloseRequest(event -> {
            // 关闭终端
            Singleton.remove();
            // 关闭线程
            Singleton.get().setRunning(false);
            // 关闭窗口
            primaryStage.close();
        });
        // 显示窗口
        primaryStage.show();
    }

}
