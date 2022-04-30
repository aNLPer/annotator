package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Dataset;
import shu.sag.anno.pojo.Task;

import java.util.List;

public interface TaskMapper {
    // 增加任务
    int addTask(Task task);

    // 通过configid获取任务
    List<Task> getTaskByConfigID(int ConfigID);

    // 获取所有的任务
    List<Task> getAllTask(@Param("currentIndex") int currentIndex, @Param("pageSize") int pageSize);

    // 获取task的总数
    int getTaskNum();

    // 更新任务
    int updateTask(Task task);

    // 获取任务的配置项
    String getConfigByTaskID(int taskid);


    //创建结果集表
    int createResultTable(@Param("resultTableName")String resultTableName);

    // 通过id删除任务
    int deleteTaskByID(int id);

    //通过id获取task对象
    Task getTaskByID(@Param("taskid") int taskid);

    //更新任务状态
    int setTaskStatus(@Param("taskID") int taskID, @Param("status")String status);

    int getTotalNum(int id);

    List<Task> searchTask(@Param("currentIndex") int currentIndex, @Param("pageSize")int pageSize, @Param("searchValue")String searchValue);

    int searchTaskResCount(String searchValue);

    // 搜索公开任务
    public List<Task> searchPubTask(@Param("currentIndex") int currentIndex,
                                    @Param("pageSize") int pageSize,
                                    @Param("searchValue") String searchValue);

    // 搜索到的公开任务总数
    public int searchPubTaskResCount(@Param("searchValue") String searchValue);

    public int setScopeByTaskID(@Param("taskid") int taskid, @Param("scope") String scope);

    // 通过数据集id获取任务
    public int getTaskByDatasetID(@Param("datasetid") int datasetid);

}
