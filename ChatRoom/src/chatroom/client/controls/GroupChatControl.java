package chatroom.client.controls;

import chatroom.client.list.MessageList;
import chatroom.client.list.OnlineList;
import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.client.utils.CustomMessageListCell;
import chatroom.client.utils.GroupSwap;
import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.packet.FileDataPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 群聊界面控制类
 *
 * @description: 实现向群聊发送和接受消息
 * @author: Vcatory
 * @date: 2021-01-07 9:00
 */

public class GroupChatControl {

    @FXML
    private Label addressee;

    @FXML
    private ListView<FileDataPacket> messageListView;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private ListView<OthersInformation> numberListView;

    // 在线人数
    private int numberCount;
    // 消息数目
    private int messageCount;
    // 发送端
    private SendingEnd sendingEnd;
    // 发送者
    private UserInformation sender;
    // 接收者
    private GroupInformation receiver;
    // 消息列表观察者
    private ObservableList<FileDataPacket> fileObservable;
    // 成员列表观察者
    private ObservableList<OthersInformation> numberObservable;


    @FXML
    void fileMC() {
        // 创建文件选择器对象
        FileChooser fileChooser = new FileChooser();
        // 设置标题
        fileChooser.setTitle("选择文件");
        // 设置默认打开路径
        fileChooser.setInitialDirectory(new File("C:" + File.separator + "Users" + File.separator +
                "Vcatory" + File.separator + "Documents"));
        // 添加文件过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*")
        );
        // 生成文件
        createFile(fileChooser.showOpenDialog(addressee.getScene().getWindow()));
    }

    @FXML
    void filmMC() {
        // 创建文件选择器对象
        FileChooser fileChooser = new FileChooser();
        // 设置标题
        fileChooser.setTitle("选择视频");
        // 设置默认打开路径
        fileChooser.setInitialDirectory(new File("C:" + File.separator + "Users" + File.separator +
                "Vcatory" + File.separator + "Videos"));
        // 添加文件过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有视频文件", "*.*"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("MVK", "*.mvk")
        );
        // 生成文件
        createFile(fileChooser.showOpenDialog(addressee.getScene().getWindow()));
    }

    @FXML
    void pictureMC() {
        // 创建文件选择器对象
        FileChooser fileChooser = new FileChooser();
        // 设置标题
        fileChooser.setTitle("选择图片");
        // 设置默认打开路径
        fileChooser.setInitialDirectory(new File("C:" + File.separator + "Users" + File.separator +
                "Vcatory" + File.separator + "Pictures"));
        // 添加文件过滤器
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有图片", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        // 生成文件
        createFile(fileChooser.showOpenDialog(addressee.getScene().getWindow()));
    }

    @FXML
    void sendAction() {
        // 创建文件数据包
        FileDataPacket fileData = new FileDataPacket();
        // 设置文件数据
        fileData.setFile(messageTextArea.getText().getBytes(StandardCharsets.UTF_8));
        // 设置发送者
        fileData.setSender(sender.getId());
        // 设置接收者
        fileData.setReceiver(receiver.getGroupId());
        // 设置当前时间
        fileData.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 设置文件类型
        fileData.setType(InstructionSet.CHARACTER);
        // 设置文件状态
        fileData.setState(InstructionSet.SEND);
        // 将消息添加到总消息列表
        MessageList.getFileList().add(fileData);
        // 将消息发送给服务器
        sendingEnd.writeFileData(fileData);
        // 清空文本区域
        messageTextArea.setText("");
    }

    @FXML
    void initialize() {
        // 初始化控制组件
        initControls();
        // 初始化界面
        initStage();
        // 初始化ListView
        initListView();
        // 启动消息线程
        startThread();
    }

    /**
     * updateFileData方法：修改文件信息
     *
     * @param fileData FileDataPacket 文件数据包
     */

    private void updateFileData(FileDataPacket fileData) {
        // 声明字节流
        OutputStream fos = null;
        try {
            // 创建字节流
            fos = new FileOutputStream(MessageList.getFile().getParent() + File.separator
                    + fileData.getFilename());
            // 输出数据
            fos.write(fileData.getFile());
            // 刷新流
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 空值判断
            if (fos != null) {
                try {
                    // 释放资源
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 修改文件数据(文件路径)
        fileData.setFile((MessageList.getFile().getParent() + File.separator
                + fileData.getFilename()).getBytes(StandardCharsets.UTF_8));
        // 将消息添加到总消息列表
        MessageList.getFileList().add(fileData);
    }

    /**
     * createFile方法：生成和发送文件
     *
     * @param file File 文件路径
     */

    private void createFile(File file) {
        // 空值判断
        if (file != null) {
            // 数据长度
            int len = 0;
            // 声明字节数组
            byte[] bytes = null;
            // 声明字节输入流
            InputStream fis = null;
            try {
                // 创建字节输出流
                fis = new FileInputStream(file);
                // 创建字节数组
                bytes = new byte[(int) file.length()];
                // 读取数据
                len = fis.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 空值判断
                if (fis != null) {
                    try {
                        // 释放资源
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 空值判断
            if (bytes != null && bytes.length == len) {
                // 数据封装（创建数据包）
                FileDataPacket fileData = new FileDataPacket();
                // 设置文件数据
                fileData.setFile(bytes);
                // 设置发送者
                fileData.setSender(sender.getId());
                // 设置接收者
                fileData.setReceiver(receiver.getGroupId());
                // 设置当前时间
                fileData.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                // 设置文件类型
                fileData.setType(InstructionSet.NO_CHARACTER);
                // 设置文件状态
                fileData.setState(InstructionSet.SEND);
                // 设置文件名
                fileData.setFileName(file.getName());
                // 将消息发送给服务器
                sendingEnd.writeFileData(fileData);
                // 将修改数据信息,并加入到总消息列表
                updateFileData(fileData);
            }
        }
    }

    /**
     * readMessageFiles方法：读取消息列表
     *
     * @return ArrayList<FileDataPacket> 消息列表
     */

    private ArrayList<FileDataPacket> readMessageFiles() {
        // 创建消息列表
        ArrayList<FileDataPacket> files = new ArrayList<>();
        // 获取总消息列表的数目
        messageCount = MessageList.getFileList().size();
        // 遍历总消息列表
        for (FileDataPacket file : MessageList.getFileList()) {
            // 判断消息
            if (check(file)) {
                // 将新消息添加到ObservableList中
                files.add(file);
            }
        }
        // 返回结果
        return files;
    }

    /**
     * readNumbers方法：获取成员列表
     *
     * @return ArrayList<OthersInformation> 成员列表
     */

    private ArrayList<OthersInformation> readNumbers() {
        // 获取在线成员的数目
        numberCount = OnlineList.getOnline().size();
        // 遍历成员列表
        for (OthersInformation temp : receiver.getNumbers()) {
            // 判断用户在线，并不在在线列表
            if (temp.getLine() == InstructionSet.ON_LINE &&
                    !OnlineList.getOnline().contains(temp.getId())) {
                // 将用户添加到在线列表
                OnlineList.getOnline().add(temp.getId());
            }
        }
        // 返回成员列表
        return receiver.getNumbers();
    }

    /**
     * check方法：判断消息是否属于该窗口
     *
     * @param file FileDataPacket 文件数据包
     * @return boolean true为属于
     */

    private boolean check(FileDataPacket file) {
        // 判断接收或发送消息
        return file.getSender().equals(receiver.getGroupId()) || file.getReceiver().equals(receiver.getGroupId());
    }

    /**
     * createBasicInformation方法：获取基本信息
     */

    private void createBasicInformation() {
        // 获取终端
        Terminal terminal = Singleton.get();
        // 获取发送端
        this.sendingEnd = terminal.getSendingEnd();
        // 获取发送者信息
        this.sender = terminal.getUserInformation();
        // 获取接收端信息
        this.receiver = GroupSwap.getGroup();
    }

    /**
     * initStage方法：初始化界面
     */

    private void initStage() {
        // 获取基本信息
        createBasicInformation();
        // 设置Label文本
        addressee.setText(receiver.getGroupName() + "（" + receiver.getGroupId() + "）");
    }

    /**
     * initControls方法：初始化控制组件
     */

    private void initControls() {
        assert addressee != null : "fx:id=\"addressee\" was not injected: check your FXML file 'FX_GroupChat.fxml'.";
        assert messageListView != null : "fx:id=\"messageListView\" was not injected: check your FXML file 'FX_GroupChat.fxml'.";
        assert messageTextArea != null : "fx:id=\"messageTextArea\" was not injected: check your FXML file 'FX_GroupChat.fxml'.";
        assert numberListView != null : "fx:id=\"numberListView\" was not injected: check your FXML file 'FX_GroupChat.fxml'.";
    }

    /**
     * initListView方法：初始化ListView
     */

    private void initListView() {
        // 创建ObservableList对象（消息列表）
        fileObservable = FXCollections.observableArrayList();
        // 将所有消息列表添加到ObservableList中
        fileObservable.addAll(readMessageFiles());
        // 将ObservableList对象重设到ListView中
        messageListView.setItems(fileObservable);
        // 设置消息列表的CellFactory
        messageListView.setCellFactory(param -> new CustomMessageListCell());

        // 创建ObservableList对象（消息列表）
        numberObservable = FXCollections.observableArrayList();
        // 将所有消息列表添加到ObservableList中
        numberObservable.addAll(readNumbers());
        // 将ObservableList对象重设到ListView中
        numberListView.setItems(numberObservable);
        // 设置消息列表的CellFactory
        numberListView.setCellFactory(param -> new CustomNumberListCell(receiver));
    }

    private static class CustomNumberListCell extends ListCell<OthersInformation> {

        /**
         * 基本属性
         */

        private final GroupInformation group; // 群聊信息
        private final Label numberName;       // 成员名称
        private final Label line;             // 登录状态
        private final HBox content;           // 顶级容器

        public CustomNumberListCell(GroupInformation group) {
            // 设置群聊信息
            this.group = group;
            // 创建成员名称和登录状态标签
            numberName = new Label();
            line = new Label();
            // 将成员名称和登录状态标签加入到顶层容器
            content = new HBox(numberName, line);
            // 对整体布局进行格式化
            formatting();
        }

        /**
         * formatting方法：格式化布局
         */

        private void formatting() {
            // 设置字体
            numberName.setFont(new Font("宋体", 12));
            line.setFont(new Font("宋体", 12));
            // 设置宽度
            numberName.setPrefWidth(70);
            // 设置对齐方式
            line.setAlignment(Pos.CENTER_RIGHT);
            content.setAlignment(Pos.CENTER_LEFT);
        }


        /**
         * checkNumberName方法：检查并设置群主显示
         *
         * @param id String 成员账号
         */

        private void checkNumberName(String id) {
            if (group.getMasterInformation().getId().equals(id)) {
                numberName.setTextFill(Color.RED);
            }
        }

        /**
         * checkLine方法：检查并设置登录状态
         *
         * @param line int 登录状态指令
         */

        private void checkLine(int line) {
            // 登陆状态：离线
            if (line == InstructionSet.OFF_LINE) {
                // 设置文本
                this.line.setText("离线");
                // 设置颜色
                this.line.setTextFill(Color.GRAY);
            } else {
                // 登陆状态：在线
                if (line == InstructionSet.ON_LINE) {
                    // 设置文本
                    this.line.setText("在线");
                    // 设置颜色
                    this.line.setTextFill(Color.GREEN);
                } else {
                    // 设置文本
                    this.line.setText("未知");
                    // 设置颜色
                    this.line.setTextFill(Color.RED);
                }
            }
        }

        @Override
        protected void updateItem(OthersInformation item, boolean empty) {
            // 调用父类的updateItem方法
            super.updateItem(item, empty);
            // 判断数据是否为空
            if (item != null && !empty) {
                // 设置昵称标签
                numberName.setText(item.getName());
                // 检查并设置登录状态标签
                checkLine(item.getLine());
                // 检查成员账号
                checkNumberName(item.getId());
                // 将整个布局设置为Item
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

    }

    /**
     * startThread方法：启动监听消息列表的线程
     */

    private void startThread() {
        // 启动监听线程
        new Thread(new Task<Void>() {

            @Override
            protected Void call() {
                while (MessageList.getOpenList().contains(receiver.getGroupId())) {
                    // 监听消息列表是否改变
                    if (messageCount != MessageList.getFileList().size()) {
                        // 启动子线程
                        Platform.runLater(() -> {
                            // 遍历新消息
                            for (int i = messageCount; i < MessageList.getFileList().size(); i++) {
                                // 判断消息
                                if (check(MessageList.getFileList().get(i))) {
                                    // 将新消息添加到ObservableList中
                                    fileObservable.add(MessageList.getFileList().get(i));
                                }
                            }
                            // 更新消息个数
                            messageCount = MessageList.getFileList().size();
                            // 更新消息界面
                            messageListView.refresh();
                        });
                    }
                    // 监听在线成员列表是否改变
                    if (numberCount != MessageList.getFileList().size()) {
                        // 启动子线程
                        Platform.runLater(() -> {
                            // 遍历在线成员列表
                            for (OthersInformation othersInformation : numberObservable) {
                                if (OnlineList.getOnline().contains(othersInformation.getId())) {
                                    othersInformation.setLine(InstructionSet.ON_LINE);
                                } else {
                                    othersInformation.setLine(InstructionSet.OFF_LINE);
                                }
                            }
                            // 更新消息个数
                            numberCount = OnlineList.getOnline().size();
                            // 更新消息界面
                            numberListView.refresh();
                        });
                    }
                    // 线程休眠
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).start();
/*
        // 启动成员监听线程
        new Thread(new Task<Void>() {

            @Override
            protected Void call() {
                while (MessageList.getOpenList().contains(receiver.getGroupId())) {
                    // 监听在线成员列表是否改变
                    if (numberCount != MessageList.getFileList().size()) {
                        // 启动子线程
                        Platform.runLater(() -> {
                            // 遍历在线成员列表
                            for (OthersInformation othersInformation : numberObservable) {
                                if (OnlineList.getOnline().contains(othersInformation.getId())) {
                                    othersInformation.setLine(InstructionSet.ON_LINE);
                                } else {
                                    othersInformation.setLine(InstructionSet.OFF_LINE);
                                }
                            }
                            // 更新消息个数
                            numberCount = OnlineList.getOnline().size();
                            // 更新消息界面
                            numberListView.refresh();
                        });
                    }
                    // 线程礼让
                    // Thread.yield();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).start();
        */
    }

}
