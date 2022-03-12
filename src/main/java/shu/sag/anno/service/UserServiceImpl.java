package shu.sag.anno.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shu.sag.anno.dao.*;
import shu.sag.anno.pojo.*;
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
        String text = datasetMapper.getTextFieldFromDataset(datasetTableName,id,fieldName);
        anno.setText(text);
        //标注结果表
        anno.setResultTableName(task.getResultTableName());
        //原始未标注数据库表名
        anno.setRawTableName(task.getDatasetTableName());
        // 获取标注任务配置项
        id = task.getConfigID();
        Config cfg = configMapper.getConfigByID(id);
        anno.setConfig(cfg.getConfig());
        //返回
        return anno;
    }

    @Override
    public boolean addAnnoResult(int userTaskID,
                             String resultTableName,
                             String username,
                             int textID,
                             String text,
                             String label,
                             String rawTableName){
        // 插入标注将结果
        int result1 = datasetMapper.addAnnoResult(resultTableName,username,textID,text,label,rawTableName);
        // 修改task表的taskstatus字段
        //taskMapper.updateTaskStatus(taskID, 2);
        // 修改userTask当前标注index
        int result2 = userTaskMapper.currentAnnoIndexAdd1(userTaskID);
        return true;
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
    public Task getTaskByID(int id){
        return taskMapper.getTaskByID(id);
    }

    @Override
    public String getTextByID(String datasetTableName, int id){
        System.out.println(datasetTableName+"__"+id);
        String fieldName = "text";
        return datasetMapper.getTextFieldFromDataset(datasetTableName, id, fieldName);
    }
}
