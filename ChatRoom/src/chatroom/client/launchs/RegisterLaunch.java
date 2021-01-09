package chatroom.client.launchs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 注册界面加载类：
 *
 * @description: 加载和关闭注册界面
 * @author: Vcatory
 * @date: 2020-12-20 10:49
 */
public class RegisterLaunch extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 新建窗口
        Stage nowStage = new Stage();
        // 获取Parent对象
        Parent root = FXMLLoader.load(getClass().getResource("/chatroom/client/fxml/FX_Register.fxml"));
        // 设置标题
        nowStage.setTitle("注册账号");
        // 设置Icon图标
        nowStage.getIcons().add(new Image("file:src/chatroom/client/images/ChatRoom.png"));
        // 设置顶层容器
        nowStage.setScene(new Scene(root));
        // 设置窗口大小不可变
        nowStage.setResizable(false);
        // 设置关闭事件
        nowStage.setOnCloseRequest(event -> Platform.runLater(() -> {
            // 显示隐藏窗口
            primaryStage.show();
            // 关闭窗口
            nowStage.close();
        }));
        // 显示窗口
        nowStage.show();
    }

}
