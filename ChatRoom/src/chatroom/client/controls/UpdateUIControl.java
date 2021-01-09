package chatroom.client.controls;

import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.communal.InstructionSet;
import chatroom.communal.packet.RefreshDataPacket;
import chatroom.communal.packet.UserDataPacket;
import chatroom.communal.javabeans.UserInformation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * 用户信息修改控制类：
 *
 * @description: 修改用户信息并合法检查
 * @author: Vcatory
 * @date: 2021-01-02 9:56
 */

public class UpdateUIControl {

    @FXML
    private DatePicker birthday;            // 生日

    @FXML
    private TextField phoneNumber;          // 电话号码

    @FXML
    private TextField email;                // 邮箱地址

    @FXML
    private ToggleGroup selectGender;       // 单选钮组

    @FXML
    private Label promptMessage;            // 提示信息

    @FXML
    private RadioButton secret;             // 单选钮：保密

    @FXML
    private TextField userName;             // 用户昵称

    @FXML
    private RadioButton female;             // 单选钮：女

    @FXML
    private RadioButton male;               // 单选钮：男

    @FXML
    private TextArea personalizedSignature; // 个性签名

    // 终端
    private Terminal terminal;


    @FXML
    void saveAction() {
        if (check()) {
            // 获取发送端
            SendingEnd sendingEnd = terminal.getSendingEnd();
            // 创建对象
            UserInformation userInformation = new UserInformation(terminal.getUserId(), userName.getText(),
                    calculateAge(), getGender(), localDateToDate());
            // 电话号码空值判断
            if (!phoneNumber.getText().equals("")) {
                userInformation.setPhoneNumber(phoneNumber.getText());
            }
            // 邮箱地址空值判断
            if (!email.getText().equals("")) {
                userInformation.setEmail(email.getText());
            }
            // 个性签名空值判断
            if (!personalizedSignature.getText().equals("")) {
                userInformation.setPersonalizedSignature(personalizedSignature.getText());
            }
            // 更新终端用户信息
            terminal.setUserInformation(userInformation);
            // 发送数据
            sendData(sendingEnd, userInformation);
            // 关闭当前窗口
            ((Stage) promptMessage.getScene().getWindow()).close();
        }
    }

    @FXML
    void initialize() {
        // 获取终端
        this.terminal = Singleton.get();
        // 初始化单选钮
        male.setUserData("男");
        female.setUserData("女");
        secret.setUserData("保密");
        // 设置实现选择器不能手动输入
        birthday.setEditable(false);
        // 显示周数
        birthday.setShowWeekNumbers(true);
        // 自动换行
        personalizedSignature.setWrapText(true);
        // 初始化界面
        initStage();
    }

    /**
     * initStage方法：初始化界面
     */

    private void initStage() {
        // 获取用户信息
        UserInformation userInformation = terminal.getUserInformation();
        // 设置用户昵称
        userName.setText(userInformation.getName());
        // 设置性别选项
        if (userInformation.getGender().equals("男")) {
            male.setSelected(true);
        } else {
            if (userInformation.getGender().equals("女")) {
                female.setSelected(true);
            } else {
                secret.setSelected(true);
            }
        }
        // 设置生日
        birthday.setValue(dateToLocalDate(userInformation.getBirthday()));
        // 设置电话号码
        if (userInformation.getPhoneNumber() != null) {
            phoneNumber.setText(userInformation.getPhoneNumber());
        }
        // 设置邮箱地址
        if (userInformation.getEmail() != null) {
            email.setText(userInformation.getEmail());
        }
        // 设置个性签名
        if (userInformation.getPersonalizedSignature() != null) {
            personalizedSignature.setText(userInformation.getPersonalizedSignature());
        }
    }

    /**
     * dateToLocalDate方法：将java.sql.date转化成java.time.LocalDate
     *
     * @param date java.sql.date 日期
     * @return LocalDate
     */

    private LocalDate dateToLocalDate(Date date) {
        // 将java.sql.date 转化成 java.util.date
        java.util.Date fDate = new java.util.Date(date.getTime());
        // 将java.util.date 转化成 java.time.LocalDate
        return fDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private boolean check() {
        // 用户昵称判断
        if (userName.getText().equals("")) {
            promptMessage.setText("用户昵称不能为空");
            return false;
        } else {
            if (userName.getText().length() > 20) {
                promptMessage.setText("用户昵称过长，最多只能20个字符");
                return false;
            }
        }
        // 电话号码判断
        if (phoneNumber.getText().length() != 0 && phoneNumber.getText().length() != 11) {
            promptMessage.setText("电话号码不足11位");
            return false;
        } else {
            if (!phoneNumber.getText().equals("") && !Pattern.matches("\\d{11}", phoneNumber.getText())) {
                promptMessage.setText("电话号码错误：包含非法字符");
                return false;
            }
        }
        // 邮箱地址判断
        if (email.getText().length() > 30) {
            promptMessage.setText("邮箱地址过长，最多只能30个字符");
            return false;
        } else {
            if (!email.getText().equals("") && !Pattern.matches(
                    "[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}", email.getText())) {
                promptMessage.setText("邮箱格式错误：包含非法字符");
                return false;
            }
        }
        // 个性签名判断
        if (personalizedSignature.getText().length() > 100) {
            promptMessage.setText("个性签名过长：最多只能100个字符");
            return false;
        }
        return true;
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
     * getGender方法：获取单选钮的的信息
     *
     * @return String 性别
     */

    private String getGender() {
        return selectGender.getSelectedToggle().getUserData().toString();
    }

    /**
     * sendData方法：向服务器发送数据包
     *
     * @param sendingEnd      sendingEnd      发送端
     * @param userInformation UserInformation 用户信息
     */

    private void sendData(SendingEnd sendingEnd, UserInformation userInformation) {
        // 创建数据包
        RefreshDataPacket refreshData = new RefreshDataPacket(userInformation,InstructionSet.UPDATE_USER_INFORMATION);
        // 发送数据包
        sendingEnd.writeRefreshData(refreshData);
    }

}

