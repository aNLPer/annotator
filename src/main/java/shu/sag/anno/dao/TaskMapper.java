package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Task;

import java.util.List;

public interface TaskMapper {
    // 增加任务
    int addTask(Task task);

    // 通过configid获取任务
    List<Task> getTaskByConfigID(int ConfigID);

    // 获取所有的任务
    List<Task> getAllTask(int currentIndex, int pageSize);

    // 获取task的总数
    int getTaskNum();

    // 更新任务
    int updateTask(Task task);




    //创建结果集表
    int createResultTable(@Param("resultTableName")String resultTableName);

    // 通过id删除任务
    int deleteTaskByID(int id);
    //通过id获取task对象
    Task getTaskByID(int id);


    //更新任务状态
    int updateTaskStatus(int taskID, int status);

    int getTotalNum(int id);
}
