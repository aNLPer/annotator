package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.UserTask;
import java.util.List;

public interface UserTaskMapper {

    // 通过taskID获取UserTask
    List<UserTask> getUserTaskByTaskID(int id);



    //管理员为用户分配任务
    int addUserTask(UserTask userTask);
    //管理员删除用户任务
    int deleteUserTaskByID(int id);
    //管理员获取有的用户任务
    List<UserTask> getAllUserTask(int currentIndex, int pageSize);
    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(@Param("username") String username, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    // 修改当前标注index
    int updateUserTaskCurrentAnnoIndex(@Param("userTaskID") int userTaskID, @Param("currentAnnoIndex") int currentAnnoIndex);
    // 获取用户标注任务总数
    int getUserTaskNum(String username);

    int currentAnnoIndexAdd1(@Param("userTaskID") int userTaskID);

    UserTask getUserTaskByID(@Param("id") int id, @Param("username") String username);
}
