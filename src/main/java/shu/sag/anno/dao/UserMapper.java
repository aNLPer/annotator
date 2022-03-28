package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.User;
import java.util.List;


public interface UserMapper {
    // 用户登录
    User login(@Param("username") String username, @Param("password") String password);

    // 获取用户列表
    List<User> getUserListByRole(@Param("currentIndex") int currentIndex, @Param("pageSize")int pageSize,  @Param("role")String role);

    // 获取特定类型的用户数量
    int getUserNumByRole(@Param("role") String role);

    // 删除用户
    int deleteUserByUsername(String username);

    // 更新用户密码
    int updateUserPassword(@Param("username") String username, @Param("password") String password);

    // 更新用户状态
    int updateUserStatus(@Param("username")String username, @Param("status") String status);

    // 添加用户
    int addUser(User user);

    // 判断username是否存在
    int UserisExist(@Param("username") String username);

    // 用户注册
    int Regist(User user);
}
