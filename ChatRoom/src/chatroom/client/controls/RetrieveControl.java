package chatroom.client.controls;

import chatroom.client.launchs.LoginLaunch;
import chatroom.client.terminal.ReceivingEnd;
import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.client.utils.VerificationCode;
import chatroom.communal.InstructionSet;
import chatroom.communal.packet.UserDataPacket;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.javabeans.UserLogin;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.regex.Pattern;


/**
 * 找回密码界面：
 *
 * @description: 实现相关的事件和合法检查
 * @author: Vcatory
 * @date: 2020-12-22 19:43
 */

public class RetrieveControl {

    @FXML
    private TextField codeField;

    @FXML
    private TextField phoneNumber;

    @FXML
    private PasswordField newPwd;

    @FXML
    private ImageView verCodeImage;

    @FXML
    private PasswordField confirmPwd;

    @FXML
    private Label promptMessage;

    @FXML
    private TextField userId;

    // 验证码
    private String verCode;


    @FXML
    void registerAction() {
        if (check()) {
            // 获取终端
            Terminal terminal = Singleton.get();
            // 获取发送端和接收端
            SendingEnd sendingEnd = terminal.getSendingEnd();
            ReceivingEnd receivingEnd = terminal.getReceivingEnd();
            // 创建匹配信息
            UserInformation userInformation = new UserInformation(userId.getText(), phoneNumber.getText());
            // 判断账号和电话号码是否匹配
            if (check(sendingEnd, receivingEnd, userInformation)) {
                // 创建对象
                UserLogin userLogin = new UserLogin(userId.getText(), newPwd.getText());
                // 发送数据
                sendData(sendingEnd, userLogin);
                // 启动线程，返回登录窗口
                Platform.runLater(() -> {
                    // 关闭注册窗口
                    ((Stage) promptMessage.getScene().getWindow()).close();
                    try {
                        // 加载登录窗口
                        new LoginLaunch().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                promptMessage.setText("账号或电话号码错误");
            }
        }
    }

    @FXML
    void verCodeMouseClicked() {
        // 更新验证码和图片
        updateVerificationCode();
    }

    @FXML
    void initialize() {
        // 初始化验证码和图片
        updateVerificationCode();
    }

    /**
     * updateVerificationCode方法：更新验证码和图片
     */

    private void updateVerificationCode() {
        VerificationCode verificationCode = new VerificationCode();
        verCodeImage.setImage(SwingFXUtils.toFXImage(verificationCode.getImage(), null));
        verCode = verificationCode.getCode();
    }

    /**
     * check方法：对用户信息进行合法检查
     *
     * @return boolean  ture为合法，false为非法
     */

    private boolean check() {
        // 判断空值
        if (userId.getText().equals("") || newPwd.getText().equals("") ||
                confirmPwd.getText().equals("") || codeField.getText().equals("")) {
            promptMessage.setText("需要填写的信息不能为空（除电话）");
            return false;
        }
        // 账号判断
        if (!userId.getText().equals("root") && !Pattern.matches("\\d{10}", newPwd.getText())) {
            promptMessage.setText("账号不足10位或输入非数字字符");
            return false;
        }
        // 电话号码判断
        if (!phoneNumber.getText().equals("") && !Pattern.matches("\\d{11}", phoneNumber.getText())) {
            promptMessage.setText("电话号码不足11位或输入非数字字符");
            return false;
        }
        // 用户密码判断
        if (newPwd.getText().length() <= 20) {
            if (!newPwd.getText().equals(confirmPwd.getText())) {
                promptMessage.setText("输入的密码不匹配");
                return false;
            }
        } else {
            promptMessage.setText("用户密码过长，最多只能20个字符");
            return false;
        }
        // 验证码判断
        if (!codeField.getText().equals(verCode)) {
            promptMessage.setText("验证码错误");
            updateVerificationCode();
            return false;
        }
        return true;
    }

    /**
     * sendData方法：向服务器发送修改后的账号和密码
     *
     * @param sendingEnd SendingEnd 发送端
     * @param userLogin  UserLogin  数据
     */

    private void sendData(SendingEnd sendingEnd, UserLogin userLogin) {
        // 创建数据包
        UserDataPacket userData = new UserDataPacket(userLogin, InstructionSet.RETRIEVE);
        // 发送数据包
        sendingEnd.writeUserData(userData);
    }

    /**
     * check方法：向服务器发送匹配信息，与数据库进行匹配
     *
     * @param sendingEnd      SendingEnd      发送端
     * @param receivingEnd    ReceivingEnd    接收端
     * @param userInformation UserInformation 匹配信息
     * @return boolean ture为成功匹配
     */

    private boolean check(SendingEnd sendingEnd, ReceivingEnd receivingEnd, UserInformation userInformation) {
        // 创建对象
        UserDataPacket userData = new UserDataPacket(userInformation, InstructionSet.QUERY_USER_INFORMATION_IH);
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
        UserInformation userInformation = new UserInformation(userId.getText(), phoneNumber.getText());
        if (check(ois, oos, userInformation)) {
            // 创建对象
            UserLogin userLogin = new UserLogin(userId.getText(), newPwd.getText());
            // 发送数据
            sendData(oos, userLogin);
            // 启动线程，返回登录窗口
            Platform.runLater(() -> {
                // 关闭注册窗口
                ((Stage) promptMessage.getScene().getWindow()).close();
                try {
                    // 加载登录窗口
                    new LoginLaunch().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            promptMessage.setText("账号或电话号码错误");
        }
        // 向服务器申请退出请求
        oos.writeObject(new UserDataPacket(InstructionSet.EXIT));
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        CommandProcess.close(ois, oos, socket);
    }
 */

/*
 * 暂时废弃：
    private boolean check(ObjectInputStream ois, ObjectOutputStream oos, UserInformation userInformation) {
        // 创建对象
        UserDataPacket userData = new UserDataPacket(userInformation, InstructionSet.QUERY_USER_INFORMATION_IH);
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

    private void sendData(ObjectOutputStream oos, UserLogin userLogin) {
        try {
            // 创建数据包
            UserDataPacket userData = new UserDataPacket(userLogin, InstructionSet.RETRIEVE);
            // 发送数据包
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 */