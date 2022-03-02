package shu.sag.anno.service;

import shu.sag.anno.pojo.Anno;
import shu.sag.anno.pojo.AnnoResult;
import shu.sag.anno.pojo.User;
import shu.sag.anno.pojo.UserTask;

import java.util.List;

public interface UserService {
    // 用户登录
    User login(String username, String password);

    // 获取Anno对象
    public Anno getAnno(int userTaskID, int currentIndex, int taskID);

    //提交标注结果返回下一条标注数据
    int addAnnoResult(AnnoResult annoResult, int userTaskID, int currentAnnoIndex);

    //用户开始标注
    Anno startAnno(int userTaskID, int currentIndex, int taskID);

    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(String userAccount, int currentIndex, int pageSize);
}
