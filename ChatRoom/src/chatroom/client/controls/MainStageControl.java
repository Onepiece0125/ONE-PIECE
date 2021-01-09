package chatroom.client.controls;

import chatroom.client.launchs.GroupChatLaunch;
import chatroom.client.launchs.PrivateChatLaunch;
import chatroom.client.launchs.UpdateUILaunch;
import chatroom.client.list.MessageList;
import chatroom.client.list.OnlineList;
import chatroom.client.terminal.ReceivingEnd;
import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.client.utils.GroupSwap;
import chatroom.client.utils.OthersSwap;
import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.packet.FileDataPacket;
import chatroom.communal.packet.RefreshDataPacket;
import chatroom.communal.packet.UserDataPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 主界面控制：
 *
 * @description: 显示主界面，接受服务器发送的数据包，并做出相应的处理
 * @author: Vcatory
 * @date: 2020/12/27 15:24
 */

public class MainStageControl {

    @FXML
    private Label addFGLb;

    @FXML
    private ToggleGroup btnGroup;

    @FXML
    private Label createGroupLb;

    @FXML
    private Label nameAndId;

    @FXML
    private Text personSign;

    @FXML
    private Label safeExitLb;

    @FXML
    private Label updatePILb;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ListView<Message> messageListView;

    @FXML
    private ListView<OthersInformation> friendListView;

    @FXML
    private ListView<GroupInformation> groupListView;

    // 终端
    private Terminal terminal;
    // 发送端
    private SendingEnd sendingEnd;
    // 接收端
    private ReceivingEnd receivingEnd;
    // 用户数据
    private UserDataPacket userData = null;
    // 消息列表
    ObservableList<Message> messageList = null;
    // 群聊列表
    ObservableList<GroupInformation> groupList = null;
    // 好友列表
    ObservableList<OthersInformation> friendList = null;

    // 运行状态
    private boolean isRunning;

    @FXML
    void addFGMC() {
        // 创建TextInputDialog对话框
        TextInputDialog dialog = new TextInputDialog();
        // 格式化对话框
        initAddDialog(dialog);
        // 显示对话框并等待对话框关闭
        dialog.showAndWait();
        // 获取账号
        String id = dialog.getEditor().getText();
        // 判断输入框是否为空
        if (!id.equals("")) {
            // 发送数据包
            sendAddData(id);
        }
    }

    @FXML
    void addFGMEnt() {
        addFGLb.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void addFGMExi() {
        addFGLb.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void createGroupMC() {
        // 创建TextInputDialog对话框
        TextInputDialog dialog = new TextInputDialog();
        // 格式化对话框
        initGroupDialog(dialog);
        // 显示对话框并等待对话框关闭
        dialog.showAndWait();
        String groupName = dialog.getEditor().getText();
        // 判断输入框是否为空
        if (!groupName.equals("")) {
            // 生成账号
            String id = createIdentification();
            // 发送数据
            sendGroupData(id, groupName);
            // 显示提示对话框
            showAlert("创建成功，群聊账号为：" + id);
        }
    }

    @FXML
    void createGroupMEnt() {
        createGroupLb.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void createGroupMExi() {
        createGroupLb.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void safeExitMC() {
        // 关闭线程
        terminal.setRunning(false);
        // 移除终端并释放资源
        Singleton.remove();
        // 关闭当前窗口
        ((Stage) personSign.getScene().getWindow()).close();
    }

    @FXML
    void safeExitMEnt() {
        safeExitLb.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void safeExitMExi() {
        safeExitLb.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void updatePIMC() {
        Platform.runLater(() -> {
            try {
                new UpdateUILaunch().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void updatePIMEnt() {
        updatePILb.setTextFill(new Color(0.125, 0.125, 0.125, 1));
    }

    @FXML
    void updatePIMExi() {
        updatePILb.setTextFill(new Color(0.749, 0.749, 0.749, 1));
    }

    @FXML
    void messageBtnAction() {
        // 将所有的ListView设置成不可见
        allListViewInvisible();
        // 显示消息的ListView
        messageListView.setVisible(true);
    }

    @FXML
    void friendBtnAction() {
        // 将所有的ListView设置成不可见
        allListViewInvisible();
        // 显示好友的ListView
        friendListView.setVisible(true);
    }

    @FXML
    void groupBtnAction() {
        // 将所有的ListView设置成不可见
        allListViewInvisible();
        // 显示群聊的ListView
        groupListView.setVisible(true);
    }

    @FXML
    void groupMC() {
        // 获取群聊信息
        GroupInformation group = groupListView.getSelectionModel().getSelectedItem();
        // 判断窗口是否已经打开
        if (group != null && !MessageList.getOpenList().contains(group.getGroupId())) {
            // 将数据写入交换类
            GroupSwap.setGroup(group);
            // 将该窗口加入到打开窗口列表
            MessageList.getOpenList().add(group.getGroupId());
            // 启动子线程
            Platform.runLater(() -> {
                try {
                    // 创建窗口
                    Stage stage = new Stage();
                    // 添加关闭事件
                    stage.setOnCloseRequest(event -> {
                        // 将该窗口移除打开装口列表
                        MessageList.getOpenList().remove(group.getGroupId());
                        // 关闭窗口
                        stage.close();
                    });
                    // 加载窗口
                    new GroupChatLaunch().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    void friendMC() {
        // 获取用户信息
        OthersInformation others = friendListView.getSelectionModel().getSelectedItem();
        // 判断窗口是否已经打开
        if (others != null && !MessageList.getOpenList().contains(others.getId())) {
            // 将数据写入交换类
            OthersSwap.setOthers(others);
            // 将该窗口加入到打开窗口列表
            MessageList.getOpenList().add(others.getId());
            // 启动子线程
            Platform.runLater(() -> {
                try {
                    // 创建窗口
                    Stage stage = new Stage();
                    // 添加关闭事件
                    stage.setOnCloseRequest(event -> {
                        // 将该窗口移除打开装口列表
                        MessageList.getOpenList().remove(others.getId());
                        // 关闭窗口
                        stage.close();
                    });
                    // 加载窗口
                    new PrivateChatLaunch().start(stage);
                    // 遍历新消息列表
                    for (Message temp : messageList) {
                        // 判断消息
                        if (others.getId().equals(temp.getId())) {
                            // 新消息设置成旧消息
                            temp.setState(InstructionSet.OLD_MESSAGE);
                        }
                    }
                    // 刷新消息列表
                    messageListView.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    void messageMC() {
        // 获取群聊信息
        Message message = messageListView.getSelectionModel().getSelectedItem();
        // 判断窗口是否已经打开
        if (message != null && !MessageList.getOpenList().contains(message.getId())) {
            // 遍历好友列表
            for (OthersInformation others : friendList) {
                if (message.getId().equals(others.getId())) {
                    // 将数据写入交换类
                    OthersSwap.setOthers(others);
                }
            }
            // 将该窗口加入到打开窗口列表
            MessageList.getOpenList().add(message.getId());
            // 启动子线程
            Platform.runLater(() -> {
                try {
                    // 创建窗口
                    Stage stage = new Stage();
                    // 添加关闭事件
                    stage.setOnCloseRequest(event -> {
                        // 将该窗口移除打开装口列表
                        MessageList.getOpenList().remove(message.getId());
                        // 关闭窗口
                        stage.close();
                    });
                    // 加载窗口
                    new PrivateChatLaunch().start(stage);
                    // 遍历新消息列表
                    for (Message temp : messageList) {
                        // 判断消息
                        if (message.getId().equals(temp.getId())) {
                            // 新消息设置成旧消息
                            temp.setState(InstructionSet.OLD_MESSAGE);
                        }
                    }
                    // 刷新消息列表
                    messageListView.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    @FXML
    void initialize() {
        // 初始化控制组件
        initControls();
        // 接收数据，并创建连接
        createDataPacket();
        // 初始化界面
        initMainStage();
        // 初始化ListView
        initListView();
        // 初始化记录文件
        initFile();
        // 启动线程
        startThread();
    }

    /**
     * getDataPacket方法：获取连接并获取数据信息
     */

    private void createDataPacket() {
        // 获取终端
        terminal = Singleton.get();
        // 获取发送和接受端
        sendingEnd = terminal.getSendingEnd();
        receivingEnd = terminal.getReceivingEnd();
        // 发送指令
        sendingEnd.writeUserData(new UserDataPacket(InstructionSet.GET_INFORMATION));
        // 接受数据包
        Object obj = receivingEnd.readObject();
        // 类型转换
        if (obj instanceof UserDataPacket) {
            userData = (UserDataPacket) obj;
        }
        // 设置用户信息
        terminal.setUserInformation(userData.getUserInformation());
    }

    /**
     * initControls方法：初始化控制组件
     */

    private void initControls() {
        assert addFGLb != null : "fx:id=\"addFGLb\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert btnGroup != null : "fx:id=\"btnGroup\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert createGroupLb != null : "fx:id=\"createGroupLb\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert nameAndId != null : "fx:id=\"nameAndId\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert personSign != null : "fx:id=\"personSign\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert safeExitLb != null : "fx:id=\"safeExitLb\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
        assert updatePILb != null : "fx:id=\"updatePILb\" was not injected: check your FXML file 'FX_MainStage.fxml'.";
    }

    /**
     * initMainStage方法：初始化主界面
     */

    private void initMainStage() {
        // 初始化界面
        refreshStage();
        // 设置背景图片
        backgroundImage.setImage(new Image(new File(
                "C:\\Users\\Vcatory\\IdeaProjects\\ChatRoom\\src\\chatroom\\client\\images\\Background02.png"
        ).toURI().toString()));
    }

    /**
     * refreshStage方法：更新界面
     */

    private void refreshStage() {
        // 更新用户名和账号
        nameAndId.setText(userData.getUserInformation().getName() +
                "（" + userData.getUserInformation().getId() + "）");
        // 更新个性签名
        personSign.setText("个性签名：" + '\n' + userData.getUserInformation().getPersonalizedSignature());
    }

    /**
     * initGroupDialog方法：格式化TextInputDialog对话框（创建群聊）
     *
     * @param dialog TextInputDialog 对话框
     */

    private void initGroupDialog(TextInputDialog dialog) {
        // 设置标题
        dialog.setTitle("创建群聊");
        // 去掉标题文本
        dialog.setHeaderText(null);
        // 去掉提示图标
        dialog.setGraphic(null);
        // 设置提示文本
        dialog.setContentText("请输入群聊名称：");
    }

    /**
     * initAddDialog方法：格式化TextInputDialog对话框（添加好友或群聊）
     *
     * @param dialog TextInputDialog 对话框
     */

    private void initAddDialog(TextInputDialog dialog) {
        // 设置标题
        dialog.setTitle("添加好友或群聊");
        // 去掉标题文本
        dialog.setHeaderText(null);
        // 去掉提示图标
        dialog.setGraphic(null);
        // 设置提示文本
        dialog.setContentText("请输入好友或群聊账号：");
    }

    /**
     * sendGroupData方法：创建群聊数据包，并将它发送给服务器
     *
     * @param groupId   String 群聊账号
     * @param groupName String 群聊名称
     */

    public void sendGroupData(String groupId, String groupName) {
        // 创建群聊信息对象
        GroupInformation group = new GroupInformation();
        // 将群聊账号添加到群聊信息
        group.setGroupId(groupId);
        // 将群聊名称添加到群聊信息
        group.setGroupName(groupName);
        // 将群主信息添加到群聊信息
        group.setMasterInformation(new OthersInformation(userData.getUserInformation().getId(),
                userData.getUserInformation().getName(), InstructionSet.ON_LINE));
        // 创建数据包
        RefreshDataPacket refreshData = new RefreshDataPacket(group, InstructionSet.CREATE_GROUP);
        // 发送数据包
        sendingEnd.writeRefreshData(refreshData);
    }

    /**
     * sendAddData方法：创建添加账号数据包，并将它发送给服务器
     *
     * @param id String 账号
     */

    public void sendAddData(String id) {
        // 判断账号（好友）
        if (id.length() == 10) {
            // 创建数据包
            RefreshDataPacket refreshData = new RefreshDataPacket(new OthersInformation(userData.getUserInformation().getId(),
                    userData.getUserInformation().getName(), InstructionSet.ON_LINE),
                    new OthersInformation(id), InstructionSet.ADD_FRIEND);
            // 发送数据包
            sendingEnd.writeRefreshData(refreshData);
        }
        // 判断账号（群聊）
        if (id.length() == 11) {
            // 创建数据包
            RefreshDataPacket refreshData = new RefreshDataPacket(new GroupInformation(id), InstructionSet.ADD_GROUP);
            // 发送数据包
            sendingEnd.writeRefreshData(refreshData);
        }
    }

    /**
     * createIdentification方法：随机生成账号
     *
     * @return String 账号
     */

    private String createIdentification() {
        StringBuilder id;
        do {
            // 生成账号
            id = new StringBuilder();
            id.append((int) (Math.random() * 10 - 1) + 1);
            for (int i = 0; i < 10; i++) {
                id.append((int) (Math.random() * 10));
            }
            // 向服务器申请账号查询
            sendingEnd.writeUserData(new UserDataPacket(new GroupInformation(id.toString()), InstructionSet.QUERY_GROUP));
        } while (isRunning);
        return id.toString();
    }

    /**
     * showAlert方法：显示文本提示对话框
     *
     * @param msg String 需要显示的内容
     */

    private void showAlert(String msg) {
        // 显示文本提示框
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // 设置标题
        alert.setTitle("创建群聊");
        // 去掉标题文本
        alert.setHeaderText(null);
        // 去掉提示图标
        alert.setGraphic(null);
        // 设置文本
        alert.setContentText(msg);
        // 显示对话框并等待对话框关闭
        alert.showAndWait();
    }

    /**
     * allListViewInvisible方法：将所有的ListView设置成不可见
     */

    private void allListViewInvisible() {
        // 消息
        messageListView.setVisible(false);
        // 好友
        friendListView.setVisible(false);
        // 群聊
        groupListView.setVisible(false);
    }

    /**
     * initListView方法：初始化ListView
     */

    private void initListView() {
        // 创建ObservableList对象（好友列表）
        friendList = FXCollections.observableArrayList();
        // 将所有的好友信息添加到ObservableList对象
        friendList.addAll(userData.getUserFriends().getFriends());
        // 将ObservableList对象重设到ListView中
        friendListView.setItems(friendList);
        // 设置好友列表的CellFactory
        friendListView.setCellFactory(param -> new CustomFriendListCell());

        // 创建ObservableList对象（群聊列表）
        groupList = FXCollections.observableArrayList();
        // 将所有的群聊信息添加到ObservableList对象
        groupList.addAll(userData.getGroups());
        // 将ObservableList对象重设到ListView中
        groupListView.setItems(groupList);
        // 设置群聊列表的CellFactory
        groupListView.setCellFactory(param -> new CustomGroupListCell());

        // 创建ObservableList对象（消息列表）
        messageList = FXCollections.observableArrayList();
        // 将所有的消息信息添加到ObservableList对象
        messageList.addAll(new ArrayList<>());
        // 将ObservableList对象重设到ListView中
        messageListView.setItems(messageList);
        // 设置群聊列表的CellFactory
        messageListView.setCellFactory(param -> new CustomMessageListCell());
    }

    /**
     * checkRefreshDataPacket方法：检查更新数据包
     *
     * @param refreshData RefreshDataPacket 更新数据包
     */

    private void checkRefreshDataPacket(RefreshDataPacket refreshData) {
        // 判断是否为刷新指令
        if (refreshData.getInstruct() == InstructionSet.REFRESH) {
            // 用户信息刷新
            if (refreshData.getUserInformation() != null) {
                Platform.runLater(() -> {
                    // 更新用户信息
                    userData.setUserInformation(refreshData.getUserInformation());
                    // 更新界面
                    refreshStage();
                });

            }
            // 好友列表刷新
            if (refreshData.getFriend() != null) {
                Platform.runLater(() -> {
                    // 更新ObservableList（好友列表）
                    friendList.add(refreshData.getFriend());
                });
            }
            // 群聊列表刷新
            if (refreshData.getGroupInformation() != null) {
                Platform.runLater(() -> {
                    // 更新ObservableList（群聊列表）
                    groupList.add(refreshData.getGroupInformation());
                });
            }
        }
        // 判断是否为通知指令
        if (refreshData.getInstruct() == InstructionSet.INFORM) {
            // 判断用户数据是否为空
            if (refreshData.getUser() != null) {
                // 启动子线程
                Platform.runLater(() -> {
                    // 创建用户对象
                    OthersInformation user = refreshData.getUser();
                    // 遍历好友列表
                    for (int i = 0; i < friendList.size(); i++) {
                        // 查询好友
                        if (friendList.get(i).getId().equals(user.getId())) {
                            // 更新好友
                            friendList.set(i, user);
                        }
                    }
                    // 更新好友列表
                    friendListView.refresh();
                    // 遍历群聊列表
                    for (GroupInformation group : groupList) {
                        // 群主判断
                        if (group.getMasterInformation().getId().equals(user.getId())) {
                            // 更新群主
                            group.setMasterInformation(user);
                        }
                        // 遍历成员列表
                        for (int i = 0; i < group.getNumbers().size(); i++) {
                            // 成员判断
                            if (group.getNumbers().get(i).getId().equals(user.getId())) {
                                // 更新成员
                                group.getNumbers().set(i, user);
                            }
                        }
                    }
                    // 判断用户上线
                    if (user.getLine() == InstructionSet.ON_LINE) {
                        // 将该用户添加到在线用户列表
                        OnlineList.getOnline().add(user.getId());
                    }
                    // 判断用户离线
                    if (user.getLine() == InstructionSet.OFF_LINE) {
                        // 将用户移除在线用户列表
                        OnlineList.getOnline().remove(user.getId());
                    }
                    // 更新群聊列表
                    groupListView.refresh();
                });
            }
            // 判断群聊信息是否为空
            if (refreshData.getGroupInformation() != null) {
                // 启动子线程
                Platform.runLater(() -> {
                    // 遍历群聊列表
                    for (GroupInformation group : groupList) {
                        // 群聊账号判断
                        if (group.getGroupId().equals(refreshData.getGroupInformation().getGroupId())) {
                            // 更新成员列表
                            group.setNumbers(refreshData.getGroupInformation().getNumbers());
                        }
                    }
                    // 更新群聊列表
                    groupListView.refresh();
                });
            }
        }

    }

    /**
     * createFiles方法：根据文件数据包，解析文件路径
     *
     * @return File 文件路径
     */

    private File createFiles() {
        // 指定文件路径
        File file = new File("files" + File.separator +
                userData.getUserInformation().getId() + File.separator + "message.object");
        // 获取父目录
        File parent = file.getParentFile();
        // 判断父目录是否存在
        if (!parent.exists()) {
            // 创建父目录
            System.out.println("创建父目录：" + parent.mkdirs());
        }
        // 判断文件是否存在
        if (!file.exists()) {
            try {
                // 创建文件
                System.out.println("创建文件：" + file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 初始化总消息列表
     */

    private void initFile() {
        // 获取文件路径
        File file = createFiles();
        // 设置文件路径
        MessageList.setFile(file);
        // 创建消息列表
        MessageList.setFileList(new ArrayList<>());
        // 创建在线成员列表
        OnlineList.setOnline(new ArrayList<>(Collections.singletonList(userData.getUserInformation().getId())));
        // 创建打开窗口列表
        MessageList.setOpenList(new ArrayList<>());
    }

    /**
     * newMessage方法：根据文件数据包显示消息（仅实现好友之间的消息提醒）
     *
     * @param fileData FileDataPacket 文件数据包
     */

    private void newMessage(FileDataPacket fileData) {
        // 判断是否为好友消息
        if (fileData.getSender().equals("root") || fileData.getSender().length() == 10) {
            // 启动子线程
            Platform.runLater(() -> {
                // 标识是否为新的好友消息
                boolean flag = true;
                // 遍历消息提示
                for (Message temp : messageList) {
                    // 判断消息提示
                    if (temp.getId().equals(fileData.getSender())) {
                        // 判断窗口是否打开
                        if (!MessageList.getOpenList().contains(fileData.getSender())) {
                            // 将消息状态设置成新消息
                            temp.setState(InstructionSet.NEW_MESSAGE);
                        }
                        // 将标识改为没有新的好友消息
                        flag = false;
                    }
                }
                // 判断新的好友消息
                if (flag) {
                    // 判断窗口是否打开
                    if (MessageList.getOpenList().contains(fileData.getSender())) {
                        // 将消息提示设置为旧消息
                        messageList.add(new Message(fileData.getSender(), InstructionSet.OLD_MESSAGE));
                    } else {
                        // 将消息提示设置为新消息
                        messageList.add(new Message(fileData.getSender(), InstructionSet.NEW_MESSAGE));
                    }
                }
            });
        }
        // 刷新消息界面
        messageListView.refresh();
    }

    /**
     * checkFileDataPacket方法：检查文件数据包
     *
     * @param fileData FileDataPacket 文件数据包
     */

    private void checkFileDataPacket(FileDataPacket fileData) {
        newMessage(fileData);
        // 设置文件状态
        fileData.setState(InstructionSet.RECEIVE);
        // 判断文件类型（字符型）
        if (fileData.getType() == InstructionSet.CHARACTER) {
            // 将文件数据包添加到总消息列表中
            MessageList.getFileList().add(fileData);
        }

        // 判断文件类型（字符型）
        if (fileData.getType() == InstructionSet.NO_CHARACTER) {
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
    }

    /**
     * startThread方法：启动线程
     */

    private void startThread() {
        // 启动接受线程
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                // 持续监听
                while (terminal.isRunning()) {
                    // 接受数据
                    Object obj = receivingEnd.readObject();
                    // 判断数据是否为空
                    if (obj == null) {
                        break;
                    }
                    // 判断数据包是否为文件数据包
                    if (obj instanceof FileDataPacket) {
                        // 检查文件类型
                        checkFileDataPacket((FileDataPacket) obj);
                    }
                    // 判断数据是否为更新数据包
                    if (obj instanceof RefreshDataPacket) {
                        // 检查指令，并做出相关处理
                        checkRefreshDataPacket((RefreshDataPacket) obj);
                    }
                    // 判断数据是否为boolean类型
                    if (obj instanceof Boolean) {
                        // 更新运行状态
                        isRunning = (Boolean) obj;
                    }
                }
                return null;
            }
        }).start();
    }

    /**
     * 该类用于自定义好友列表的显示效果
     */

    private static class CustomFriendListCell extends ListCell<OthersInformation> {

        /**
         * 基本属性区域
         */

        private final Label id;          // 账号
        private final Label name;        // 名称
        private final Label line;        // 登陆状态
        private final HBox content;      // 顶层容器


        /**
         * 构造方法区域
         */

        public CustomFriendListCell() {
            // 创建Label对象
            this.id = new Label();
            this.name = new Label();
            this.line = new Label();
            // 创建VBox布局
            VBox vBox = new VBox(this.id, this.line);
            this.content = new HBox(this.name, vBox);
            // 对整体布局进行格式化
            formatting();
        }

        /**
         * formatting方法：格式化布局
         */

        private void formatting() {
            // 设置字体
            id.setFont(new Font("宋体", 14));
            name.setFont(new Font("宋体", 20));
            line.setFont(new Font("宋体", 12));
            // 设置宽度
            id.setPrefWidth(140);
            name.setPrefWidth(200);
            line.setPrefWidth(140);
            // 设置对齐方式
            line.setAlignment(Pos.CENTER_RIGHT);
            content.setAlignment(Pos.CENTER_LEFT);
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
                // 设置账号标签
                id.setText(item.getId());
                // 设置昵称标签
                name.setText(item.getName());
                // 检查并设置登录状态标签
                checkLine(item.getLine());
                // 将整个布局设置为Item
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

    }

    /**
     * 该类用于自定义群聊列表的显示效果
     */

    private static class CustomGroupListCell extends ListCell<GroupInformation> {

        /**
         * 基本属性区域
         */

        private final Label id;          // 账号
        private final Label name;        // 名称
        private final Label amount;      // 成员
        private final HBox content;      // 顶层容器


        /**
         * 构造方法区域
         */

        public CustomGroupListCell() {
            // 创建Label对象
            this.id = new Label();
            this.name = new Label();
            this.amount = new Label();
            // 创建VBox布局
            VBox vBox = new VBox(this.id, this.amount);
            this.content = new HBox(this.name, vBox);
            // 对整体布局进行格式化
            formatting();
        }

        /**
         * formatting方法：格式化布局
         */

        private void formatting() {
            // 设置字体
            id.setFont(new Font("宋体", 14));
            name.setFont(new Font("宋体", 20));
            amount.setFont(new Font("宋体", 12));
            // 设置宽度
            id.setPrefWidth(140);
            name.setPrefWidth(200);
            amount.setPrefWidth(140);
            // 设置对齐方式
            content.setAlignment(Pos.CENTER_LEFT);
        }

        /**
         * countNumbers方法：统计成员
         *
         * @param numbers ArrayList<OthersInformation> 群聊成员
         */

        private void countNumbers(ArrayList<OthersInformation> numbers) {
            // 在线成员
            int countOnline = 0;
            // 遍历成员
            for (OthersInformation number : numbers) {
                // 统计在线成员
                if (number.getLine() == InstructionSet.ON_LINE) {
                    countOnline++;
                }
            }
            // 设置成员标签
            amount.setText("在线：" + countOnline + "\t" + "总人数：" + numbers.size());
        }

        @Override
        protected void updateItem(GroupInformation item, boolean empty) {
            // 调用父类的updateItem方法
            super.updateItem(item, empty);
            // 判断数据是否为空
            if (item != null && !empty) {
                // 设置账号标签
                id.setText(item.getGroupId());
                // 设置昵称标签
                name.setText(item.getGroupName());
                // 设置成员标签
                countNumbers(item.getNumbers());
                // 将整个布局设置为Item
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

    }

    /**
     * 该类用于自定义好友列表的显示效果
     */

    private static class CustomMessageListCell extends ListCell<Message> {

        /**
         * 基本属性区域
         */

        private final Label id;        // 昵称
        private final Label state;       // 消息状态
        private final HBox content;      // 顶层容器


        /**
         * 构造方法区域
         */

        public CustomMessageListCell() {
            // 创建Label对象
            this.id = new Label();
            this.state = new Label();
            this.content = new HBox(this.id, this.state);
            // 对整体布局进行格式化
            formatting();
        }

        /**
         * formatting方法：格式化布局
         */

        private void formatting() {
            // 设置字体
            id.setFont(new Font("宋体", 20));
            state.setFont(new Font("宋体", 12));
            // 设置宽度
            id.setPrefWidth(260);
            state.setPrefWidth(80);
            // 设置对齐方式
            state.setAlignment(Pos.CENTER_RIGHT);
            content.setAlignment(Pos.CENTER_LEFT);
        }

        /**
         * check方法：检查并设置消息状态
         */

        private void check(Message item) {
            if (item.getState() == InstructionSet.NEW_MESSAGE) {
                // 设置文本
                state.setText("新消息");
                // 设置颜色
                state.setTextFill(Color.RED);
            }
            if (item.getState() == InstructionSet.OLD_MESSAGE) {
                // 设置文本
                state.setText("");
                // 设置颜色
                state.setTextFill(Color.BLACK);
            }
        }

        @Override
        protected void updateItem(Message item, boolean empty) {
            // 调用父类的updateItem方法
            super.updateItem(item, empty);
            // 判断数据是否为空
            if (item != null && !empty) {
                // 设置账号标签
                id.setText(item.getId());
                // 检查是否有新消息
                check(item);
                // 将整个布局设置为Item
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }

    }

    /**
     * Message类：便于显示消息列表
     */

    private static class Message {

        /**
         * 基本属性区域
         */

        private String id;  // 账号
        private int state;  // 消息状态

        /**
         * 构造方法区域
         */

        public Message(String id, int state) {
            this.id = id;
            this.state = state;
        }

        /**
         * setter方法区域
         */

        public void setId(String id) {
            this.id = id;
        }


        public void setState(int state) {
            this.state = state;
        }

        /**
         * getter方法区域
         */

        public String getId() {
            return id;
        }

        public int getState() {
            return state;
        }
    }

}

/*
 * 注：暂时废弃
   private void receiveId() {
        // 字节数据
        byte[] bytes = new byte[100];
        // 读取数据
        int len = DataSwap.read(bytes);
        // 关闭输入流
        DataSwap.closeInput();
        // 建立连接并获取数据包
        System.out.println("数据获取：" + createDataPacket(new String(bytes, 0, len)));
    }
 */

/*
 * 注：暂时废弃
    private void readAndWrite(FileDataPacket fileData) {
        // 解析数据包，创建文件路径
        File file = createFiles();
        // 声明对象输入输出流
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 创建对象输入输出流
            ois = new ObjectInputStream(new FileInputStream(file));
            oos = new ObjectOutputStream(new FileOutputStream(file));
            // 从文件中读取对象
            Object obj = ois.readObject();
            // 类型判断
            if (obj instanceof ChatRecord) {
                // 读取数据
                ChatRecord list = (ChatRecord) ois.readObject();
                // 添加数据
                list.getFileList().add(fileData);
                // 写回数据
                oos.writeObject(list);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    // 释放资源
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    // 释放资源
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/

/*
 * 注：暂时废弃
    public ArrayList<FileDataPacket> readFile(File file) {
        if (file.length() != 0) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                Object obj = ois.readObject();
                if (obj instanceof ChatRecord) {
                    return ((ChatRecord) obj).getFileList();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new ArrayList<>();
    }
*/