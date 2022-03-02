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

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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

    @RequestMapping("user/logout")
    public String logout(HttpSession httpSession){
        //删除session登录信息
        httpSession.removeAttribute("user");
        User u = (User)httpSession.getAttribute("user");
        if(u == null){
            System.out.println("删除登录信息");
        }
        return "redirect:/index.jsp";
    }

    // 获取用户相关任务
    @RequestMapping("user/task/list")
    @ResponseBody
    public Object getMyTask(String token,int currentIndex, int pageSize){
        JSONObject TaskList = new JSONObject();
        TaskList.put("data", new JSONObject());
        TaskList.getJSONObject("data").put("token",token);
        //获取
        List<UserTask> myTaskList = userService.getUserTaskByUserAccount( token,currentIndex,pageSize);

        return null;
    }

    @RequestMapping("user/anno/start")
    public ModelAndView startAnno(int userTaskID, int currentIndex, int taskID){
        ModelAndView mav = new ModelAndView("anno");
        Anno anno = userService.startAnno(userTaskID, currentIndex, taskID);
        mav.addObject("anno", anno);
        return mav;
    }

    @RequestMapping("submitAnnoResult")
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
