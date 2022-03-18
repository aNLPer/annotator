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
    public int getConfigNum(){
        return configMapper.getConfigNum();
    }

    @Override
    public List<Task> getTaskByConfigID(int configID) {
        return taskMapper.getTaskByConfigID(configID);
    }


    @Override
    public int updateConfig(Config config) {
        //判断该配置是否被装配若没被装配则可以修改
        List<Task> taskList = taskMapper.getTaskByConfigID(config.getId());
        if(taskList.isEmpty()){
            int status = configMapper.updateConfig(config);
            if(status==1){
                return 0;
            }else{
                return 1;
            }
        }else{
           return 2;

        }
    }

    @Override
    public int deleteConfig(int id) {
        // 判断该配置项是否被分配
        List<Task> taskList = taskMapper.getTaskByConfigID(id);
        // 若未分配可以删除，
        if(taskList.isEmpty()){
            int status = configMapper.deleteConfig(id);
            if(status==1){
                // 删除成功
                return 0;
            }else{
                // 删除失败
                return 1;
            }
        }else{//若已分配不可以删除，
            return 2;

        }
    }

    @Override
    public List<Task> getAllTask(int currentIndex, int pageSize) {
        return taskMapper.getAllTask(currentIndex, pageSize);
    }

    @Override
    public int getTaskNum(){
        return taskMapper.getTaskNum();
    }

    @Override
    public int addTask(Task task) {
        //标注结果表名称=数据集表名+任务名+配置id
        String resultTableName = task.getDatasetTableName()+"_"+task.getConfigID()+"_"+"result";
        int status = taskMapper.createResultTable(resultTableName);
        if(status==1){// 创建结果集数据表成功
            task.setResultTableName(resultTableName);
            //添加任务
            int addstatus = taskMapper.addTask(task);
            if(addstatus == 1){// 添加任务成功
                return 0;
            }else{// 添加任务失败
                return 1;
            }
        }else{// 标注结果集数据表创建失败；
            return 2;
        }
    }

    @Override
    public int updateTask(Task task) {
        //判断该任务是否被装配若没被装配则可以修改
        List<UserTask> userTaskList = userTaskMapper.getUserTaskByTaskID(task.getId());
        if(userTaskList.isEmpty()){// task没有被分配
            int status = taskMapper.updateTask(task);
            if(status==1){// 更新成功
                return 0;
            }else{// 更新失败
                return 1;
            }
        }else{// task已经被分配
            return 2;

        }
    }

    @Override
    public int deleteTaskByID(int id) {
        // 判断该任务是否被分配
        List<UserTask> userTaskList = userTaskMapper.getUserTaskByTaskID(id);
        // 若未分配可以删除，
        if(userTaskList.isEmpty()){
            int status = taskMapper.deleteTaskByID(id);
            if(status==1){
                // 删除成功
                return 0;
            }else{
                // 删除失败
                return 1;
            }
        }else{//若已分配不可以删除，
            return 2;

        }
    }








    @Override
    public int addUserTask(UserTask userTask) {
        return userTaskMapper.addUserTask(userTask);
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
    public Task getTaskByID(int id) {
        return taskMapper.getTaskByID(id);
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
