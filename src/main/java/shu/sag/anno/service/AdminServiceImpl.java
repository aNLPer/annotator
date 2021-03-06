package shu.sag.anno.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shu.sag.anno.dao.*;
import shu.sag.anno.pojo.*;
import shu.sag.anno.utils.JSONUtils;
import shu.sag.anno.utils.CompressFileUtils;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserTaskMapper userTaskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public int addConfig(Config config) {
        // 验证config对象字段
        if(!JSONUtils.isJson(config.getConfig())){
            // 配置项内容不合法
            return 2;
        }
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
        // 检查该配置项是否存在
        Config cfg = configMapper.getConfigByID(config.getId());
        if(cfg == null){
            // 配置项不存在
            return 3;
        }
        //  验证配置项的合法性
        if(!JSONUtils.isJson(config.getConfig())){
            // 配置项内容不合法
            return 2;
        } else{
            int status = configMapper.updateConfig(config);
            if(status==1){
                // 更新成功
                return 0;
            }else{
                // 更新失败
                return 1;
            }
        }
    }

    @Override
    public int deleteConfig(int id) {
        // config是否存在
        Config cfg = configMapper.getConfigByID(id);
        if (cfg == null){
            return 2;
        }
        int status = configMapper.deleteConfig(id);
        if(status==1){
            // 删除成功
            return 0;
        }else{
            // 删除失败
            return 1;
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
        // config是否合法
        if(!JSONUtils.isJson(task.getConfig())){
            //配置项不合法!
            return 2;
        }
        // dataset是否存在？
        if (datasetMapper.getDatasetByID(task.getDatasetID()) == null){
            // 数据集不存在
            return 3;
        }
        // taskScope合法性检测
        if(!(task.getTaskScope().equals("1")||task.getTaskScope().equals("0"))){
            //任务属性（私有/公开）不合法!
            return 4;
        }
        // startAnnoIndex 和 endAnnoIndex 是否合法
        if(!(task.getStartAnnoIndex() < task.getEndAnnoIndex())){
            //标注索引不合法!
            return 5;
        }
        //生成标注结果数据集表名字
        String resultTableName = task.getDatasetID()+"_"+UUID.randomUUID().toString().replace('-','_');
        taskMapper.createResultTable(resultTableName);
        task.setResultTableName(resultTableName);
        //添加任务
        int addstatus = taskMapper.addTask(task);
        if(addstatus == 1){// 添加任务成功
            return 0;
        }else{// 添加任务失败
            return 1;
        }
    }

    @Override
    public int updateTask(Task task) {
        //判断该任务是否被装配若没被装配则可以修改
        Task searchedTask = taskMapper.getTaskByID(task.getId());
        if(searchedTask.getTaskStatus().equals("0")){// task没有被分配
            // config是否合法
            if(!JSONUtils.isJson(task.getConfig())){
                // 配置不合法
                return 3;
            }
            // taskScope合法性检测
            if(!(task.getTaskScope().equals("1")||task.getTaskScope().equals("0"))){
                //任务属性（私有/公开）不合法!
                return 4;
            }
            // startAnnoIndex 和 endAnnoIndex 是否合法
            if(!(task.getStartAnnoIndex() < task.getEndAnnoIndex())){
                // 起始标注位置不合法
                return 5;
            }
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
        Task searchedTask = taskMapper.getTaskByID(id);
        if (searchedTask==null){
            // 该任务不存在
            return 3;
        }
        if(searchedTask.getTaskStatus().equals("0")){// 若未分配可以删除，
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
        // 获取任务
        Task task = taskMapper.getTaskByID(userTask.getTaskID());

        // 获取用户
        User user = userMapper.getUserByUsername(userTask.getUsername());

        if(task==null || user==null){
            // 用户或者任务不存在
            return 2;
        }
        // userTask 标注索引是否合法(在任务规定的范围内)
        int task_startAnnoIndex = task.getStartAnnoIndex();
        int task_endAnnoIndex = task.getEndAnnoIndex();
        int userTask_startAnnoIndex = userTask.getStartAnnoIndex();
        int userTask_currentAnnoIndex = userTask.getCurrentAnnoIndex();
        int userTask_endAnnoIndex = userTask.getEndAnnoIndex();

        if(userTask_startAnnoIndex <= userTask_currentAnnoIndex &
                userTask_currentAnnoIndex <= userTask_endAnnoIndex &
        userTask_startAnnoIndex >= task_startAnnoIndex &
        userTask_endAnnoIndex <= task_endAnnoIndex){
            int addRes =  userTaskMapper.addUserTask(userTask);
            int taskID = task.getId();
            String status = "1";
            int setRes = taskMapper.setTaskStatus(taskID,status);
            if(addRes>=1 & addRes>=1){
                return 1;
            }else{
                return 0;
            }
        }else{
            // 标注范围不合法
            return 3;
        }

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
    public List<Config> searchConfig(int currentIndex, int pageSize, String creator, String searchValue) {
        return configMapper.searchConfig(currentIndex, pageSize, creator, searchValue);
    }

    @Override
    public int searchConfigResCount(String creator, String searchValue) {
        return configMapper.searchConfigResCount(creator, searchValue);
    }

    @Override
    public List<Dataset> searchDataset(int currentIndex, int pageSize, String creator, String searchVaule) {
        return datasetMapper.searchDataset(currentIndex, pageSize, creator, searchVaule);
    }

    @Override
    public int searchDatasetResCount(String creator, String searchValue) {
        return datasetMapper.searchDatasetResCount(creator, searchValue);
    }

    @Override
    public List<Task> searchTask(int currentIndex, int pageSize, String creator, String searchValue) {
        return taskMapper.searchTask(currentIndex, pageSize, creator, searchValue);
    }

    @Override
    public int searchTaskResCount(String creator, String searchValue) {
        return taskMapper.searchTaskResCount(creator, searchValue);
    }

    @Override
    public List<UserTask> searchUserTask(int currentIndex, int pageSize, String creator, String username, String taskName) {
        return userTaskMapper.searchUserTask(currentIndex, pageSize, creator, username, taskName);
    }

    @Override
    public int searchUserTaskResCount(String creator, String username, String taskName) {
        return userTaskMapper.searchUserTaskResCount(creator, username, taskName);
    }

    @Override
    public int setTaskScope(int taskid, String scope) {
        return taskMapper.setScopeByTaskID(taskid, scope);
    }


    @Override
    public int fileUpload(MultipartFile file,
                          HttpSession session,
                          String remark,
                          String username,
                          String datatype) throws IllegalStateException, IOException{
        // 创建数据集表
        String datasetTableName = "dataset_"+UUID.randomUUID().toString().replace('-','_');
        datasetMapper.createDatasetTable(datasetTableName);
        if(datatype.equals("文本")){// 上传文本数据
            // 添加数据
            InputStream inputStream = file.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            String line = null;
            // 记录数据总数
            int count = 0;
            List<String> items = new ArrayList<>();
            try(BufferedReader br = new BufferedReader(reader)){
                while((line = br.readLine())!= null){
                    // 若该行不符合json格式则,跳过
                    if(!JSONUtils.isJson(line)){
                        continue;
                    }
                    JSONObject json = JSON.parseObject(line);
                    String item = json.getString("text");
                    // 若字段为空则跳过
                    if(item == null){
                        continue;
                    }
                    items.add(item);
                    if(items.size()==2000){
                        // 每两千条批量添加数据一次
                        datasetMapper.addItemBatch(datasetTableName, items);
                        count = count + items.size();
                        // 清空集合
                        items.clear();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            if(items.size()>0){
                datasetMapper.addItemBatch(datasetTableName, items);
                count = count + items.size();
            }
            // 记录该数据集表信息
            Dataset dataset = new Dataset();
            dataset.setName(datasetTableName);
            dataset.setRemark(remark);
            dataset.setCreator(username);
            dataset.setSampleNums(count);
            dataset.setDatatype(datatype);
            datasetMapper.addDataset(dataset);
            return 0;
        }else{// 数据类型为图片
            //获取存储文件的目录
//            String path = session.getServletContext().getRealPath("/upload");
            String path = "D:\\anno\\upload";
            // 上传文件名
            String fileName = file.getOriginalFilename();
            // 文件扩展名
            int pos = fileName.lastIndexOf(".");
            String extName = fileName.substring(pos+1).toLowerCase();

            // 时间加后缀名保存
            String saveName = UUID.randomUUID().toString().replace('-','_')+ "."+extName;
            //文件名
            String saveFileName = saveName.substring(0, saveName.lastIndexOf("."));
            // 根据服务器的文件保存地址和原文件名创建目录文件全路径
            File pushFile = new File(path  + "/" +saveFileName+"/"+ saveName);

            File descFile = new File(path+"/"+saveFileName);
            if (!descFile.exists()) {
                descFile.mkdirs();
            }
            //解压目的文件
            String descDir = path +"/"+saveFileName+"/";

            file.transferTo(pushFile);
            //开始解压zip
            if (extName.equals("zip")) {
                String unZipPath = CompressFileUtils.unZipFiles(pushFile, descDir);
                File dir = new File(unZipPath);
                File[] files = dir.listFiles();// 该文件目录下文件全部放入数组
                if (files != null) {
                    ArrayList<String> items = new ArrayList<>();
                    int count = 0;
                    for (int i = 0; i < files.length; i++) {
                        String fileName_1 = files[i].getName();
                        String filePath = "/img"+unZipPath.replace("D:\\anno\\upload","")+"/"+fileName_1;
                        items.add(filePath);
                        if(items.size()==2000){
                            // 每两千条批量添加数据一次
                            datasetMapper.addItemBatch(datasetTableName, items);
                            count = count + items.size();
                            // 清空集合
                            items.clear();
                        }
                    }
                    if(items.size()>0){
                        datasetMapper.addItemBatch(datasetTableName, items);
                        count = count + items.size();
                    }
                    // 记录该数据集表信息
                    Dataset dataset = new Dataset();
                    dataset.setName(datasetTableName);
                    dataset.setRemark(remark);
                    dataset.setCreator(username);
                    dataset.setSampleNums(count);
                    dataset.setDatatype(datatype);
                    datasetMapper.addDataset(dataset);
                    return 0;
                }
            }else if (extName.equals("rar")) {
                //开始解压rar
                CompressFileUtils.unRarFile(pushFile.getAbsolutePath(), descDir);
            }

            System.out.print(path);
            return 0;
        }


    }

    @Override
    public int deleteDataset(int id) {
        // 获取dataset
        Dataset ds = datasetMapper.getDatasetByID(id);
        // 判断数据集有没有被分配
        int datasetid = ds.getId();
        int tasknums = taskMapper.getTaskByDatasetID(datasetid);
        if(tasknums>0){
            return 2;
        }
        if(ds != null){
            // 删除数据表
            datasetMapper.dropDataset(ds.getName());
            // 删除表信息
            datasetMapper.deleteDatasetInfo(id);
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public List<Application> seachApplication(int currentIndex, int pageSize, String creator, String username, String applystatus) {
        return applicationMapper.seachApplication(currentIndex, pageSize,creator, username, applystatus);
    }

    @Override
    public int countSeachedApplication(String creator, String username, String applystatus) {
        return applicationMapper.countSeachedApplication(username, creator, applystatus);
    }

    // 申请状态设置为成功
    @Override
    public int setApplyStatus(int id,
                              String applystatus,
                              int startAnnoIndex,
                              int endAnnoIndex,
                              String creator) {
        // 判断用户是否存在该申请
        Application app = applicationMapper.getApplicationByID(id);
        if(app != null){// 用户存在该申请
            // 标注索引合法性判断
            Task task = taskMapper.getTaskByID(app.getTaskid());
            if(startAnnoIndex >= endAnnoIndex ||
                    startAnnoIndex < task.getStartAnnoIndex() ||
                    endAnnoIndex > task.getEndAnnoIndex()){
                // 标注索引不合法
                return 3;
            }
            // 设置申请状态
            int setRes = applicationMapper.setApplyStatus(id, applystatus);
            // 为用户添加任务
            UserTask ut = new UserTask();
            ut.setUsername(app.getUsername());
            ut.setTaskID(app.getTaskid());
            ut.setTaskName(task.getTaskName());
            ut.setCurrentAnnoIndex(startAnnoIndex);
            ut.setStartAnnoIndex(startAnnoIndex);
            ut.setEndAnnoIndex(endAnnoIndex);
            ut.setCreator(creator);
            int addRes = userTaskMapper.addUserTask(ut);
            // 设置任务状态为已分配
            int taskID = app.getTaskid();
            String status = "1";
            taskMapper.setTaskStatus(taskID,status);
            if(addRes>0 && setRes>0){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 2;
        }
    }

    // 申请状态设置为失败
    @Override
    public int setApplyStatus(int id, String applystatus) {
        // 判断用户是否存在该申请
        Application app = applicationMapper.getApplicationByID(id);
        if(app != null){
            // 用户存在该申请，则设置申请状态
            return applicationMapper.setApplyStatus(id, applystatus);
        }else{
            return 2;
        }
    }

    @Override
    public int deleteUserTaskByID(int id) {
        // 当所有的被分派任务被删除后，应该修任务状态为未分配
        UserTask userTask = userTaskMapper.getUserTaskByID1(id);
        if(userTask == null){
            // 任务不存在
            return 1;
        }else{
            userTaskMapper.deleteUserTaskByID(id);
            // 获取任务被分派出去的数量
            int taskid = userTask.getTaskID();
            List<UserTask> userTaskList = userTaskMapper.getUserTaskByTaskID(taskid);
            if(userTaskList.size()==0){//任务被分派出去的数量为0,则设置为待分配“0”
                String status = "0";
                taskMapper.setTaskStatus(taskid,status);
            }
            return 0;
        }

    }

    @Override
    public List<UserTask> getAllUserTask(int currentIndex, int pageSize) {
        return userTaskMapper.getAllUserTask(currentIndex, pageSize);
    }

    @Override
    public int getAllUserTaskNum(){
        return userTaskMapper.getAllUserTaskNum();
    }
}
