package shu.sag.anno.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.sag.anno.dao.UserMapper;
import shu.sag.anno.pojo.User;
import shu.sag.anno.service.SuperAdminService;
import shu.sag.anno.utils.TokenUtil;

import java.util.List;

@Controller
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    // 获取用户列表
    @RequestMapping("super/user/list")
    @ResponseBody
    public Object getUserList(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchRole){
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
            if (role.equals("0")) {
                // 获取用户列表
                List<User> userList= superAdminService.getUserListByRole(currentIndex,pageSize,searchRole);
                // 对应role的用户总数量
                int userTotal = superAdminService.getUserNumByRole(searchRole);
                res.put("code", "1");
                res.put("message", "获取失败！");
                res.put("data",new JSONObject());
                res.getJSONObject("data").put("userList", userList);
                res.getJSONObject("data").put("userTotal",userTotal);
                return res;
            } else {
                res.put("code", "1");
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 删除用户
    @RequestMapping("super/user/delete")
    @ResponseBody
    public Object deleteUser(@RequestHeader("token") String token, String username){
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
            if (role.equals("0")) {
                //删除用户以及与该用户相关的任务
                int status_1 = superAdminService.deleteUserByUsername(username);
                int status_2 = superAdminService.deleteUserTaskByUsername(username);
                if(status_1>0 & status_2>0){
                    res.put("code",0);
                    res.put("message","删除成功！");
                    return res;
                }else{
                    // 删除失败
                    // 回滚
                    res.put("code", 1);
                    res.put("message", "系统错误，删除失败！");
                    return res;
                }
            } else {
                res.put("code", "1");
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 重置用户密码
    @RequestMapping("super/user/set/pwd")
    @ResponseBody
    public Object updateUserPwd(@RequestHeader("token") String token, String username, String password) {
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
            if (role.equals("0")) {
                //修改用户密码
                int status = superAdminService.updateUserPassword(username, password);
                if (status > 0) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                } else {
                    // 删除失败
                    // 回滚
                    res.put("code", 1);
                    res.put("message", "系统错误，更新失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }
    // 禁用或者启动用户
    // 重置用户密码
    @RequestMapping("super/user/set/status")
    @ResponseBody
    public Object updateUserStatus(@RequestHeader("token") String token, String username, String status) {
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
            if (role.equals("0")) {
                //修改用户状态（启用/禁用）
                int update_res = superAdminService.updateUserStatus(username, status);
                if (update_res > 0) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                } else {
                    // 删除失败
                    // 回滚
                    res.put("code", 1);
                    res.put("message", "系统错误，更新失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 添加账户（管理员或者普通账户）
    @RequestMapping("super/user/add")
    @ResponseBody
    public Object addUser(@RequestHeader("token") String token, User user){
        JSONObject res = new JSONObject();
        // 查找username字段是否已经存在
        String username = user.getUsername();
        int userCount = superAdminService.UserisExist(username);
        // 若用户存在则返回
        if (userCount>0){
            res.put("code",1);
            res.put("message","该用户已存在");
            return res;
        }
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.equals("0")) {
                //添加用户
                int update_res = superAdminService.addUser(user);
                if (update_res > 0) {
                    res.put("code", 0);
                    res.put("message", "更新成功！");
                    return res;
                } else {
                    // 删除失败
                    // 回滚
                    res.put("code", 1);
                    res.put("message", "系统错误，更新失败！");
                    return res;
                }
            } else {
                res.put("code", 1);
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

}
