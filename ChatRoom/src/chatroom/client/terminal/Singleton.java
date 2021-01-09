package chatroom.client.terminal;

/**
 * 终端：
 *
 * @description: 封装发送端和接收端, 并添加创建和删除功能
 * @author: Vcatory
 * @date: 2020-12-28 8:31
 */
public class Singleton {

    /**
     * 基本属性区域：
     */

    private static Terminal terminal;   // 终端

    /**
     * 私有化构造方法：
     */

    private Singleton() {

    }

    /**
     * create方法：创建终端
     * <p>
     * // * @param userId String 唯一标识符
     */

    private static Terminal create() {
        // 新建终端
        terminal = new Terminal();
        // 返回终端
        return terminal;
    }

    /**
     * get方法：获取终端
     * <p>
     * // * @param userId String 唯一标识符
     *
     * @return Terminal 终端
     */

    public static Terminal get() {
        // 打印当前终端
        traversalTerminals();
        // 如果不为空，返回该对象
        if (terminal != null) {
            return terminal;
        }
        // 否则创建一个新对象
        return create();
    }

    /**
     * remove方法：移除终端
     */
    public static void remove() {
        terminal.close();
    }

    /**
     * traversalTerminals方法：打印当前终端
     */

    private static void traversalTerminals() {
        System.out.print("--当前终端：");
        if (terminal != null) {
            System.out.println(terminal.getUserId());
        } else {
            System.out.println("null");
        }
    }

}
