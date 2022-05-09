package shu.sag.anno.service;

import shu.sag.anno.pojo.*;

import java.util.List;

public interface UserService {
    // 用户登录
    User login(String username, String password);

    // 获取Anno对象
    public Anno getAnno(int currentIndex, Task task);

    //提交标注结果返回下一条标注数据
    int addAnnoResult(int userTaskID,
                      String resultTableName,
                      String username,
                      int textID,
                      String text,
                      String label,
                      String rawTableName);

    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(String username, int currentIndex, int pageSize);

    // 获取用户密码
    String getUserPWD(String username);

    // 更新用户密码
    int updateUserPWD(String username, String newPWD);

    //用户获取usertask
    UserTask getUserTaskByID(int id, String username);

    //获取用户标注任务数目
    int getUserTaskNum(String username);

    //获取Task
    Task getTaskByID(int id);

    //获取文本标注
    String getTextByID(String datasetTableName, int id);

    // 判断用户是否存在
    int UserisExist(String username);

    // 添加用户
    int Regist(User user);

    //按名字模糊搜索usertask
    List<UserTask> searchUserTask(int currentIndex,
                                  int pageSize,
                                  String username,
                                  String searchValue);

    // 按照条件搜索结果的总数
    int searchUserTaskResCount(String username,
                               String searchValue);

    //按名字模糊搜索usertask
    List<Task> searchPubTask(int currentIndex,
                                  int pageSize,
                                  String searchValue);

    // 按照条件搜索结果的总数
    int searchPubTaskResCount(String searchValue);

    // 用户申请任务
    int addApplication(String username, int taskid);

    // 用户撤回任务申请
    int withdrawApplication(String username, int id);

    // 获取用户申请列表
    List<Application> getUserApplications(String uername, int currentIndex, int pageSize);

    // 计算用户申请总数
    int applicationCount(String username);

}
