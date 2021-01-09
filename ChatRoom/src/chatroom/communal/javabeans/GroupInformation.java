package chatroom.communal.javabeans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 群聊信息：
 *
 * @description: 保存一个群聊的基本信息
 * @author: Vcatory
 * @date: 2021-01-03 10:50
 */
public class GroupInformation implements Serializable {

    /**
     * 基本属性区域
     */

    private String groupName;                       // 群聊名称
    private String groupId;                         // 群聊账号
    private OthersInformation masterInformation;    // 群主账号
    // 群聊成员
    private ArrayList<OthersInformation> numbers;

    /**
     * 构造方法区域
     */

    public GroupInformation() {
        // 创建群聊列表
        numbers = new ArrayList<>();
    }

    public GroupInformation(String groupId) {
        this.groupId = groupId;
        // 创建群聊列表
        numbers = new ArrayList<>();
    }

    public GroupInformation(String groupName, String groupId, OthersInformation masterInformation) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.masterInformation = masterInformation;
        // 创建群聊列表
        numbers = new ArrayList<>();
        // 将群主加入到群聊成员
        numbers.add(masterInformation);
    }

    /**
     * setter方法区域
     */

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setMasterInformation(OthersInformation masterInformation) {
        // 判断是否已有群主
        if (this.masterInformation == null) {
            // 将群主加入到群聊成员
            numbers.add(masterInformation);
        } else {
            // 遍历成员列表
            for (int i = 0; i < numbers.size(); i++) {
                // 查找群主
                if (numbers.get(i).getId().equals(masterInformation.getId())) {
                    // 更新群主
                    numbers.set(i, masterInformation);
                }
            }
        }
        this.masterInformation = masterInformation;
    }

    public void setNumbers(ArrayList<OthersInformation> numbers) {
        this.numbers = numbers;
    }

    /**
     * getter方法区域：
     */

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public OthersInformation getMasterInformation() {
        return masterInformation;
    }

    public ArrayList<OthersInformation> getNumbers() {
        return numbers;
    }

    /**
     * traversalNumbers方法：遍历成员列表
     *
     * @return String
     */

    private String traversalNumbers() {
        StringBuilder sb = new StringBuilder();
        if (numbers.size() != 0) {
            for (OthersInformation number : numbers) {
                sb.append("\n\t").append(number);
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\t" + "群聊名称：" + groupName + "\t" +
                "群聊账号：" + groupId + "\n\t" +
                "群主信息：" + "\n\t" + masterInformation + "\n\t" +
                "成员列表：" + traversalNumbers() + "\t";
    }
}
