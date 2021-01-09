package chatroom.client.launchs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 找回密码加载类：
 *
 * @description: 加载和关闭找回密码界面
 * @author: Vcatory
 * @date: 2020-12-22 20:18
 */
public class RetrieveLaunch extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 新建窗口
        Stage nowStage = new Stage();
        // 获取Parent对象
        Parent root = FXMLLoader.load(getClass().getResource("/chatroom/client/fxml/FX_Retrieve.fxml"));
        // 设置标题
        nowStage.setTitle("找回密码");
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
