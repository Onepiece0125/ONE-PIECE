package chatroom.client.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * 接收端：
 *
 * @description: 为客户端接收数据
 * @author: Vcatory
 * @date: 2020-12-27 20:35
 */
public class ReceivingEnd {

    /**
     * 基本数据区域：
     */

    private ObjectInputStream ois;

    /**
     * 构造方法区域：
     */

    public ReceivingEnd(InputStream ois) {
        try {
            this.ois = new ObjectInputStream(ois);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter方法区域：
     */

    public ObjectInputStream getObjectInputStream() {
        return ois;
    }

    /**
     * readObject方法：接受一个数据包
     *
     * @return Object 数据包
     */

    public Object readObject() {
        try {
            // 读取数据
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
