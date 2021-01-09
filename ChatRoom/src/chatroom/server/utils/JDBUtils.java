package chatroom.server.utils;

import chatroom.communal.javabeans.GroupInformation;
import chatroom.communal.javabeans.OthersInformation;
import chatroom.communal.javabeans.UserInformation;
import chatroom.communal.javabeans.UserLogin;

import java.sql.*;
import java.util.ArrayList;

/**
 * 数据库工具类：
 *
 * @description: 实现数据库的连接，对数据的增删查改
 * @author: Vcatory
 * @date: 2020-12-17 9:20
 */
public class JDBUtils {

    private static Connection connection;

    // 私有化构造方法
    private JDBUtils() {
    }

    // 默认MySQL驱动程序
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    // URL
    private static final String url = "jdbc:mysql://localhost:3306/chatroom?useSSL=true&serverTimezone=UTC" +
            "&useUnicode=true&characterEncoding=utf-8";

    /**
     * createConnection方法：生成数据库连接
     *
     * @param userName String 用户名
     * @param password String 密码
     * @return boolean ture为连接成功
     */

    public static boolean createConnection(String userName, String password) {
        try {
            // 利用Java反射，加载MySQL驱动
            Class.forName(driverName);
            // 获取Connection连接
            connection = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection != null;
    }

    /**
     * queryUser方法：根据用户账号，查询用户是否存在
     *
     * @param userID String  用户账号
     * @return boolean true表示存在
     */

    public static boolean queryUserLogin(String userID) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from user_login where user_identification = ?");
            // 设置参数
            preparedStatement.setString(1, userID);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集是否存在数据
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return false;
    }

    /**
     * queryUser方法：根据用户账号和用户密码进行查询，用于登录验证
     *
     * @param userID  String  用户账号
     * @param userPwd String  用户密码
     * @return boolean true表示通过
     */

    public static boolean queryUserLogin(String userID, String userPwd) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from user_login where user_identification = ? and user_password = ?");
            // 设置参数
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, userPwd);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集是否存在数据
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return false;
    }

    /**
     * queryUserInformation方法：根据账号和电话号码，查询用户是否存在，用于修改密码
     *
     * @param userID      String 账号
     * @param phoneNumber String 密码
     * @return boolean true为存在
     */

    public static boolean queryUserInformation(String userID, String phoneNumber) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from user_information where user_identification = ? and user_phonenumber = ?");
            // 设置参数
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, phoneNumber);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集是否存在数据
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return false;
    }

    /**
     * queryGroupInformation方法：根据群聊账号，查询该群聊是否存在
     *
     * @param groupId String 群聊账号
     * @return boolean true为存在
     */

    public static boolean queryGroupInformation(String groupId) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from group_information where group_id = ?");
            // 设置参数
            preparedStatement.setString(1, groupId);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集是否存在数据
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return false;
    }

    /**
     * insertUserLogin方法：向数据库插入一条登录信息
     *
     * @param userLogin UserLogin   登录信息
     * @return boolean true为成功
     */

    public static boolean insertUserLogin(UserLogin userLogin) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（插入登录信息）
            preparedStatement = connection.prepareStatement(
                    "insert into user_login values (?,?,?)");
            // 设置参数
            preparedStatement.setString(1, userLogin.getId());
            preparedStatement.setString(2, userLogin.getPassword());
            preparedStatement.setInt(3, userLogin.getIsOnline());
            // 执行sql语句，并判断是否插入成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * insertUserInformation方法：向数据库插入一条用户信息记录
     *
     * @param userInformation UserInformation 用户信息
     * @return boolean true为成功
     */

    public static boolean insertUserInformation(UserInformation userInformation) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（插入用户信息）
            preparedStatement = connection.prepareStatement(
                    "insert into user_information values (?,?,?,?,?,?,?,?)");
            // 设置参数
            preparedStatement.setString(1, userInformation.getId());
            preparedStatement.setString(2, userInformation.getName());
            preparedStatement.setInt(3, userInformation.getAge());
            preparedStatement.setString(4, userInformation.getGender());
            preparedStatement.setDate(5, userInformation.getBirthday());
            preparedStatement.setString(6, userInformation.getPhoneNumber());
            preparedStatement.setString(7, userInformation.getEmail());
            preparedStatement.setString(8, userInformation.getPersonalizedSignature());
            // 执行sql语句，并判断是否插入成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * insertGroupInformation方法：向数据库插入一条群聊信息记录
     *
     * @param groupInformation GroupInformation 群聊信息
     * @return boolean true为成功
     */

    public static boolean insertGroupInformation(GroupInformation groupInformation) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（插入群聊信息）
            preparedStatement = connection.prepareStatement("insert into group_information values (?,?,?)");
            // 设置参数
            preparedStatement.setString(1, groupInformation.getGroupId());
            preparedStatement.setString(2, groupInformation.getGroupName());
            preparedStatement.setString(3, groupInformation.getMasterInformation().getId());
            // 执行sql语句，并判断是否插入成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * insertGroupRelationship方法：向数据库插入一条群聊关系记录
     *
     * @param groupId  String 群聊账号
     * @param numberId String 成员账号
     * @return boolean true为成功
     */

    public static boolean insertGroupRelationship(String groupId, String numberId) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（插入群聊信息）
            preparedStatement = connection.prepareStatement("insert into group_relationship values (?,?)");
            // 设置参数
            preparedStatement.setString(1, groupId);
            preparedStatement.setString(2, numberId);
            // 执行sql语句，并判断是否插入成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * insertFriendRelationship方法：向数据库中插入一条好友关系记录
     *
     * @param userId   String 用户账号
     * @param friendId String 好友账号
     * @return boolean true为添加成功
     */

    public static boolean insertFriendRelationship(String userId, String friendId) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（插入群聊信息）
            preparedStatement = connection.prepareStatement("insert into friend_relationship values (?,?)");
            // 设置参数
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, friendId);
            // 执行sql语句，并判断是否插入成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * updateUserLogin方法：根据账号修改数据库中的一条记录（找回密码）
     *
     * @param userLogin UserLogin 登录信息
     * @return boolean ture为成功
     */

    public static boolean updateUserLogin(UserLogin userLogin) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（修改登录信息）
            preparedStatement = connection.prepareStatement(
                    "update user_login set user_password = ? where user_identification = ?");
            // 设置参数
            preparedStatement.setString(1, userLogin.getPassword());
            preparedStatement.setString(2, userLogin.getId());
            // 执行sql语句，并判断是否修改成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * updateUserLogin方法：根据账号修改数据库中的一条记录（修改登录状态）
     *
     * @param userId   String 用户账号
     * @param isOnline int    登录状态 20为离线 21为在线
     * @return boolean ture为成功
     */

    public static boolean updateUserLogin(String userId, int isOnline) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（修改登录信息）
            preparedStatement = connection.prepareStatement(
                    "update user_login set online = ? where user_identification = ?");
            // 设置参数
            preparedStatement.setInt(1, isOnline);
            preparedStatement.setString(2, userId);
            // 执行sql语句，并判断是否修改成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * updateUserInformation方法：更新用户信息
     *
     * @param userInformation UserInformation 用户信息
     * @return boolean true为成功
     */

    public static boolean updateUserInformation(UserInformation userInformation) {
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（修改登录信息）
            preparedStatement = connection.prepareStatement(
                    "update user_information set user_name = ?, user_age = ?, user_gender = ?, user_birthday = ?," +
                            "user_phonenumber = ?, user_email = ?, personalizedsignature = ? where user_identification = ?");
            // 设置参数
            preparedStatement.setString(1, userInformation.getName());
            preparedStatement.setInt(2, userInformation.getAge());
            preparedStatement.setString(3, userInformation.getGender());
            preparedStatement.setDate(4, userInformation.getBirthday());
            preparedStatement.setString(5, userInformation.getPhoneNumber());
            preparedStatement.setString(6, userInformation.getEmail());
            preparedStatement.setString(7, userInformation.getPersonalizedSignature());
            preparedStatement.setString(8, userInformation.getId());
            // 执行sql语句，并判断是否修改成功
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(preparedStatement);
        }
        return false;
    }

    /**
     * getUserLogin方法：根据用户账号来获取用户信息
     *
     * @param userID String 用户账号
     * @return UserInformation 用户信息
     */

    public static UserInformation getUserInformation(String userID) {
        // 声明登录信息对象
        UserInformation userInformation = null;
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from user_information where user_identification = ?");
            // 设置参数
            preparedStatement.setString(1, userID);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 创建登录信息对象
            userInformation = new UserInformation();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                userInformation.setId(resultSet.getString(1));
                userInformation.setName(resultSet.getString(2));
                userInformation.setAge(resultSet.getInt(3));
                userInformation.setGender(resultSet.getString(4));
                userInformation.setBirthday(resultSet.getDate(5));
                userInformation.setPhoneNumber(resultSet.getString(6));
                userInformation.setEmail(resultSet.getString(7));
                userInformation.setPersonalizedSignature(resultSet.getString(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return userInformation;
    }

    /**
     * getUserFriends方法：根据用户账号获取用户好友账号列表
     *
     * @param userId String 用户账号
     * @return ArrayList<String> 好友账号列表
     */

    public static ArrayList<String> getUserFriends(String userId) {
        // 声明用户好友对象
        ArrayList<String> friends = null;
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from friend_relationship where user_identification = ?");
            // 设置参数
            preparedStatement.setString(1, userId);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 创建用户好友对象
            friends = new ArrayList<>();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                friends.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return friends;
    }

    /**
     * getOthersInformation方法：根据好友或成员账号，获取信息
     *
     * @param id String 账号
     * @return OthersInformation 信息
     */

    public static OthersInformation getOthersInformation(String id) {
        // 声明其他人信息对象
        OthersInformation others = null;
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select ul.user_identification, ui.user_name, ul.online " +
                            "from user_login as ul " +
                            "inner join (select user_identification, user_name from user_information WHERE user_identification = ?) as ui " +
                            "on ul.user_identification = ui.user_identification");
            // 设置参数
            preparedStatement.setString(1, id);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 创建其他人信息对象
            others = new OthersInformation();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                others.setId(resultSet.getString(1));
                others.setName(resultSet.getString(2));
                others.setLine(resultSet.getInt(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return others;
    }

    /**
     * getGroups方法：根据群主账号获取群聊信息
     *
     * @param masterInformation String 群主账号
     */

    public static void getGroups(OthersInformation masterInformation, ArrayList<GroupInformation> groups) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（群主）
            preparedStatement = connection.prepareStatement(
                    "select * from group_information where master_id = ?");
            // 设置参数
            preparedStatement.setString(1, masterInformation.getId());
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                groups.add(new GroupInformation(resultSet.getString(2),
                        resultSet.getString(1), masterInformation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
    }

    /**
     * getGroups方法：根据成员账号获取所在群聊的账号
     *
     * @param numberId String 成员账号
     * @return ArrayList<String> 群聊账号列表
     */

    public static ArrayList<String> getGroups(String numberId) {
        // 声明群聊账号列表
        ArrayList<String> groups = null;
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（成员）
            preparedStatement = connection.prepareStatement(
                    "select * from group_relationship where number_id = ?");
            // 设置参数
            preparedStatement.setString(1, numberId);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 创建群聊账号列表
            groups = new ArrayList<>();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                groups.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return groups;
    }

    /**
     * getGroup方法：根据群聊账号获取群聊信息
     *
     * @param groupId String 群聊账号
     * @return GroupInformation 群聊信息
     */

    public static GroupInformation getGroup(String groupId) {
        // 声明群聊信息对象
        GroupInformation group = null;
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象（成员）
            preparedStatement = connection.prepareStatement(
                    "select * from group_information where group_id = ?");
            // 设置参数
            preparedStatement.setString(1, groupId);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 创建群聊信息对象
            group = new GroupInformation();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                group.setGroupId(resultSet.getString(1));
                group.setGroupName(resultSet.getString(2));
                group.setMasterInformation(getOthersInformation(resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
        return group;
    }

    /**
     * getNumbers方法：根据群聊账号获取成员列表
     *
     * @param group GroupInformation 群聊信息
     */

    public static void getNumbers(GroupInformation group) {
        // 创建ResultSet对象
        ResultSet resultSet = null;
        // 创建PreparedStatement对象
        PreparedStatement preparedStatement = null;
        try {
            // 获取PreparedStatement对象
            preparedStatement = connection.prepareStatement(
                    "select * from group_relationship where group_id = ?");
            // 设置参数
            preparedStatement.setString(1, group.getGroupId());
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 将结果集转换成数据对象
            while (resultSet.next()) {
                group.getNumbers().add(getOthersInformation(resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closingResource(resultSet, preparedStatement);
        }
    }

    /**
     * closingResource方法：释放资源
     */

    public static void closingResource() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * closingResource方法：私有static方法，释放资源
     *
     * @param statement Statement
     */

    private static void closingResource(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * closingResource方法：私有static方法，释放资源
     *
     * @param resultSet ResultSet
     * @param statement Statement
     */

    public static void closingResource(ResultSet resultSet, Statement statement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closingResource(statement);
    }

}
