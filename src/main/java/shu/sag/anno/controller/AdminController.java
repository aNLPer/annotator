package shu.sag.anno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Task;
import shu.sag.anno.pojo.UserTask;
import shu.sag.anno.service.AdminService;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/addConfig")
    public String addConfig(Config config){
        int status = adminService.addConfig(config);
        if(status==1){
            return "success!";
        }else{
            return "failed!";
        }
    }

    @RequestMapping("/addTask")
    public String addTask(Task task){
        int status = adminService.addTask(task);
        if(status==1){
            return "succeed!";
        }else{
            return "failed!";
        }
    }


    @RequestMapping("/deleteTask")
    public String deleteTask(int id){
        int status = adminService.deleteTaskByID(id);
        if(status==1){
            return "删除成功！";
        }else{
            return "删除失败！";
        }
    }

    @RequestMapping("/updateTask")
    public String updateTask(Task task){
        // 需要判断该表的
        int status = adminService.updateTask(task);
        if(status==1){
            return "更新成功！";
        }else{
            return "更新失败！";
        }
    }

    @RequestMapping("getAllTask")
    public ModelAndView getAllTask(int currentIndex, int pageSize){
        ModelAndView mav = new ModelAndView("tasklist");
        List<Task> taskList = adminService.getAllTask(currentIndex, pageSize);
        mav.addObject("taskList", taskList);
        return mav;
    }

    @RequestMapping("/assignment")
    public String addUserTask(UserTask userTask){
        int status = adminService.addUserTask(userTask);
        if(status==1){
            return "success!";
        }else {
            return "failed!";
        }
    }
}
