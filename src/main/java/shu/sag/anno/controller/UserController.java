package shu.sag.anno.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shu.sag.anno.pojo.Anno;
import shu.sag.anno.pojo.AnnoResult;
import shu.sag.anno.pojo.User;
import shu.sag.anno.pojo.UserTask;
import shu.sag.anno.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("user/login")
    public String login(String userAccount, String password, HttpSession httpSession, HttpServletResponse response){
        User user = userService.login(userAccount, password);
        if(user==null){
            response.setStatus(404);

            return "loginFailed";
        }else{
            httpSession.setAttribute("user",user);
            return "forward:getMyTask?userAccount="+user.getAccount()+"&currentIndex="+0+"&pageSize="+5;
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
    public ModelAndView getMyTask(String userAccount,int currentIndex, int pageSize){
        ModelAndView mav = new ModelAndView("myTaskList");
        List<UserTask> myTaskList = userService.getUserTaskByUserAccount(userAccount, currentIndex,pageSize);
        mav.addObject("myTaskList", myTaskList);
        return mav;
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
