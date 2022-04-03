package shu.sag.anno.controller;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import shu.sag.anno.pojo.*;
import shu.sag.anno.service.AdminService;
import com.alibaba.fastjson.JSONObject;
import shu.sag.anno.utils.JSONUtils;
import shu.sag.anno.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    /* -------------------------数据集管理--------------------------------------*/

    // 数据上传及数据集表生成（已调试）
    @RequestMapping("admin/file/upload")
    @ResponseBody
    public Object testController(@RequestHeader("token") String token,
                                 @Param("file") MultipartFile file,
                                 @Param("remark") String remark)
                            throws IllegalStateException, IOException {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            String username = loginUser.getString("username");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限验证通过
                // 判断文件是否为空，空则返回失败页面
                if (file.isEmpty() || file==null) {
                    res.put("code",1);
                    res.put("message", "无法获取文件内容！");
                    return res;
                }
                adminService.fileUpload(file, remark, username);
                res.put("code", 0);
                res.put("message", "上传成功！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }

    }

    // 获取数据集列表（已调试）
    @RequestMapping("admin/dataset/list")
    @ResponseBody
    public Object datasetList(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchValue) {
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限验证通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                List<Dataset> datasetList = adminService.searchDataset(currentIndex, pageSize, searchValue);
                int datasetTotal = adminService.searchDatasetResCount(searchValue);
                if (datasetList != null) {
                    res.put("code", 0);
                    res.put("message", "获取" + datasetTotal + "条数据集！");
                    res.getJSONObject("data").put("datasetList", datasetList);
                    res.getJSONObject("data").put("datasetTotal", datasetTotal);
                    return res;
                } else {
                    res.put("code", 1);
                    res.put("message", "系统错误，获数据集失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 删除数据集
    @RequestMapping("admin/dataset/delete")
    @ResponseBody
    public Object datasetDelete(@RequestHeader("token") String token, int id) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限验证通过
                int delRes = adminService.deleteDataset(id);
                if(delRes == 0){
                    res.put("code", 0);
                    res.put("message", "删除成功！");
                    return res;
                }else{
                    res.put("code", 1);
                    res.put("message", "该数据库信息不存在，删除失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }



    /* -------------------------配置项管理--------------------------------------*/


    // 添加配置项(已测试)
    @RequestMapping("admin/config/add")
    @ResponseBody
    public Object addConfig(@RequestHeader("token") String token, Config config) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) { // 权限验证通过
                // 设置配置项创建者
                config.setCreator(loginUser.getString("username").trim());
                // 添加配置项
                int addRes = adminService.addConfig(config);
                if (addRes == 1) {
                    res.put("code", 0);
                    res.put("message", "添加成功！");
                    return res;
                } else {
                    if(addRes == 0){
                        res.put("code", 1);
                        res.put("message", "添加失败！");
                        return res;
                    }else{
                        res.put("code", 1);
                        res.put("message", "配置项内容不合法");
                        return res;
                    }
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 获取配置项列表（已调试）
    @RequestMapping("admin/config/list")
    @ResponseBody
    public Object configList(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchValue) {
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限验证通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                List<Config> configList = adminService.searchConfig(currentIndex, pageSize, searchValue);
                int configTotal = adminService.searchConfigResCount(searchValue);
                if (configList != null) {
                    res.put("code", 0);
                    res.put("message", "获取" + configTotal + "条配置项！");
                    res.getJSONObject("data").put("configList", configList);
                    res.getJSONObject("data").put("configTotal", configTotal);
                    return res;
                } else {
                    res.put("code", 1);
                    res.put("message", "系统错误，获取置项失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }

    //更新配置项（已测试）
    @RequestMapping("admin/config/edit")
    @ResponseBody
    public Object editConfig(@RequestHeader("token") String token, Config config) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if (role.trim().equals("1") & status.trim().equals("0")) { // 具有更新配置项的权限
                int upRes = adminService.updateConfig(config);
                if (upRes == 0) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                }
                if (upRes == 1) {
                    res.put("code", 1);
                    res.put("message", "系统错误，更新失败！");
                    return res;
                }
                if (upRes == 2) {
                    res.put("code", 1);
                    res.put("message", "配置项格式错误，更新失败！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "配置项不存在！");
                return res;

            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 删除配置项(已测试)
    @RequestMapping("admin/config/delete")
    @ResponseBody
    public Object addTask(@RequestHeader("token") String token, int id) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if (role.trim().equals("1") & status.trim().equals("0")) { // 具有删除配置项的权限
                int delRes = adminService.deleteConfig(id);
                if (delRes == 0) {
                    res.put("code", 0);
                    res.put("message", "删除成功！");
                    return res;
                }
                if (delRes == 1) {
                    res.put("code", 1);
                    res.put("message", "系统错误，删除失败！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "配置项不存在！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }


    /* -------------------------任务管理--------------------------------------*/


    // 获取所有任务列表(已调试)
    @RequestMapping("admin/task/list")
    @ResponseBody
    public Object taskList(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchValue) {
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限验证通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                List<Task> taskList = adminService.searchTask(currentIndex, pageSize, searchValue);
                int taskTotal = adminService.searchTaskResCount(searchValue);
                if (taskList != null) {
                    res.put("code", 0);
                    res.put("message", "获取" + taskTotal + "条任务！");
                    res.getJSONObject("data").put("taskList", taskList);
                    res.getJSONObject("data").put("taskTotal", taskTotal);
                    return res;
                } else {
                    res.put("code", 1);
                    res.put("message", "系统错误，获取任务失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }


    // 添加任务(已调试)
    @RequestMapping("admin/task/create")
    @ResponseBody
    public Object addTask(@RequestHeader("token") String token, Task task) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 具有添加任务的权限
                task.setCreator(loginUser.getString("username"));
                int addRes = adminService.addTask(task);
                if (addRes == 0) {
                    res.put("code", 0);
                    res.put("message", "添加成功！");
                    return res;
                }
                if(addRes== 1){
                    res.put("code", 1);
                    res.put("message", "系统错误，添加失败！");
                    return res;
                }
                if(addRes== 2){
                    res.put("code", 1);
                    res.put("message", "配置项不合法！");
                    return res;
                }if(addRes== 3){
                    res.put("code", 1);
                    res.put("message", "数据集不存在！");
                    return res;
                }if(addRes== 4){
                    res.put("code", 1);
                    res.put("message", "任务属性（公开/私有）不合法！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "标注索引不合法！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }
        }
    }

    @RequestMapping("admin/task/set/scope")
    @ResponseBody
    public Object setScope(@RequestHeader("token") String token, int taskid, String scope) {
        // 只允许更新任务私有/公开属性
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser== null || role == null || status == null){
                res.put("code", 1);
                res.put("message", "登录信息出错，请重新登录！");
                return res;
            }
            if (role.trim().equals("1") & status.trim().equals("0")) { // 具有更新任务的权限
                int setRes = adminService.setTaskScope(taskid, scope);
                if (setRes == 1) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                }else{
                    res.put("code", 1);
                    res.put("message", "更新失败！");
                    return res;
                }

            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 更新任务（简单，保留）
    @RequestMapping("admin/task/edit")
    @ResponseBody
    public Object editTask(@RequestHeader("token") String token, Task task) {
        // 只允许更新任务名字、任务配置、任务属性、起始标注位置、结束标注位置
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser== null || role == null || status == null){
                res.put("code", 1);
                res.put("message", "登录信息出错，请重新登录！");
                return res;
            }
            if (role.trim().equals("1") & status.trim().equals("0")) { // 具有更新任务的权限
                int upRes = adminService.updateTask(task);
                if (upRes == 0) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                }
                if (upRes == 1) {
                    res.put("code", 1);
                    res.put("message", "系统错误，更新失败！");
                    return res;
                }
                if (upRes == 2) {
                    res.put("code", 1);
                    res.put("message", "任务已被分配，请先尝试删除用户标注任务！");
                    return res;
                }
                if (upRes == 3) {
                    res.put("code", 1);
                    res.put("message", "配置项不合法，更新失败！");
                    return res;
                }
                if (upRes == 4) {
                    res.put("code", 1);
                    res.put("message", "任务可见属性不合法，更新失败！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "标注索引不合法，更新失败！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 删除标注任务（已调试）
    @RequestMapping("admin/task/delete")
    @ResponseBody
    public Object deleteTask(@RequestHeader("token") String token, int id) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            if(loginUser == null){
                res.put("code", 1);
                res.put("message", "用户登录信息获取失败，请重新登录！");
                return res;
            }
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") & status.trim().equals("0")) { // 具有删除任务的权限
                int delRes = adminService.deleteTaskByID(id);
                if (delRes == 0) {
                    res.put("code", 0);
                    res.put("message", "删除成功！");
                    return res;
                }
                if (delRes == 1) {
                    res.put("code", 1);
                    res.put("message", "系统错误，删除失败！");
                    return res;
                }
                if (delRes==2){
                    res.put("code", 1);
                    res.put("message", "任务已分配，删除失败！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "任务不存在！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }

        }
    }


    /* -------------------------分配任务管理--------------------------------------*/


    // 获取分派任务列表
    @RequestMapping("admin/assignedTask/list")
    @ResponseBody
    public Object userTaskList(@RequestHeader("token") String token, int currentIndex, int pageSize, String username, String taskName) {
        JSONObject res = new JSONObject();
        res.put("data", new JSONObject());
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            // 检查用户权限
            JSONObject loginUser = JSON.parseObject(verifyRes);
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if(loginUser == null || role == null || status == null){
                res.put("code", 1);
                res.put("message", "获取登录信息失效，请重新登录！");
                return res;
            }
            if (role.trim().equals("1") & status.trim().equals("0")) {// 权限检测通过
                if(username == null){
                    username="%";
                }
                if(username.equals("")){
                    username="%";
                }
                if(taskName == null){
                    taskName = "%";
                }
                if(taskName.equals("")){
                    taskName = "%";
                }
                // 获取分派任务列表
                List<UserTask> userTaskList = adminService.searchUserTask(currentIndex, pageSize, username, taskName);
                int userTaskTotal = adminService.searchUserTaskResCount(username, taskName);
                if (userTaskList != null) {
                    res.put("code", 0);
                    res.put("message", "获取" + userTaskTotal + "条用户任务项！");
                    res.getJSONObject("data").put("userTaskList", userTaskList);
                    res.getJSONObject("data").put("userTaskTotal", userTaskTotal);
                    return res;
                } else {
                    res.put("code", 1);
                    res.put("message", "系统错误，获取置项失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 分派任务(已调试)
    @RequestMapping("admin/task/assign")
    @ResponseBody
    public Object addUserTask(@RequestHeader("token") String token, UserTask userTask) {
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            String username = loginUser.getString("username");
            if (role.trim().equals("1") & status.trim().equals("0")) {// 有添加配置项的权限
                userTask.setCurrentAnnoIndex(userTask.getStartAnnoIndex());
                userTask.setCreator(username.trim());
                // 添加配置项
                int addRes = adminService.addUserTask(userTask);
                if (addRes == 1) {
                    res.put("code", 0);
                    res.put("message", "添加成功！");
                    return res;
                }
                if (addRes == 2) {
                    res.put("code", 1);
                    res.put("message", "用户或者任务不存在！");
                    return res;
                }
                if (addRes == 3) {
                    res.put("code", 1);
                    res.put("message", "标注范围不合法！");
                    return res;
                }
                res.put("code", 1);
                res.put("message", "添加失败！");
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }
        }
    }

    // 取消分派任务
    @RequestMapping("admin/assignedTask/delete")
    @ResponseBody
    public Object deleteUserTask(@RequestHeader("token") String token, int id){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", "1");
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.equals("1") & status.trim().equals("0")) {// 权限验证通过
                // 删除已分配的任务
                int delRes = adminService.deleteUserTaskByID(id);
                if (delRes == 1) {
                    res.put("code", "0");
                    res.put("message", "删除成功！");
                    return res;
                } else {
                    res.put("code", "1");
                    res.put("message", "删除失败失败！");
                    return res;
                }
            } else {
                res.put("code", "1");
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }

        }
    }


    /* -------------------------管理员任务申请管理--------------------------------------*/

    // 获取用户申请任务列表
    @RequestMapping("admin/application/list")
    @ResponseBody
    public Object listApplication(@RequestHeader("token") String token,
                                  int currentIndex,
                                  int pageSize,
                                  String username,
                                  String applystatus){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", "1");
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") && status.trim().equals("0")) {
                if(username == null){
                    username = "%";
                }
                if(username.trim().equals("")){
                    username = "%";
                }
                if(applystatus == null){
                    applystatus = "%";
                }
                if(username.trim().equals("")){
                    applystatus = "%";
                }
                List<Application> applications = adminService.seachApplication(currentIndex, pageSize, username, applystatus);
                for(Application app: applications){
                    int taskid = app.getTaskid();
                    Task task = adminService.getTaskByID(taskid);
                    app.setTaskName(task.getTaskName());
                }
                int applicationCount = adminService.countSeachedApplication(username, applystatus);
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
            } else {
                res.put("code", "1");
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 公开任务申请状态设置
    @RequestMapping("admin/applystatus/set")
    @ResponseBody
    public Object setApplystatus(@RequestHeader("token") String token,
                                  int id,
                                  String applystatus){
        JSONObject res = new JSONObject();
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", "1");
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            String status = loginUser.getString("status");
            if (role.trim().equals("1") && status.trim().equals("0")) {
                int setRes = adminService.setApplyStatus(id,applystatus);
                if(setRes == 0){
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                }else{
                    res.put("code", 1);
                    res.put("message", "申请不存在，更新失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有系统管理员权限！拒绝访问");
                return res;
            }

        }
    }
    //

}
