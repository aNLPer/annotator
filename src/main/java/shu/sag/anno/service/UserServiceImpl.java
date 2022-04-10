package shu.sag.anno.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.sag.anno.dao.*;
import shu.sag.anno.pojo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserTaskMapper userTaskMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    @Override
    public Anno getAnno(int currentIndex, Task task){
        /*
        * 获取标注页面需要的数据
        * */
        Anno anno = new Anno();
        // 原始未标注数据库表id
        anno.setId(currentIndex);
        // 原始未标注数据库文本
        String datasetTableName = task.getDatasetTableName();
        int id = currentIndex;
        String fieldName = "text";
        anno.setText(datasetMapper.getTextFieldFromDataset(datasetTableName,id,fieldName));
        //标注结果表
        anno.setResultTableName(task.getResultTableName());
        //原始未标注数据库表名
        anno.setRawTableName(task.getDatasetTableName());
        // 获取标注任务配置项
        anno.setConfig(task.getConfig());
        // 若已经标注过，设置已标注的标签
        anno.setLabel("");
        //返回
        return anno;
    }

    @Override
    public int addAnnoResult(int userTaskID,
                             String resultTableName,
                             String username,
                             int textID,
                             String text,
                             String label,
                             String rawTableName){
        /*
        插入标注结果
        此时应该考虑到用户重复提交的问题：

              （1）验证改用户是否已经提交过条数据的标注，若没有提交过则：插入标注结果，currentIndex应该自增1，
                                                  如果已经提交过则： 更新标注结果，currentIndex保持不变
        * */
        //System.out.println(resultTableName);
        int annoResultCount = datasetMapper.countAnnoResult(resultTableName,username, textID);
        int addRes = 0;
        int autoAddRes = 0;
        int updateRes = 0;
        if(annoResultCount == 0){//没有提交过
            //插入标注结果
            addRes = userMapper.addAnnoResult(resultTableName,username,textID,text,label,rawTableName);
            // 修改userTask当前标注index，
            autoAddRes = userTaskMapper.currentAnnoIndexAdd1(userTaskID);
        }else{// 已经标注过
            updateRes = userMapper.updateAnnoResult(resultTableName,username, textID, text, label);
            // 修改userTask当前标注index
            autoAddRes = userTaskMapper.currentAnnoIndexAdd1(userTaskID);
        }
        if(addRes == 1 &  autoAddRes == 1){// 插入标注结果成功
            //taskMapper.setTaskStatus(userTaskID,"已完成")
            return 0;
        }else{
            if(updateRes==1){
                    return 0;
            }
            return 1;
        }

    }

    @Override
    public List<UserTask> getUserTaskByUserAccount(String username, int currentIndex, int pageSize) {
        // 查询返回结果数据
        List<UserTask> taskList = userTaskMapper.getUserTaskByUserAccount(username,currentIndex, pageSize);
        return taskList;
    }

    @Override
    public int getUserTaskNum(String username){
        return userTaskMapper.getUserTaskNum(username);
    }

    @Override
    public UserTask getUserTaskByID(int id, String username){
        return userTaskMapper.getUserTaskByID(id,username);
    }

    @Override
    public Task getTaskByID(int taskid){
        return taskMapper.getTaskByID(taskid);
    }

    @Override
    public String getTextByID(String datasetTableName, int id){
        System.out.println(datasetTableName+"__"+id);
        String fieldName = "text";
        return datasetMapper.getTextFieldFromDataset(datasetTableName, id, fieldName);
    }

    @Override
    public int UserisExist(String username) {
        return userMapper.UserisExist(username);
    }

    @Override
    public int Regist(User user) {
        return userMapper.Regist(user);
    }

    @Override
    public List<UserTask> searchUserTask(int currentIndex, int pageSize, String username, String searchValue) {
        return null;
    }

    @Override
    public int searchUserTaskResCount(String username, String searchValue) {
        return 0;
    }

    @Override
    public List<Task> searchPubTask(int currentIndex, int pageSize, String searchValue) {
        return taskMapper.searchPubTask(currentIndex, pageSize,searchValue);
    }

    @Override
    public int searchPubTaskResCount(String searchValue) {
        return taskMapper.searchPubTaskResCount(searchValue);
    }

    @Override
    public int addApplication(String username, int taskid) {
        Task task = taskMapper.getTaskByID(taskid);
        if(task == null){ // 任务不存在
            return 2;
        }
        //判断有没有该任务的申请在待审核状态
        Application app = applicationMapper.getApplicationByTaskIDandUsername(username, taskid);
        if(app != null){
            // 有该任务在申请待审核状态则无法申请
            return 3;
        }
        Application AT = new Application();
        // 任务id
        AT.setTaskid(taskid);
        // 申请人
        AT.setUsername(username);
        //申请时间
        LocalDate d = LocalDate.now(); // 当前日期
        //LocalTime t = LocalTime.now(); // 当前时间
        String timestr = d.toString();
        AT.setApplytime(timestr);
        return applicationMapper.addApplication(AT);
    }

    @Override
    public int withdrawApplication(String username, int id) {
        // 用户申请是否存在
        Application at = applicationMapper.getApplicationByID(id);
        if(at.getUsername().equals(username) && at.getApplystatus().equals("1")){// 若存在而且申请状态为待审核为则删除
            return applicationMapper.withdrawApplication(id);
        }else{
            return 2;
        }

    }

    @Override
    public List<Application> getUserApplications(String uername, int currentIndex, int pageSize) {
        return applicationMapper.getApplicationList(uername,currentIndex, pageSize);
    }

    @Override
    public int applicationCount(String username) {
        return applicationMapper.ApplicationCount(username);
    }


}
