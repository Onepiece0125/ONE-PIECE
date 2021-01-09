package chatroom.client.list;

import chatroom.communal.packet.FileDataPacket;

import java.io.File;
import java.util.ArrayList;

/**
 * 消息文件列表
 *
 * @description: 保存消息历史记录
 * @author: Vcatory
 * @date: 2021-01-06 15:12
 */
public class MessageList {

    /**
     * 基本属性区域
     */

    // 文件路经
    private static File file;
    // 窗口打开列表
    private static ArrayList<String> openList;
    // 数据列表
    private static ArrayList<FileDataPacket> fileList;

    /**
     * 私有化构造方法
     */

    private MessageList() {

    }

    /**
     * setter方法区域
     */

    public static void setFile(File file) {
        MessageList.file = file;
    }

    public static void setOpenList(ArrayList<String> openList) {
        MessageList.openList = openList;
    }

    public static void setFileList(ArrayList<FileDataPacket> fileList) {
        MessageList.fileList = fileList;
    }

    /**
     * getter方法区域
     */

    public static File getFile() {
        return file;
    }

    public static ArrayList<String> getOpenList() {
        return openList;
    }

    public static ArrayList<FileDataPacket> getFileList() {
        return fileList;
    }
}
