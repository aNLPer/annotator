package shu.sag.anno.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;
import shu.sag.anno.pojo.*;

import java.io.IOException;
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

    // 按名字模糊搜索config
    List<Config> searchConfig(int currentIndex, int pageSize, String searchValue);

    // 按照条件搜索结果的总数
    int searchConfigResCount(String searchValue);

    // 按名字模糊搜索dataset
    List<Dataset> searchDataset(int currentIndex, int pageSize, String searchValue);

    // 按照条件搜索结果的总数
    int searchDatasetResCount(String searchValue);

    // 按名字模糊搜索task
    List<Task> searchTask(int currentIndex, int pageSize, String searchValue);

    // 按照条件搜索结果的总数
    int searchTaskResCount(String searchValue);

    // 按名字模糊搜索usertask
    List<UserTask> searchUserTask(int currentIndex,
                                  int pageSize,
                                  String username,
                                  String taskName);

    // 按照条件搜索结果的总数
    int searchUserTaskResCount(String username,
                               String taskName);

    // 设置任务私用/公开属性
    int setTaskScope(int taskid, String scope);

    // 文件上传
    int fileUpload(MultipartFile file,String remark, String username)  throws IllegalStateException, IOException;

    // 删除数据库
    int deleteDataset(int id);

    // 获取用户申请列表
    List<Application> seachApplication(int currentIndex,
                                       int pageSize,
                                       String username,
                                       String applystatus);

    // 用户申请计数
    int countSeachedApplication(String username,
                                String applystatus);

    // 设置用户申请状态
    int setApplyStatus(int id, String applystatus);

    int setApplyStatus(int id,
                       String applystatus,
                       int startAnnoIndex,
                       int endAnnoIndex,
                       String username);
}
