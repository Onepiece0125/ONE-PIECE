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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * 注册界面控制：
 *
 * @description: 实现相关的事件和合法检查
 * @author: Vcatory
 * @date: 2020-12-17 19:34
 */
public class RegisterControl {

    @FXML
    private DatePicker birthday;        // 生日选择区

    @FXML
    private TextField userName;         // 用户昵称文本区

    @FXML
    private TextField userPassword;     // 用户密码文本区

    @FXML
    private TextField confirmPwd;       // 确认密码文本区

    @FXML
    private ToggleGroup selectGender;   // 单选钮组

    @FXML
    private RadioButton secret;         // 单选钮：保密

    @FXML
    private RadioButton female;         // 单选钮：女

    @FXML
    private RadioButton male;           // 单选钮：男

    @FXML
    private TextField codeField;        // 验证码文本区

    @FXML
    private ImageView verCodeImage;     // 验证码图片

    @FXML
    private Label promptMessage;        // 提示信息

    // 验证码
    private String verCode;

    @FXML
    void verCodeMouseClicked() {
        // 更新验证码和图片
        updateVerificationCode();
    }

    @FXML
    void registerAction() {
        if (check()) {
            // 获取终端
            Terminal terminal = Singleton.get();
            // 获取发送端和接收端
            SendingEnd sendingEnd = terminal.getSendingEnd();
            ReceivingEnd receivingEnd = terminal.getReceivingEnd();
            // 获取账号
            String id = createIdentification(sendingEnd, receivingEnd);
            // 创建对象
            UserLogin userLogin = new UserLogin(id, userPassword.getText(), InstructionSet.OFF_LINE);
            UserInformation userInformation = new UserInformation(id, userName.getText(), calculateAge(),
                    getGender(), localDateToDate());
            // 发送数据
            sendData(sendingEnd, userLogin, userInformation);
            // 显示对话框
            showAlert(id);
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
        }
    }

    @FXML
    void initialize() {
        // 初始化单选钮
        male.setUserData("男");
        female.setUserData("女");
        secret.setUserData("保密");
        // 初始化日期选择器
        Calendar calendar = Calendar.getInstance();
        birthday.setValue(LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)));
        // 设置实现选择器不能手动输入
        birthday.setEditable(false);
        // 显示周数
        birthday.setShowWeekNumbers(true);
        // 初始化验证码和图片
        updateVerificationCode();
    }

    /**
     * updateVerificationCode方法：更新验证码和图片
     */

    private void updateVerificationCode() {
        // 创建随机图片和验证码对象
        VerificationCode verificationCode = new VerificationCode();
        // 获取随机图片
        verCodeImage.setImage(SwingFXUtils.toFXImage(verificationCode.getImage(), null));
        // 获取随机验证码
        verCode = verificationCode.getCode();
    }

    /**
     * check方法：对用户信息进行合法检查
     *
     * @return boolean  ture为合法，false为非法
     */

    private boolean check() {
        // 判断空值
        if (userName.getText().equals("") || birthday.getValue() == null || userPassword.getText().equals("") ||
                confirmPwd.getText().equals("") || codeField.getText().equals("")) {
            promptMessage.setText("需要填写的信息不能为空");
            return false;
        }
        // 用户昵称判断
        if (userName.getText().length() > 20) {
            promptMessage.setText("用户昵称过长，最多只能20个字符");
            return false;
        }
        // 用户密码判断
        if (userPassword.getText().length() <= 20) {
            if (!userPassword.getText().equals(confirmPwd.getText())) {
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
     * sendData方法：向服务器发送数据包
     *
     * @param sendingEnd      sendingEnd      发送端
     * @param userLogin       UserLogin       登录信息
     * @param userInformation UserInformation 用户信息
     */

    private void sendData(SendingEnd sendingEnd, UserLogin userLogin, UserInformation userInformation) {
        // 创建数据包
        UserDataPacket userData = new UserDataPacket(userLogin, userInformation, InstructionSet.REGISTER);
        // 发送数据包
        sendingEnd.writeUserData(userData);
    }

    /**
     * calculateAge方法：计算年龄
     *
     * @return int 年龄
     */

    private int calculateAge() {
        // 当前时间对象
        Calendar now = Calendar.getInstance();
        // 生日时间对象
        Calendar bir = Calendar.getInstance();
        bir.setTime(localDateToDate());
        // 年龄计算
        int age = now.get(Calendar.YEAR) - bir.get(Calendar.YEAR);
        if (now.get(Calendar.MONTH) < bir.get(Calendar.MONTH) ||
                (now.get(Calendar.MONTH) == bir.get(Calendar.MONTH) &&
                        now.get(Calendar.DATE) < bir.get(Calendar.DATE))) {
            age--;
        }
        return age;
    }

    /**
     * createIdentification方法：随机生成账号
     *
     * @param sendingEnd   SendingEnd   发送端
     * @param receivingEnd ReceivingEnd 接收端
     * @return String 账号
     */
    private String createIdentification(SendingEnd sendingEnd, ReceivingEnd receivingEnd) {
        StringBuilder id;
        do {
            // 生成账号
            id = new StringBuilder();
            id.append((int) (Math.random() * 10 - 1) + 1);
            for (int i = 1; i < 10; i++) {
                id.append((int) (Math.random() * 10));
            }
            // 向服务器申请账号查询
            sendingEnd.writeUserData(new UserDataPacket(new UserLogin(id.toString()), InstructionSet.QUERY_USER));
            // 读取服务器的查询结果，并对其结果进行判断
            Object obj = receivingEnd.readObject();
            if (obj instanceof Boolean) {
                if ((Boolean) obj) {
                    break;
                }
            } else {
                break;
            }
        } while (true);
        return id.toString();
    }

    /**
     * localDateToDate方法：将java.time.LocalDate转换成java.sql.Date
     *
     * @return java.sql.Date 日期
     */

    private Date localDateToDate() {
        // 将java.time.LocalDate转换成java.sql.Date
        return Date.valueOf(birthday.getValue());
    }

    /**
     * getGender方法：获取单选钮的的信息
     *
     * @return String 性别
     */

    private String getGender() {
        return selectGender.getSelectedToggle().getUserData().toString();
    }

    /**
     * showAlert方法：显示文本提示对话框
     *
     * @param msg String  需要显示的内容
     */

    private void showAlert(String msg) {
        // 显示文本提示框
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // 设置标题
        alert.setTitle("注册账号");
        // 去掉标题文本
        alert.setHeaderText(null);
        // 去掉提示图标
        alert.setGraphic(null);
        // 设置文本
        alert.setContentText("注册成功，您的账号为：" + msg);
        // 显示对话框并等待对话框关闭
        alert.showAndWait();
    }

}

/*
 * 注：小知识
    // 将java.time.LocalDate转换成java.util.Date
    Date.from(birthday.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
 */

/*
 * 注：暂时废弃
    // 生日判断
    if (Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}", birthday.getValue().toString())) {
        // 年份判断
        if(birthday.getValue().getYear() < 1900 || birthday.getValue().getYear() > 2020) {
            promptMessage.setText("年份格式错误：[1900,2020]");
            return false;
        }
        // 月份判断
        if(birthday.getValue().getMonthValue() <= 0 || birthday.getValue().getMonthValue() > 12) {
            promptMessage.setText("月份格式错误：[1,12]");
            return false;
        }
        // 日期判断
        if(birthday.getValue().getDayOfMonth() <= 0 || birthday.getValue().getDayOfMonth() > 31) {
            promptMessage.setText("日期格式错误：[1,31]");
            return false;
        }
    } else {
        promptMessage.setText("生日格式错误：yyyy-MM-dd");
        return false;
    }
 */

/* 注：暂时废弃
    private void sendData(UserLogin userLogin, UserInformation userInformation) {
        Socket socket = null;
        ObjectIO objectIO = null;
        try {
            // 与服务器建立连接
            socket = new Socket("localhost", 8888);
            // 创建DataPacket对象
            DataPacket dataPacket = new DataPacket(userLogin, userInformation, "register");
            // 创建对象流输入输出对象
            objectIO = new ObjectIO(socket);
            // 发送对象
            objectIO.write(dataPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源和关闭连接
            if (objectIO != null) {
                objectIO.sendClose();
            }
            CommandProcess.close(socket);
        }
    }
 */

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
        // 获取账号
        String id = createIdentification(ois, oos);
        // 创建对象
        UserLogin userLogin = new UserLogin(id, userPassword.getText());
        UserInformation userInformation = new UserInformation(id, userName.getText(), calculateAge(),
                getGender(), localDateToDate());
        // 发送数据
        sendData(oos, userLogin, userInformation);
        // 向服务器申请退出请求
        oos.writeObject(new UserDataPacket(InstructionSet.EXIT));
        // 显示对话框
        showAlert(id);
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
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        CommandProcess.close(ois, oos, socket);
    }
 */

/*
 * 注：暂时废弃
    private String createIdentification(ObjectInputStream ois, ObjectOutputStream oos) {
        StringBuilder id = null;
        try {
            do {
                // 生成账号
                id = new StringBuilder();
                id.append((int) (Math.random() * 10) + 1);
                for (int i = 1; i < 10; i++) {
                    id.append((int) (Math.random() * 10));
                }
                // 向服务器申请账号查询
                oos.writeObject(new UserDataPacket(new UserLogin(id.toString()), InstructionSet.QUERY_USER));
                // 读取服务器的查询结果，并对其结果进行判断
                Object obj = ois.readObject();
                if (obj instanceof Boolean) {
                    if ((Boolean) obj) {
                        break;
                    }
                } else {
                    break;
                }
            } while (true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return id.toString();
    }

    private void sendData(ObjectOutputStream oos, UserLogin userLogin, UserInformation userInformation) {
        try {
            // 创建数据包
            UserDataPacket userData = new UserDataPacket(userLogin, userInformation, InstructionSet.REGISTER);
            // 发送数据包
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 */

