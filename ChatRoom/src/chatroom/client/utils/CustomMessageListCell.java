package chatroom.client.utils;

import chatroom.communal.InstructionSet;
import chatroom.communal.packet.FileDataPacket;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义ListView显示效果
 *
 * @description: 自定义消息列表的显示效果
 * @author: Vcatory
 * @date: 2021-01-07 9:55
 */
public class CustomMessageListCell extends ListCell<FileDataPacket> {

    /**
     * 基本属性
     */

    private Label senderId;     // 发送者
    private Label sendTime;     // 发送时间
    private HBox content;       // 顶级容器
    // 子级容器
    private VBox vBox;
    private HBox hBox;

    /**
     * 构造方法区域
     */

    public CustomMessageListCell() {

    }

    private void format() {
        // 设置两个组件之间的距离
        hBox.setSpacing(10);
    }

    /**
     * check方法：检查文件信息
     *
     * @param item FileDataPacket 文件数据包
     */

    private void check(FileDataPacket item) {
        // 创建发送者标签和发送时间标签
        this.senderId = new Label();
        this.sendTime = new Label();
        // 将两个标签加入到HBox容器中
        this.hBox = new HBox(this.sendTime, this.senderId);
        // 发送消息格式
        if (item.getState() == InstructionSet.SEND) {
            // 字符型文件
            if (item.getType() == InstructionSet.CHARACTER) {
                // 创建消息标签
                Label message = new Label();
                // 将消息标签和VBox容器加入到VBox容器中
                vBox = new VBox(hBox, message);
                // 设置消息内容
                message.setText(new String(item.getFile(), StandardCharsets.UTF_8));
                // 设置字体大小
                message.setFont(new Font(16));
            }
            // 非字符型文件
            if (item.getType() == InstructionSet.NO_CHARACTER) {
                // 创建链接
                Hyperlink link = new Hyperlink();
                // 将超链接和VBox容器加入到VBox容器中
                vBox = new VBox(hBox, link);
                // 设置文本
                link.setText(new String(item.getFile(), StandardCharsets.UTF_8));
                // 设置字体大小
                link.setFont(new Font(20));
                // 设置点击事件
                link.setOnAction(event -> {
                    try {
                        String[] cmd = new String[]{
                                "cmd", "/c", "start", " ",
                                new String(item.getFile(), StandardCharsets.UTF_8)
                        };
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            // 将vBox容器加入到顶级容器中
            content = new HBox(vBox);
            // 将说有组件和布局设置成右对齐
            senderId.setAlignment(Pos.CENTER_RIGHT);
            sendTime.setAlignment(Pos.CENTER_RIGHT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.setAlignment(Pos.CENTER_RIGHT);
            content.setAlignment(Pos.CENTER_RIGHT);
            // 格式化布局
            format();
        }
        // 接受消息格式
        if (item.getState() == InstructionSet.RECEIVE) {
            // 字符型文件
            if (item.getType() == InstructionSet.CHARACTER) {
                // 创建消息标签
                Label message = new Label();
                // 将消息标签和VBox容器加入到VBox容器中
                vBox = new VBox(hBox, message);
                // 设置消息内容
                message.setText(new String(item.getFile(), StandardCharsets.UTF_8));
                // 设置字体大小
                message.setFont(new Font(16));
            }
            // 非字符型文件
            if (item.getType() == InstructionSet.NO_CHARACTER) {
                // 创建链接
                Hyperlink link = new Hyperlink();
                // 将超链接和VBox容器加入到VBox容器中
                vBox = new VBox(hBox, link);
                // 设置文本
                link.setText(new String(item.getFile(), StandardCharsets.UTF_8));
                // 设置字体大小
                link.setFont(new Font(20));
                // 设置点击事件
                link.setOnAction(event -> {
                    try {
                        String[] cmd = new String[]{
                                "cmd", "/c", "start", " ",
                                new String(item.getFile(), StandardCharsets.UTF_8)
                        };
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            // 将vBox容器加入到顶级容器中
            content = new HBox(vBox);
            // 将所有组件和布局设置成左对齐
            senderId.setAlignment(Pos.CENTER_LEFT);
            sendTime.setAlignment(Pos.CENTER_LEFT);
            hBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setAlignment(Pos.CENTER_LEFT);
            content.setAlignment(Pos.CENTER_LEFT);
            // 格式化布局
            format();
        }
    }

    @Override
    protected void updateItem(FileDataPacket item, boolean empty) {
        // 调用父类的updateItem方法
        super.updateItem(item, empty);
        // 判断数据是否为空
        if (item != null && !empty) {
            // 检查数据
            check(item);
            // 设置发送者标签
            this.senderId.setText(item.getSender());
            // 设置发送时间标签
            this.sendTime.setText(item.getSendTime());
            // 将整个布局设置为Item
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

}
