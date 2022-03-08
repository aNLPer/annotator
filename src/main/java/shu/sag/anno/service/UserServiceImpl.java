package shu.sag.anno.service;

import com.alibaba.fastjson.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.sag.anno.dao.TaskMapper;
import shu.sag.anno.dao.UserMapper;
import shu.sag.anno.dao.UserTaskMapper;
import shu.sag.anno.pojo.*;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserTaskMapper userTaskMapper;


    @Override
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    @Override
    public Anno getAnno(int userTaskID, int currentIndex, int taskID){
        //获取Task对象
        Task task = taskMapper.getTaskByID(taskID);
        String datasetTableName = task.getDatasetTableName();

        Anno anno = new Anno();
        //设置任务id
        anno.setTaskID(taskID);
        //设置用户任务id
        anno.setUserTaskID(userTaskID);
        //当前的标注索引,
        anno.setCurrentAnnoIndex(currentIndex);
        //设置标注数据集表，
        anno.setDatasetTableName(datasetTableName);
        //设置结果数据集表，
        anno.setResultTableName(task.getResultTableName());
        //当前标注内容
        Map<String, Object> data = userMapper.getCurrentAnnoData(datasetTableName,currentIndex);
        anno.setCurrentText((String) data.get("text"));
        // 当前内容id
        Number num = (Number) data.get("id");
        anno.setTextID(num.intValue());
        return anno;
    }

    @Override
    public Anno startAnno(int userTaskID, int currentIndex, int taskID) {
        return this.getAnno(userTaskID, currentIndex, taskID);
    }

    @Override
    public int addAnnoResult(AnnoResult annoResult, int userTaskID, int currentAnnoIndex) {
        //修改userTask当前标注index
        userTaskMapper.updateUserTaskCurrentAnnoIndex(userTaskID, currentAnnoIndex);
        return userMapper.addAnnoResult(annoResult);
    }

    @Override
    public List<UserTask> getUserTaskByUserAccount(String username, int currentIndex, int pageSize) {
        // 查询返回结果数据
        List<UserTask> taskList = userTaskMapper.getUserTaskByUserAccount(username,currentIndex, pageSize);
        for(UserTask ut: taskList){
            //获取标注任务样本总数
            int totalNum = taskMapper.getTotalNum(ut.getTaskID());
            ut.setAllAnnoNumber(totalNum);
        }
        return taskList;
    }

    @Override
    public int getUserTaskNum(String username){
        return userTaskMapper.getUserTaskNum(username);
    }
}
