package chatroom.communal.packet;

import java.io.Serializable;

/**
 * 文件数据包
 *
 * @description: 用于传输任何类型的文件
 * @author: Vcatory
 * @date: 2021-01-05 16:53
 */

public class FileDataPacket implements Serializable {

    /**
     * 基本属性区域
     */

    private byte[] file;          // 文件
    private String sender;        // 发送者
    private String receiver;      // 接收者
    private String fileName;      // 文件名
    private String sendTime;      // 发送时间

    private int type;             // 文件类型
    private int state;            // 文件状态

    /**
     * 构造方法区域
     */

    public FileDataPacket() {
    }

    public FileDataPacket(byte[] file, String sender, String receiver, String sendTime, String isSetOrName, int type, int state) {
        this.file = file;
        this.sender = sender;
        this.receiver = receiver;
        this.sendTime = sendTime;
        this.fileName = isSetOrName;
        this.type = type;
        this.state = state;
    }

    /**
     * setter方法区域
     */

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setFileName(String isSetOrName) {
        this.fileName = isSetOrName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * getter方法区域
     */

    public byte[] getFile() {
        return file;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getFilename() {
        return fileName;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }
}
