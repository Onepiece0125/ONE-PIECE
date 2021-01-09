package chatroom.client.controls;

import chatroom.client.terminal.SendingEnd;
import chatroom.client.terminal.Singleton;
import chatroom.client.terminal.Terminal;
import chatroom.client.utils.CustomMessageListCell;
import chatroom.client.list.MessageList;
import chatroom.client.utils.OthersSwap;
import chatroom.communal.InstructionSet;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.packet.FileDataPacket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 私聊控制类
 *
 * @description: 实现私聊发送消息和接受消息
 * @author: Vcatory
 * @date: 2021-01-05 14:34
 */

public class PrivateChatControl {

    @FXML
    private Label addressee;

    @FXML
    private ListView<FileDataPacket> messageListView;

    @FXML
    private TextArea messageTextArea;

    // 发送端
    private SendingEnd sendingEnd;
    // 发送者
    private UserInformation sender;
    // 接收者
    private OthersInformation receiver;
    // 消息数
    private int count;
    // 消息列表观察者
    private ObservableList<FileDataPacket> fileObservable;


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
        fileData.setReceiver(receiver.getId());
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
     * initControls方法：初始化控制组件
     */

    private void initControls() {
        assert addressee != null : "fx:id=\"addressee\" was not injected: check your FXML file 'FX_PrivateChat.fxml'.";
        assert messageListView != null : "fx:id=\"messageListView\" was not injected: check your FXML file 'FX_PrivateChat.fxml'.";
        assert messageTextArea != null : "fx:id=\"messageTextArea\" was not injected: check your FXML file 'FX_PrivateChat.fxml'.";
    }

    /**
     * initStage方法：初始化界面
     */

    private void initStage() {
        // 获取基本信息
        createBasicInformation();
        // 设置Label文本
        addressee.setText(receiver.getName() + "（" + receiver.getId() + "）");
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
        this.receiver = OthersSwap.getOthers();
    }

    /**
     * check方法：判断消息是否属于该窗口
     *
     * @param file FileDataPacket 文件数据包
     * @return boolean true为属于
     */

    private boolean check(FileDataPacket file) {
        // 判断发送消息
        if (file.getSender().equals(sender.getId()) && file.getReceiver().equals(receiver.getId())) {
            return true;
        }

        // 判断接收消息
        return file.getSender().equals(receiver.getId()) && file.getReceiver().equals(sender.getId());
    }

    /**
     * readMessageFiles方法：读取消息列表
     *
     * @return ArrayList<FileDataPacket> 消息列表
     */

    private ArrayList<FileDataPacket> readMessageFiles() {
        // 创建消息列表
        ArrayList<FileDataPacket> files = new ArrayList<>();
        // 获取总消息列表的大小
        count = MessageList.getFileList().size();
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
                fileData.setReceiver(receiver.getId());
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
     * startThread方法：启动监听消息列表的线程
     */

    private void startThread() {
        // 启动线程
        new Thread(new Task<Void>() {

            @Override
            protected Void call() {
                while (MessageList.getOpenList().contains(receiver.getId())) {
                    // 监听消息列表是否改变
                    if (count != MessageList.getFileList().size()) {
                        // 启动子线程
                        Platform.runLater(() -> {
                            // 遍历新消息
                            for (int i = count; i < MessageList.getFileList().size(); i++) {
                                // 判断消息
                                if (check(MessageList.getFileList().get(i))) {
                                    // 将新消息添加到ObservableList中
                                    fileObservable.add(MessageList.getFileList().get(i));
                                }
                            }
                            // 更新消息个数
                            count = MessageList.getFileList().size();
                            // 更新消息界面
                            messageListView.refresh();
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
    }
}



//     private static class CustomMessageListCell extends ListCell<FileDataPacket> {
//
//         /**
//          * 基本属性
//          */
//
//         private Label senderId;     // 发送者
//         private Label sendTime;     // 发送时间
//         private HBox content;       // 顶级容器
//         // 子级容器
//         private VBox vBox;
//         private HBox hBox;
//
//         /**
//          * 构造方法区域
//          */
//
//         public CustomMessageListCell() {
//
//         }
//
//         private void format() {
//             // 设置两个组件之间的距离
//             hBox.setSpacing(10);
//         }
//
//         /**
//          * check方法：检查文件信息
//          *
//          * @param item FileDataPacket 文件数据包
//          */
//
//         private void check(FileDataPacket item) {
//             // 创建发送者标签和发送时间标签
//             this.senderId = new Label();
//             this.sendTime = new Label();
//             // 将两个标签加入到HBox容器中
//             this.hBox = new HBox(this.sendTime, this.senderId);
//             // 发送消息格式
//             if (item.getState() == InstructionSet.SEND) {
//                 // 字符型文件
//                 if (item.getType() == InstructionSet.CHARACTER) {
//                     // 创建消息标签
//                     Label message = new Label();
//                     // 将消息标签和VBox容器加入到VBox容器中
//                     vBox = new VBox(hBox, message);
//                     // 设置消息内容
//                     message.setText(new String(item.getFile(), StandardCharsets.UTF_8));
//                 }
//                 // 非字符型文件
//                 if (item.getType() == InstructionSet.NO_CHARACTER) {
//                     // 创建链接
//                     Hyperlink link = new Hyperlink();
//                     // 将超链接和VBox容器加入到VBox容器中
//                     vBox = new VBox(hBox, link);
//                     // 设置文本
//                     link.setText(new String(item.getFile(), StandardCharsets.UTF_8));
//                     // 设置点击事件
//                     link.setOnAction(event -> {
//                         try {
//                             // Desktop.getDesktop().open(new File(new String(item.getFile(), StandardCharsets.UTF_8)));
//                             String[] cmd = new String[]{
//                                     "cmd", "/c", "start", " ",
//                                     new String(item.getFile(), StandardCharsets.UTF_8)
//                             };
//                             Runtime.getRuntime().exec(cmd);
//                         } catch (IOException e) {
//                             e.printStackTrace();
//                         }
//                     });
//                 }
//                 // 将vBox容器加入到顶级容器中
//                 content = new HBox(vBox);
//                 // 将说有组件和布局设置成右对齐
//                 senderId.setAlignment(Pos.CENTER_RIGHT);
//                 sendTime.setAlignment(Pos.CENTER_RIGHT);
//                 hBox.setAlignment(Pos.CENTER_RIGHT);
//                 vBox.setAlignment(Pos.CENTER_RIGHT);
//                 content.setAlignment(Pos.CENTER_RIGHT);
//                 // 格式化布局
//                 format();
//             }
//             // 接受消息格式
//             if (item.getState() == InstructionSet.RECEIVE) {
//                 // 字符型文件
//                 if (item.getType() == InstructionSet.CHARACTER) {
//                     // 创建消息标签
//                     Label message = new Label();
//                     // 将消息标签和VBox容器加入到VBox容器中
//                     vBox = new VBox(hBox, message);
//                     // 设置消息内容
//                     message.setText(new String(item.getFile(), StandardCharsets.UTF_8));
//                 }
//                 // 非字符型文件
//                 if (item.getType() == InstructionSet.NO_CHARACTER) {
//                     // 创建链接
//                     Hyperlink link = new Hyperlink();
//                     // 将超链接和VBox容器加入到VBox容器中
//                     vBox = new VBox(hBox, link);
//                     // 设置文本
//                     link.setText(new String(item.getFile(), StandardCharsets.UTF_8));
//                     // 设置点击事件
//                     link.setOnAction(event -> {
//                         try {
//                             // Desktop.getDesktop().open(new File(new String(item.getFile(), StandardCharsets.UTF_8)));
//                             String[] cmd = new String[]{
//                                     "cmd", "/c", "start", " ",
//                                     new String(item.getFile(), StandardCharsets.UTF_8)
//                             };
//                             Runtime.getRuntime().exec(cmd);
//                         } catch (IOException e) {
//                             e.printStackTrace();
//                         }
//                     });
//                 }
//                 // 将vBox容器加入到顶级容器中
//                 content = new HBox(vBox);
//                 // 将所有组件和布局设置成左对齐
//                 senderId.setAlignment(Pos.CENTER_LEFT);
//                 sendTime.setAlignment(Pos.CENTER_LEFT);
//                 hBox.setAlignment(Pos.CENTER_LEFT);
//                 vBox.setAlignment(Pos.CENTER_LEFT);
//                 content.setAlignment(Pos.CENTER_LEFT);
//                 // 格式化布局
//                 format();
//             }
//         }
//
//         @Override
//         protected void updateItem(FileDataPacket item, boolean empty) {
//             // 调用父类的updateItem方法
//             super.updateItem(item, empty);
//             // 判断数据是否为空
//             if (item != null && !empty) {
//                 // 检查数据
//                 check(item);
//                 // 设置发送者标签
//                 this.senderId.setText(item.getSender());
//                 // 设置发送时间标签
//                 this.sendTime.setText(item.getSendTime());
//                 // 将整个布局设置为Item
//                 setGraphic(content);
//             } else {
//                 setGraphic(null);
//             }
//         }
//
//     }
//
// }


/*
 * 注：暂时废弃
    private void createFiles() {
        // 指定文件路径
        this.file = new File("files" + File.separator + sender.getId() + File.separator
                + receiver.getId() + File.separator + "message.object");
        // 获取父目录
        File parent = this.file.getParentFile();
        // 判断父目录是否存在
        if (!parent.exists()) {
            // 创建父目录
            System.out.println("创建父目录：" + parent.mkdirs());
        }
        // 声明对象输出流
        ObjectOutputStream oos = null;
        // 判断文件是否存在
        if (!this.file.exists()) {
            try {
                // 创建文件
                System.out.println("创建文件：" + this.file.createNewFile());
                // 创建对象输出流
                oos = new ObjectOutputStream(new FileOutputStream(this.file));
                // 将消息列表写入文件
                oos.writeObject(new ChatRecord());
                // 刷新流
                oos.flush();
                // 获取消息列表
                files = new ArrayList<>();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
        // 设置观察者，监听.object文件
        FileAlterationObserver observer = new FileAlterationObserver(parent, FileFilterUtils.suffixFileFilter(".object"));
        // 添加监听事件
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileChange(File file) {
                // 启动子线程
                Platform.runLater(() -> {
                    // 获取消息列表
                    files = readFile(file);
                    // 遍历新消息
                    for (int i = fileObservable.size(); i < Objects.requireNonNull(files).size(); i++) {
                        // 将信息添加到ObservableList中
                        fileObservable.add(files.get(i));
                    }
                    // 更新消息列表
                    messageListView.refresh();
                });
                super.onFileChange(file);
            }
        });
        // 创建监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000);
        // 将观察者添加到监听器
        monitor.addObserver(observer);
        try {
            // 启动监听器
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
/*
 * 注：暂时废弃
    private ArrayList<FileDataPacket> readFile(File file) {
        // 声明对象输入流
        ObjectInputStream ois = null;
        try {
            // 创建对象输入流
            ois = new ObjectInputStream(new FileInputStream(file));
            // 从文件中读取对象
            Object obj = ois.readObject();
            // 类型判断
            if (obj instanceof ChatRecord) {
                // 获取消息列表
                return ((ChatRecord) obj).getFileList();
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
        }
        return null;
    }
*/
/*
 * 注：暂时废弃
    private void writeFile(FileDataPacket fileData) {
        // 声明对象输出流
        ObjectOutputStream oos = null;
        try {
            // 删除文件
            System.out.println("删除文件：" + file.delete());
            // 创建对象输入输出流
            oos = new ObjectOutputStream(new FileOutputStream(file));
            // 将新消息添加到消息列表
            files.add(fileData);
            // 更新历史记录
            oos.writeObject(files);
            // 刷新流
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
