package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Dataset;
import shu.sag.anno.pojo.User;
import java.util.List;


public interface UserMapper {
    // 用户登录
    User login(@Param("username") String username, @Param("password") String password);

    // 获取用户密码
    String getUserPWD(@Param("username") String username);

    // 获取用户列表
    List<User> getUserListByRole(@Param("currentIndex") int currentIndex, @Param("pageSize")int pageSize,  @Param("role")String role);

    // 获取特定类型的用户数量
    int getUserNumByRole(@Param("role") String role);

    // 删除用户
    int deleteUserByUsername(String username);

    // 更新用户密码
    int updateUserPassword(@Param("username") String username, @Param("newPWD") String newPWD);

    // 更新用户状态
    int updateUserStatus(@Param("username")String username, @Param("status") String status);

    // 添加用户
    int addUser(User user);

    // 判断username是否存在
    int UserisExist(@Param("username") String username);

    // 用户注册
    int Regist(User user);

    // 通过账号获取用户对象
    User getUserByUsername(String username);

    // 模糊搜索user
    List<User> searchUser(@Param("currentIndex") int currentIndex,
                             @Param("pageSize") int pageSize,
                             @Param("searchRole") String searchRole,
                             @Param("searchValue") String searchValue);

    // 模糊搜索结果数目
    int searchUserResCount(@Param("searchRole") String searchRole,@Param("searchValue")String searchValue);

    // 提交标注结果
    int addAnnoResult(@Param("resultTableName") String resultTableName,
                      @Param("username") String username,
                      @Param("textID") int textID,
                      @Param("text") String text,
                      @Param("label") String label,
                      @Param("rawTableName") String rawTableName);

    // 更新标注结果
    int updateAnnoResult(@Param("resultTableName") String resultTableName,
                         @Param("username") String username,
                         @Param("textID") int textID,
                         @Param("text") String text,
                         @Param("label") String label);
}
