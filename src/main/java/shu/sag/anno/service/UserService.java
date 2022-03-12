package shu.sag.anno.service;

import shu.sag.anno.pojo.*;

import java.util.List;

public interface UserService {
    // 用户登录
    User login(String username, String password);

    // 获取Anno对象
    public Anno getAnno(int currentIndex, Task task);

    //提交标注结果返回下一条标注数据
    boolean addAnnoResult(int userTaskID,
                      String resultTableName,
                      String username,
                      int textID,
                      String text,
                      String label,
                      String rawTableName);

    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(String username, int currentIndex, int pageSize);

    //用户获取usertask
    UserTask getUserTaskByID(int id, String username);

    //获取用户标注任务数目
    int getUserTaskNum(String username);

    //获取Task
    Task getTaskByID(int id);

    //获取文本标注
    String getTextByID(String datasetTableName, int id);
}
