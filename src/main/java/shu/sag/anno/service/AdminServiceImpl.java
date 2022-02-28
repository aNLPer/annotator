package shu.sag.anno.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.sag.anno.dao.ConfigMapper;
import shu.sag.anno.dao.TaskMapper;
import shu.sag.anno.dao.UserTaskMapper;
import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Task;
import shu.sag.anno.pojo.UserTask;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserTaskMapper userTaskMapper;

    @Override
    public int addConfig(Config config) {
        return configMapper.addConfig(config);
    }

    @Override
    public int addTask(Task task) {
        //标注结果表名称=数据集表名+任务名+配置id
        String resultTableName = task.getDatasetTableName()+"_"+task.getConfigID()+"_"+"result";
        int status = taskMapper.createResultTable(resultTableName);
        //创建结果表
        task.setResultTableName(resultTableName);
        //添加任务
        return taskMapper.addTask(task);
    }

    @Override
    public int addUserTask(UserTask userTask) {
        return userTaskMapper.addUserTask(userTask);
    }




    @Override
    public int deleteConfig(int id) {
        return configMapper.deleteConfig(id);
    }

    @Override
    public Config getConfigByID(int id) {
        return configMapper.getConfigByID(id);
    }

    @Override
    public Config getConfigByType(String type) {
        return configMapper.getConfigByType(type);
    }

    @Override
    public List<Config> getAllConfig(int currentIndex, int pageSize) {
        return configMapper.getAllConfig(currentIndex,pageSize);
    }

    @Override
    public int updateConfig(Config config) {
        //判断该配置是否被装配若没被装配则可以修改
        List<Task> taskList = taskMapper.getTaskByConfigID(config.getId());
        if(taskList.isEmpty()){
            return configMapper.updateConfig(config);
        }else{
            //若是被装配，判断任务是否被分配
            boolean flag = false;
            for(Task task: taskList){
                if(task.getTaskStatus()>0){
                    //任务被分配
                    flag = true;
                }
            }
            //若是被分配则不能修改否则可以修改
            if(flag){
                return 0;
            }else{
                return configMapper.updateConfig(config);
            }
        }
    }





    @Override
    public int deleteTaskByID(int id) {
        return taskMapper.deleteTaskByID(id);
    }

    @Override
    public Task getTaskByID(int id) {
        return taskMapper.getTaskByID(id);
    }

    @Override
    public List<Task> getTaskByConfigID(int configID) {
        return taskMapper.getTaskByConfigID(configID);
    }

    @Override
    public List<Task> getAllTask(int currentIndex, int pageSize) {
        return taskMapper.getAllTask(currentIndex, pageSize);
    }

    @Override
    public int updateTask(Task task) {
        return taskMapper.updateTask(task);
    }




    @Override
    public int deleteUserTaskByID(int id) {
        return userTaskMapper.deleteUserTaskByID(id);
    }

    @Override
    public List<UserTask> getAllUserTask(int currentIndex, int pageSize) {
        return userTaskMapper.getAllUserTask(currentIndex, pageSize);
    }


}
