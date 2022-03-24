package shu.sag.anno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.sag.anno.dao.UserMapper;
import shu.sag.anno.dao.UserTaskMapper;
import shu.sag.anno.pojo.User;

import java.util.List;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTaskMapper userTaskMapper;

    @Override
    public List<User> getUserListByRole(int currentIndex, int pageSize, String role) {
        return userMapper.getUserListByRole(currentIndex, pageSize, role);
    }

    @Override
    public int getUserNumByRole(String role) {
        return userMapper.getUserNumByRole(role);
    }

    @Override
    public int deleteUserByUsername(String username) {
        return userMapper.deleteUserByUsername(username);
    }

    @Override
    public int deleteUserTaskByUsername(String username) {
        return userTaskMapper.deleteUserTaskByUsername(username);
    }

    @Override
    public int updateUserPassword(String username, String password) {
        return userMapper.updateUserPassword(username, password);
    }

    @Override
    public int updateUserStatus(String username, String status) {
        return userMapper.updateUserStatus(username, status);
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public int UserisExist(String username) {
        return userMapper.UserisExist(username);
    }


}
