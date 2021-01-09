package chatroom.client.controls;

import chatroom.client.launchs.MainStageLaunch;
import chatroom.client.launchs.RegisterLaunch;
import chatroom.client.launchs.RetrieveLaunch;
import chatroom.client.terminal.ReceivingEnd;
import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.communal.InstructionSet;
import chatroom.communal.packet.UserDataPacket;
import chatroom.communal.javabeans.UserLogin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.regex.Pattern;

/**
 * 登录界面：
 *
 * @description: 实现用户登录，账号密码合法检查，及其他的相关事件
 * @author: Vcatory
 * @date: 2020-12-17 13:07
 */

public class LoginControl {

    @FXML
    private TextField userIdentification;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button registerButton;

    @FXML
    private Button retrieveButton;

    @FXML
    private Label promptMessage;

    @FXML
    void loginAction() {
        if (check()) {
            // 获取终端
            Terminal terminal = Singleton.get();
            // 获取发送端和接收端
            SendingEnd sendingEnd = terminal.getSendingEnd();
            ReceivingEnd receivingEnd = terminal.getReceivingEnd();
            // 创建匹配信息
            UserLogin userLogin = new UserLogin(userIdentification.getText(), userPassword.getText());
            // 判断账号和密码是否匹配
            if (check(sendingEnd, receivingEnd, userLogin)) {
                // 设置唯一标识符
                terminal.setUserId(userIdentification.getText());
                // 启动线程
                Platform.runLater(() -> {
                    try {
                        // 加载主界面窗口
                        new MainStageLaunch().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 关闭当前窗口
                    ((Stage) promptMessage.getScene().getWindow()).close();
                });
            } else {
                promptMessage.setText("账号或密码错误");
            }
        }
    }

    @FXML
    void registerAction() {
        Platform.runLater(() -> {
            // 获取按钮所在的窗口
            Stage primaryStage = (Stage) registerButton.getScene().getWindow();
            // 窗口隐藏
            primaryStage.hide();
            try {
                // 加载注册窗口
                new RegisterLaunch().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void registerMouseEntered() {
        registerButton.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void registerMouseExited() {
        registerButton.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void retrieveAction() {
        Platform.runLater(() -> {
            // 获取按钮所在的窗口
            Stage primaryStage = (Stage) retrieveButton.getScene().getWindow();
            // 窗口隐藏
            primaryStage.hide();
            try {
                // 加载注册窗口
                new RetrieveLaunch().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void retrieveMouseEntered() {
        retrieveButton.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void retrieveMouseExited() {
        retrieveButton.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void initialize() {
        assert userIdentification != null : "fx:id=\"userIdentification\" was not injected: check your FXML file 'FX_Login.fxml'.";
        assert userPassword != null : "fx:id=\"userPassword\" was not injected: check your FXML file 'FX_Login.fxml'.";
    }

    /**
     * check方法：对用户信息进行合法检查
     *
     * @return boolean  ture为合法，false为非法
     */

    private boolean check() {
        // 判断空值
        if (userIdentification.getText().equals("") || userPassword.getText().equals("")) {
            promptMessage.setText("账号或密码不能为空");
            return false;
        }
        // 账号判断
        if (!userIdentification.getText().equals("root") && !Pattern.matches("\\d{10}", userIdentification.getText())) {
            promptMessage.setText("账号不足10位或输入非数字字符");
            return false;
        }
        return true;
    }

    /**
     * check方法：向服务器发送匹配信息，与数据库进行匹配
     *
     * @param sendingEnd   SendingEnd   发送端
     * @param receivingEnd ReceivingEnd 接收端
     * @param userLogin    UserLogin    匹配信息
     * @return boolean ture为成功匹配
     */

    private boolean check(SendingEnd sendingEnd, ReceivingEnd receivingEnd, UserLogin userLogin) {
        // 创建对象
        UserDataPacket userData = new UserDataPacket(userLogin, InstructionSet.QUERY_LOGIN);
        // 发送指令包
        sendingEnd.writeUserData(userData);
        // 接受处理结果
        Object obj = receivingEnd.readObject();
        // 返回结果值
        if (obj instanceof Boolean) {
            return (boolean) obj;
        }
        return false;
    }

}

/*
 * 注：暂时废弃
    // 初始化对象
    Socket socket = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    try {
        // 建立连接
        socket = new Socket("localhost", 8888);
        // 创建流
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        UserLogin userLogin = new UserLogin(userIdentification.getText(), userPassword.getText());
        if (check(ois, oos, userLogin)) {
            // 获取字节数组
            byte[] bytes = userIdentification.getText().getBytes();
            // 写入字节数组
            DataSwap.write(bytes);
            // 关闭输出流
            DataSwap.closeOutput();
            // 启动线程
            Platform.runLater(() -> {
                try {
                    // 加载主界面窗口
                    new MainStageLaunch().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 关闭当前窗口
                ((Stage) promptMessage.getScene().getWindow()).close();
            });
        } else {
            promptMessage.setText("账号或密码错误");
        }
        // 向服务器申请退出请求
        oos.writeObject(new UserDataPacket(InstructionSet.EXIT));
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        // 释放资源
        CommandProcess.close(ois, oos, socket);
    }
 */

/*
 * 注：暂时废弃
    private boolean check(ObjectInputStream ois, ObjectOutputStream oos, UserLogin userLogin) {
        // 创建对象
        UserDataPacket userData = new UserDataPacket(userLogin, InstructionSet.QUERY_LOGIN);
        try {
            // 发送数据
            oos.writeObject(userData);
            // 接受处理结果
            Object obj = ois.readObject();
            // 返回结果值
            if (obj instanceof Boolean) {
                return (boolean) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
 */
