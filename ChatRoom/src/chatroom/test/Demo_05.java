package chatroom.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

/**
 * 测试类：
 *
 * @description: 测试TextInputDialog对话框
 * @author: Vcatory
 * @date: 2021-01-03 10:23
 */
public class Demo_05 extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("测试对话框");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("创建群聊");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("请输入群聊名称：");

        TilePane tilePane = new TilePane();

        Button button = new Button("Check");
        button.setOnAction(event -> dialog.show());

        tilePane.getChildren().add(button);

        primaryStage.setScene(new Scene(tilePane, 400, 300));
        primaryStage.show();
    }
}
