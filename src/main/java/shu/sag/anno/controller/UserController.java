package shu.sag.anno.controller;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shu.sag.anno.pojo.*;
import shu.sag.anno.service.UserService;
import java.util.List;
import shu.sag.anno.utils.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登录
    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password){
        JSONObject state = new JSONObject();
        state.put("data",new JSONObject());
        User user = userService.login(username, password);
        if(user==null){
            state.put("code",1);
            state.put("message","用户不存在或者密码错误！");
            return state;
        }else{
            //登录成功返回token
            state.put("code",0);
            state.put("message","登录成功！");
            state.getJSONObject("data").put("token", TokenUtil.sign(user.getUsername(),user.getPassword(),user.getRole()));
            return state;
        }
    }

    // 获取用户标注任务列表
    @RequestMapping("user/task/list")
    @ResponseBody
    public Object getMyTask(@RequestHeader("token") String token, int currentIndex, int pageSize){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
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
        }
    }

    // 执行标注任务
    @RequestMapping("user/anno/do")
    @ResponseBody
    public Object doAnno(@RequestHeader("token") String token, int userTaskID, int currentIndex){
        /**/
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            // 获取用户账号
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            //获取userTask对象
            UserTask ut = userService.getUserTaskByID(userTaskID, username);
            //获取Task对象
            Task task = userService.getTaskByID(ut.getTaskID());
            //判断currentIndex
            if(currentIndex==-1){//如果currentIndex为-1表示获取下一条标注数据,该情况下Anno对象label值为空
                currentIndex=ut.getCurrentAnnoIndex();
                if (currentIndex > ut.getEndAnnoIndex()){//如果currentIndex大于标注范围则返回
                    res.put("code","1");
                    res.put("message","该任务已标注完成！");
                    res.put("data",new JSONObject());
                    return res;
                }else{
                    res.put("code","0");
                    res.put("message","success!");
                    res.put("data",new JSONObject());
                    //获取Anno对象
                    Anno anno = userService.getAnno(currentIndex, task);
                    res.getJSONObject("data").put("id",anno.getId());
                    res.getJSONObject("data").put("text",anno.getText());
                    res.getJSONObject("data").put("config",anno.getConfig());
                    res.getJSONObject("data").put("rawTableName",anno.getRawTableName());
                    res.getJSONObject("data").put("resultTaleName",anno.getResultTableName());
                    res.getJSONObject("data").put("label",anno.getLabel());
                    return res;
                }
            }else{//如果currentIndex不为-1，该情况下Anno对象的label的值不为空
                res.put("code","0");
                res.put("message","currentIndex不为-1,返回指定标注数据");
                res.put("data",new JSONObject());
                return res;
            }

        }
    }

    // 提交标注结果
    @RequestMapping("user/anno/submit")
    @ResponseBody
    public Object submitAnnoResult(@RequestHeader("token") String token,
                                   int userTaskID,//标注任务id
                                   int id,// 文本id
                                   String label, //标注结果
                                   String rawTableName, //原始数据表名字
                                   String resultTableName){// 结果数据表名字
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String text = userService.getTextByID(rawTableName, id);
            System.out.println(text);
            userService.addAnnoResult(userTaskID,resultTableName,username,id,text,label,rawTableName);
            res.put("code","0");
            res.put("message","提交成功！");
            res.put("data",new JSONObject());
            return res;
        }
    }


    // 获取用户信息
    @RequestMapping("user/info")
    @ResponseBody
    public Object getInfo(@RequestHeader("token") String token){
        System.out.println(token);
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            res.put("code","0");
            res.put("message","用户信息已获取！");
            res.put("data",new JSONObject());
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            String[] roleArr = {loginUser.getString("role")};
            res.put("username",username);
            res.getJSONObject("data").put("role",roleArr);
            return res;
        }
    }
}


