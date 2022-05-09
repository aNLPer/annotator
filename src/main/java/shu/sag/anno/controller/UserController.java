package shu.sag.anno.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shu.sag.anno.pojo.*;
import shu.sag.anno.service.UserService;
import java.util.List;
import shu.sag.anno.utils.PwdSecurity;
import shu.sag.anno.utils.JSONUtils;
import shu.sag.anno.utils.NameGen;
import shu.sag.anno.utils.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登录(已调试)
    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password){
        JSONObject state = new JSONObject();
        state.put("data",new JSONObject());
        User user = userService.login(username, PwdSecurity.encode(password.trim()));
        if(user==null){
            state.put("code",1);
            state.put("message","用户不存在或者密码错误！");
            return state;
        }else{
            if (user.getStatus().equals("1")){
                state.put("code", 1);
                state.put("message", "登录失败,账户已禁用");
                return state;
            }else{
                //登录成功返回token
                state.put("code",0);
                state.put("message","登录成功！");
                state.getJSONObject("data").put("token", TokenUtil.sign(user.getUsername(),user.getStatus(),user.getRole()));
                return state;
            }
        }
    }

    // 用户注册(已测试)
    @RequestMapping(value = "user/regist",method = RequestMethod.POST)
    @ResponseBody
    public Object regist(User user){
        JSONObject res = new JSONObject();
        if(user.getUsername()==null){
            res.put("code",1);
            res.put("message","用户账号不能为空");
            return res;
        }
        int userCount = userService.UserisExist(user.getUsername());
        if (userCount>0){
            res.put("code",1);
            res.put("message","该用户已存在");
            return res;
        }else{
            // 用户注册
            if(user.getName().trim().equals("") || user.getName()==null){
                // 如果用户名字为空,随机生成
                user.setName(NameGen.randomName());
            }
            // 用户密码加密
            if(user.getPassword().trim().equals("") || user.getPassword()==null){
                user.setPassword(PwdSecurity.encode("123456"));
            }else{
                user.setPassword(PwdSecurity.encode(user.getPassword().trim()));
            }
            // 用户注册
            int reg = userService.Regist(user);
            if(reg>0){
                res.put("code",0);
                res.put("message","注册成功");
                return res;
            }else{
                res.put("code",1);
                res.put("message","系统错误，注册失败");
                return res;
            }

        }
    }

    // 用户修改密码
    @RequestMapping("user/update/pwd")
    @ResponseBody
    public Object updatePWD(@RequestHeader("token") String token, String oldPwd, String newPwd){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            // 检测旧密码
            String pwd = userService.getUserPWD(username);
            if(pwd.equals(PwdSecurity.encode(oldPwd.trim()))){
                // 修改密码
                int updateState = userService.updateUserPWD(username, PwdSecurity.encode(newPwd.trim()));
                if(updateState>0){
                    res.put("code",0);
                    res.put("message","修改成功！");
                    return res;
                }else{
                    res.put("code",1);
                    res.put("message","修改失败！");
                    return res;
                }
            }else{
                res.put("code",1);
                res.put("message","旧密码错误！");
                return res;
            }
        }
    }

    // 获取用户信息(已调试)
    @RequestMapping("user/info")
    @ResponseBody
    public Object getInfo(@RequestHeader("token") String token, String oldPwd, String newPwd){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            res.put("code",0);
            res.put("message","用户信息已获取！");
            res.put("data",new JSONObject());
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String[] roleArr = {loginUser.getString("role").trim()};
            res.getJSONObject("data").put("username",username);
            res.getJSONObject("data").put("roles",roleArr);
            return res;
        }
    }

    // 用户退出登录
    @RequestMapping("user/logout")
    @ResponseBody
    public Object logout(){
        JSONObject res = new JSONObject();
        res.put("code",0);
        res.put("message","已退出登录！");
        return res;
    }

    // 获取用户标注任务列表（）
    @RequestMapping("user/task/list")
    @ResponseBody
    public Object getMyTask(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchValue){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null || username==null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") && status.trim().equals("0")){// 权限检查通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                //获取任务列表
                List<UserTask> taskList = userService.getUserTaskByUserAccount(username,currentIndex,pageSize);
                // 获取用户所有的任务总数
                int taskTotal = userService.getUserTaskNum(username);
                res.put("code",0);
                if (taskTotal==0){
                    res.put("message","暂无标注任务");
                }else{
                    res.put("message","查找到"+taskTotal+"条标注任务");
                }
                res.put("data",new JSONObject());
                res.getJSONObject("data").put("taskList",taskList);
                res.getJSONObject("data").put("taskTotal",taskTotal);
                return res;
            }else{
                res.put("code", 1);
                res.put("message", "权限错误，拒绝访问！");
                return res;
            }

        }
    }

    // 执行标注任务/获取标注任务信息（已调试）
    @RequestMapping("user/anno/do")
    @ResponseBody
    public Object doAnno(@RequestHeader("token") String token, int userTaskID, int currentIndex){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 用户权限验证
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String status = loginUser.getString("status");
            String role = loginUser.getString("role");
            if(loginUser==null || status==null || role==null){
                res.put("code",1);
                res.put("message","获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.trim().equals("0")){// 验证权限通过
                //获取userTask对象
                UserTask ut = userService.getUserTaskByID(userTaskID, username);
                if(ut==null){
                    res.put("code",1);
                    res.put("message","获取任务信息出错!");
                    return res;
                }
                //获取Task对象
                Task task = userService.getTaskByID(ut.getTaskID());
                //判断currentIndex
                if(currentIndex==-1){//如果currentIndex为-1表示获取下一条标注数据,该情况下Anno对象label值为空
                    currentIndex=ut.getCurrentAnnoIndex();
                    if (currentIndex > ut.getEndAnnoIndex()){//如果currentIndex大于标注范围则返回
                        res.put("code",0);
                        res.put("message","标注索引越界！");
                        return res;
                    }else{
                        res.put("code",0);
                        res.put("message","获取成功!");
                        res.put("data",new JSONObject());
                        //获取Anno对象
                        Anno anno = userService.getAnno(currentIndex, task);
                        res.getJSONObject("data").put("id",anno.getId());
                        res.getJSONObject("data").put("text",anno.getText());
                        res.getJSONObject("data").put("config",anno.getConfig());
                        res.getJSONObject("data").put("rawTableName",anno.getRawTableName());
                        res.getJSONObject("data").put("resultTableName",anno.getResultTableName());
                        res.getJSONObject("data").put("label",anno.getLabel());
                        return res;
                    }
                }else{//如果currentIndex不为-1，该情况下Anno对象的label的值不为空
                    res.put("code",0);
                    res.put("message","currentIndex不为-1,返回指定标注数据");
                    res.put("data",new JSONObject());
                    return res;
                }
            }else{
                res.put("code",1);
                res.put("message","权限错误，拒绝访问！");
                return res;
            }
        }
    }

    // 提交标注结果（）
    @RequestMapping(value="user/anno/submit", method = RequestMethod.POST)
    @ResponseBody
    public Object submitAnnoResult(@RequestHeader("token") String token,
                                   int userTaskID,
                                   int id,
                                   String label,
                                   String rawTableName,
                                   String resultTableName){
        /*
        @RequestHeader("token") String token,
        int userTaskID 标注任务id
        int id        文本id
        String label  标注结果
        String rawTableName  原始数据表名字
        String resultTableName  结果数据表名字
        * */
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || username==null || role==null || status==null){
                res.put("code",1);
                res.put("message","获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.trim().equals("0")){// 权限验证通过
                String text = userService.getTextByID(rawTableName, id);
                // label是否为json格式
                if(!JSONUtils.isJson(label)){
                    res.put("code",1);
                    res.put("message","标注结果不合法！");
                    return res;
                }
                int  addRes = userService.addAnnoResult(userTaskID,resultTableName,username,id,text,label,rawTableName);
                if(addRes == 0){
                    res.put("code",0);
                    res.put("message","提交成功！");
                    return res;
                }else{
                    res.put("code",1);
                    res.put("message","系统错误，提交失败！");
                    return res;
                }
            }else{
                res.put("code",1);
                res.put("message","获取登录信息失效，请重新登录！");
                return res;
            }

        }
    }



    // 获取公开任务列表 user/publicTask/list
    @RequestMapping("user/publicTask/list")
    @ResponseBody
    public Object getPubTaskList(@RequestHeader("token") String token,int currentIndex, int pageSize, String searchValue){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null || username==null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.equals("0")){// 权限检查通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                //获取任务列表
                List<Task> pubTaskList = userService.searchPubTask(currentIndex, pageSize, searchValue);
                // 获取用户所有的任务总数
                int pubTaskTotal = userService.searchPubTaskResCount(searchValue);
                res.put("code",0);
                if (pubTaskTotal==0){
                    res.put("message","暂无公开任务");
                }else{
                    res.put("message","查找到"+pubTaskTotal+"条标注任务");
                }
                res.put("data",new JSONObject());
                res.getJSONObject("data").put("pubTaskList",pubTaskList);
                res.getJSONObject("data").put("pubTaskTotal",pubTaskTotal);
                return res;
            }else{
                res.put("code", 1);
                res.put("message", "权限错误，拒绝访问！");
                return res;
            }

        }
    }

    //获取我的任务申请列表 user/appliedTask/list
    @RequestMapping("user/appliedTask/list")
    @ResponseBody
    public Object getUserAppliedTaskList(@RequestHeader("token") String token,int currentIndex, int pageSize){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null || username==null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.equals("0")){// 权限检查通过
                List<Application> applications = userService.getUserApplications(username,currentIndex, pageSize);
                for(Application app: applications){
                    int taskid = app.getTaskid();
                    Task task = userService.getTaskByID(taskid);
                    if(task == null){
                        app.setTaskName("该任务已删除");
                        continue;
                    }
                    app.setTaskName(task.getTaskName());
                }
                int applicationCount = userService.applicationCount(username);
                res.put("code",0);
                if (applicationCount==0){
                    res.put("message","");
                }else{
                    res.put("message","查找到"+applicationCount+"条申请记录");
                }
                res.put("data",new JSONObject());
                res.getJSONObject("data").put("applicationList",applications);
                res.getJSONObject("data").put("applicationTotal",applicationCount);
                return res;

            }else{
                res.put("code", 1);
                res.put("message", "权限错误，拒绝访问！");
                return res;
            }

        }
    }

    // 申请公开任务 user/publicTask/apply
    @RequestMapping("user/publicTask/apply")
    @ResponseBody
    public Object applyTask(@RequestHeader("token") String token, int taskid){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null || username==null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.trim().equals("0")){// 权限检查通过
                int addRes = userService.addApplication(username, taskid);
                if(addRes == 1){
                    res.put("code",0);
                    res.put("message","申请已提交！");
                    return res;
                }else {
                    if(addRes == 0){
                        res.put("code",1);
                        res.put("message","系统错误提交失败！");
                        return res;
                    }else{
                        if(addRes==2){
                            res.put("code",1);
                            res.put("message","任务不存在！");
                            return res;
                        }
                        res.put("code",1);
                        res.put("message","申请待审核中，不能重复申请！");
                        return res;
                    }
                }
            }else{
                res.put("code", 1);
                res.put("message", "权限错误，拒绝访问！");
                return res;
            }

        }
    }

    // 撤回公开任务申请 user/appliedTask/withdraw
    @RequestMapping("user/appliedTask/withdraw")
    @ResponseBody
    public Object withdrawAppliedTask(@RequestHeader("token") String token, int id){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code",1);
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null || username==null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if(role.trim().equals("2") & status.equals("0")){// 权限检查通过
                int wdRes = userService.withdrawApplication(username,id);
                if(wdRes == 1){
                    res.put("code",0);
                    res.put("message","申请已撤回！");
                    return res;
                }else {
                    if(wdRes == 0){
                        res.put("code",1);
                        res.put("message","系统错误撤回失败！");
                        return res;
                    }else{
                        res.put("code",1);
                        res.put("message","用户申请不存在或已审核，无法撤回！");
                        return res;
                    }
                }
            }else{
                res.put("code", 1);
                res.put("message", "权限错误，拒绝访问！");
                return res;
            }

        }
    }

}


