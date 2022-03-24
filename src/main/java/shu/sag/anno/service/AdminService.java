package shu.sag.anno.service;

import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Task;
import shu.sag.anno.pojo.UserTask;

import java.util.List;

public interface AdminService {
    // 增加配置
    int addConfig(Config config);

    // 通过id删除任务配置
    int deleteConfig(int id);

    // 分页获取任务配置
    List<Config> getAllConfig(int currentIndex, int pageSize);

    // 获取配置项的总数
    int getConfigNum();

    // 通过configid获取任务
    List<Task> getTaskByConfigID(int ConfigID);

    // 更新任务配置
    int updateConfig(Config config);

    //获取所有的任务
    List<Task> getAllTask(int currentIndex, int pageSize);

    // 获取配置项的总数
    int getTaskNum();

    // 通过id删除任务
    int deleteTaskByID(int id);

    // 增加任务
    int addTask(Task task);

    // 更新任务
    int updateTask(Task task);

    //管理员获取所有的用户任务
    List<UserTask> getAllUserTask(int currentIndex, int pageSize);

    // 获取usertask总数
    int getAllUserTaskNum();

    //为用户分配任务
    int addUserTask(UserTask userTask);

    //管理员删除用户任务
    int deleteUserTaskByID(int id);



    // 通过id获取任务配置
    Config getConfigByID(int id);
    // 通过标注类型获取任务配置
    Config getConfigByType(String type);

    // 通过id获取任务
    Task getTaskByID(int id);



}
