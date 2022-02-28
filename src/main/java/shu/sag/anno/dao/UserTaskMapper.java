package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.UserTask;
import java.util.List;

public interface UserTaskMapper {
    //管理员为用户分配任务
    int addUserTask(UserTask userTask);
    //管理员删除用户任务
    int deleteUserTaskByID(int id);
    //管理员获取有的用户任务
    List<UserTask> getAllUserTask(int currentIndex, int pageSize);
    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(@Param("userAccount") String userAccount, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    // 修改当前标注index
    int updateUserTaskCurrentAnnoIndex(@Param("userTaskID") int userTaskID, @Param("currentAnnoIndex") int currentAnnoIndex);
}