package chatroom.communal.javabeans;

import java.io.Serializable;
import java.sql.Date;

/**
 * 用户信息：
 *
 * @description: 保存一个用户的基本信息
 * @author: Vcatory
 * @date: 2020-12-20 10:34
 */

public class UserInformation implements Serializable {

    /**
     * 基本属性区域
     */

    private String id;                      // 用户账号
    private String name;                    // 用户昵称
    private int age;                        // 用户年龄
    private String gender;                  // 用户性别
    private Date birthday;                  // 用户生日
    private String phoneNumber;             // 电话号码
    private String email;                   // 邮箱地址
    private String personalizedSignature;   // 个性签名

    /**
     * 构造方法区域
     */

    public UserInformation() {
    }

    public UserInformation(String id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public UserInformation(String id, String name, int age, String gender, Date birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
    }

    public UserInformation(String id, String name, int age, String gender, Date birthday,
                           String phoneNumber, String email, String personalizedSignature) {
        this(id, name, age, gender, birthday);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.personalizedSignature = personalizedSignature;
    }

    /**
     * setter方法区域
     */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    /**
     * getter方法区域
     */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    @Override
    public String toString() {
        return "\t" + "账号：" + id + "\t" +
                "昵称：" + name + "\t" +
                "年龄：" + age + "\t" +
                "性别：" + gender + "\t" +
                "生日：" + birthday + "\t" +
                "电话号码：" + phoneNumber + "\t" +
                "电子邮箱：" + email + "\t" +
                "个性签名：" + personalizedSignature + "\t";
    }
}
