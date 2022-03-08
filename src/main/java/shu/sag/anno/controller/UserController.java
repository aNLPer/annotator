package shu.sag.anno.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import shu.sag.anno.pojo.Anno;
import shu.sag.anno.pojo.AnnoResult;
import shu.sag.anno.pojo.User;
import shu.sag.anno.pojo.UserTask;
import shu.sag.anno.service.UserService;
import javax.servlet.http.HttpSession;
import java.util.List;
import shu.sag.anno.utils.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // 登录
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
            state.getJSONObject("data").put("token", TokenUtil.sign(user.getUsername(),user.getPassword()));
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
            res.put("code","450");
            res.put("message","登录信息失效，请重新登录！");
            res.put("data",new JSONObject());
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String username = loginUser.getString("username");
            //获取任务列表
            List<UserTask> myTaskList = userService.getUserTaskByUserAccount(username,currentIndex,pageSize);
            int taskTotal = userService.getUserTaskNum(username);
            res.put("code",0);
            if (taskTotal==0){
                res.put("message","暂无标注任务");
            }else{
                res.put("message","查找到"+taskTotal+"条标注任务");
            }
            res.put("data",new JSONObject());
            res.getJSONObject("data").put("taskList",myTaskList);
            res.getJSONObject("data").put("taskTotal",taskTotal);
            return res;
        }
    }

    // 执行标注任务
    @RequestMapping("user/anno/do")
    @ResponseBody
    public ModelAndView doAnno(int userTaskID, int currentIndex, int taskID){
        /**/

        ModelAndView mav = new ModelAndView("anno");
        Anno anno = userService.startAnno(userTaskID, currentIndex, taskID);
        mav.addObject("anno", anno);
        return mav;
    }

    // 提交标注结果
    @RequestMapping("user/anno/submit")
    public ModelAndView submitAnnoResult(AnnoResult annoResult, @Param("userTaskID") int userTaskID,
                                         @Param("currentAnnoIndex")int currentAnnoIndex,
                                         @Param("taskID")int taskID){
        System.out.println(annoResult.toString());
        currentAnnoIndex+=1;
        ModelAndView mav = new ModelAndView("anno");
        //存储标注结果更新当前标注数据index
        userService.addAnnoResult(annoResult,userTaskID,currentAnnoIndex);
        //获取下一个标注对象并返回
        Anno anno = userService.startAnno(userTaskID, currentAnnoIndex,taskID);
        mav.addObject("anno", anno);
        return mav;
    }
}
