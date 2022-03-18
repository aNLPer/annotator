package shu.sag.anno.controller;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Task;
import shu.sag.anno.pojo.UserTask;
import shu.sag.anno.service.AdminService;
import com.alibaba.fastjson.JSONObject;
import shu.sag.anno.utils.TokenUtil;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 添加配置项
    @RequestMapping("admin/config/add")
    @ResponseBody
    public Object addConfig(@RequestHeader("token") String token, Config config){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.equals("1")){
                // 添加配置项
                int status = adminService.addConfig(config);
                if (status==1){
                    res.put("code","0");
                    res.put("message","添加成功！");
                    return res;
                }else{
                    res.put("code","1");
                    res.put("message","添加失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有系统管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 获取配置项列表
    @RequestMapping("admin/config/list")
    @ResponseBody
    public Object configList(@RequestHeader("token") String token, int currentIndex, int pageSize){
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){
                // 获取配置列表
                List<Config> configList = adminService.getAllConfig(currentIndex, pageSize);
                int configTotal = adminService.getConfigNum();
                if (configList!=null){
                    res.put("code","0");
                    res.put("message","获取"+configTotal+"条配置项！");
                    res.getJSONObject("data").put("configList", configList);
                    res.getJSONObject("data").put("configTotal", configTotal);
                    return res;
                }else{
                        res.put("code","1");
                        res.put("message","系统错误，获取置项失败！");
                        return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

    //更新配置项
    @RequestMapping("admin/config/edit")
    @ResponseBody
    public Object editConfig(@RequestHeader("token") String token, Config config){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){ // 具有更新配置项的权限
                int status = adminService.updateConfig(config);
                if(status == 0){
                    res.put("code","0");
                    res.put("message","更新成功！");
                    return res;
                }else{
                    if(status == 1){
                        res.put("code","1");
                        res.put("message","系统错误，更新失败！");
                        return res;
                    }
                    res.put("code","1");
                    res.put("message","配置项已分配，更新失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 删除配置项
    @RequestMapping("admin/config/delete")
    @ResponseBody
    public Object addTask(@RequestHeader("token") String token, int id){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){ // 具有删除配置项的权限
                int status = adminService.deleteConfig(id);
                if(status == 0){
                    res.put("code","0");
                    res.put("message","删除成功！");
                    return res;
                }else{
                    if(status == 1){
                        res.put("code","1");
                        res.put("message","系统错误，删除失败！");
                        return res;
                    }
                    res.put("code","1");
                    res.put("message","配置项已分配，删除失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }

        }
    }


    // 未完成
    @RequestMapping("admin/dataset/list")
    @ResponseBody
    public Object datasetList(@RequestHeader("token") String token){
        JSONObject res = new JSONObject();
        return res;
    }

    // 未完成
    @RequestMapping("admin/dataset/delete")
    @ResponseBody
    public Object datasetDelete(@RequestHeader("token") String token,  int id){
        JSONObject res = new JSONObject();
        return res;
    }

    // 获取所有任务列表
    @RequestMapping("admin/task/list")
    @ResponseBody
    public Object taskList(@RequestHeader("token") String token, int currentIndex, int pageSize){
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){
                // 获取任务列表
                List<Task> taskList = adminService.getAllTask(currentIndex, pageSize);
                int taskTotal = adminService.getTaskNum();
                if (taskList!=null){
                    res.put("code","0");
                    res.put("message","获取"+taskTotal+"条配置项！");
                    res.getJSONObject("data").put("taskList", taskList);
                    res.getJSONObject("data").put("taskTotal", taskTotal);
                    return res;
                }else{
                    res.put("code","1");
                    res.put("message","系统错误，获取置项失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 获取添加任务页面需要的参数（未完成）
    @RequestMapping("admin/task/add")
    @ResponseBody
    public Object addTaskPage(){
        JSONObject res = new JSONObject();
        return res;
    }

    // 添加任务
    @RequestMapping("admin/task/add.do")
    @ResponseBody
    public Object addTask(@RequestHeader("token") String token, Task task){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.equals("1")){
                // 添加配置项
                int status = adminService.addTask(task);
                if (status==0){
                    res.put("code","0");
                    res.put("message","添加成功！");
                    return res;
                }else{
                    if(status==1){
                        res.put("code","1");
                        res.put("message","添加失败！");
                        return res;
                    }else{
                        res.put("code","1");
                        res.put("message","标注结果集数据表创建失败！");
                        return res;
                    }
                }
            }else{
                res.put("code","1");
                res.put("message","没有系统管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 更新任务
    @RequestMapping("admin/Task/edit")
    @ResponseBody
    public Object editTask(@RequestHeader("token") String token, Task task){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){ // 具有更新任务的权限
                int status = adminService.updateTask(task);
                if(status == 0){
                    res.put("code","0");
                    res.put("message","更新成功！");
                    return res;
                }else{
                    if(status == 1){
                        res.put("code","1");
                        res.put("message","系统错误，更新失败！");
                        return res;
                    }
                    res.put("code","1");
                    res.put("message","任务已分配，更新失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 删除任务
    @RequestMapping("admin/Task/delete")
    @ResponseBody
    public Object deleteTask(@RequestHeader("token") String token, int id){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")){
            res.put("code","1");
            res.put("message","获取登录信息失效，请重新登录！");
            return res;
        }else{
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            if (role.equals("1")){ // 具有删除任务的权限
                int status = adminService.deleteTaskByID(id);
                if(status == 0){
                    res.put("code","0");
                    res.put("message","删除成功！");
                    return res;
                }else{
                    if(status == 1){
                        res.put("code","1");
                        res.put("message","系统错误，删除失败！");
                        return res;
                    }
                    res.put("code","1");
                    res.put("message","任务已分配，删除失败！");
                    return res;
                }
            }else{
                res.put("code","1");
                res.put("message","没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

}
