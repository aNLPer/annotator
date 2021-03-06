package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.User;
import shu.sag.anno.pojo.UserTask;
import java.util.List;

public interface UserTaskMapper {

    // 通过taskID获取UserTask
    List<UserTask> getUserTaskByTaskID(@Param("taskid") int taskid);

    //管理员获取有的用户任务
    List<UserTask> getAllUserTask(int currentIndex, int pageSize);

    // 获取userTask表记录总数
    int getAllUserTaskNum();

    //管理员为用户分配任务
    int addUserTask(UserTask userTask);

    //管理员删除用户任务
    int deleteUserTaskByID(int id);

    // 删除用户相关任务
    int deleteUserTaskByUsername(String username);


    //用户获取自己的任务
    List<UserTask> getUserTaskByUserAccount(@Param("username") String username, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    // 修改当前标注index
    int updateUserTaskCurrentAnnoIndex(@Param("userTaskID") int userTaskID, @Param("currentAnnoIndex") int currentAnnoIndex);
    // 获取用户标注任务总数
    int getUserTaskNum(String username);

    int currentAnnoIndexAdd1(@Param("userTaskID") int userTaskID);

    UserTask getUserTaskByID(@Param("id") int id, @Param("username") String username);

    UserTask getUserTaskByID1(@Param("id") int id);

    // 模糊搜索usertask
    List<UserTask> searchUserTask(@Param("currentIndex") int currentIndex,
                          @Param("pageSize") int pageSize,
                          @Param("creator") String creator,
                          @Param("username") String username,
                          @Param("taskName") String taskName);

    // 模糊搜索结果数TasK目
    int searchUserTaskResCount(@Param("creator") String creator,
                               @Param("username") String username,
                               @Param("taskName")String taskName);



}
