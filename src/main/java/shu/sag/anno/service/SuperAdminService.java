package shu.sag.anno.service;

import shu.sag.anno.pojo.User;

import java.util.List;

public interface SuperAdminService {
    // 根据角色获取用户列表
    List<User> getUserListByRole(int currentIndex, int pageSize, String role);
    // 获取对应角色用户数量
    int getUserNumByRole(String role);
    // 删除用户
    int deleteUserByUsername(String username);
    // 删除用户相关的任务
    int deleteUserTaskByUsername(String username);
    // 更新用户密码
    int updateUserPassword(String username, String password);
    // 更新用户状态
    int updateUserStatus(String username, String status);
    // 添加用户
    int addUser(User user);
    // 判断用户是否存在
    int UserisExist(String username);
}
