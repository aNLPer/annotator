package shu.sag.anno.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shu.sag.anno.pojo.User;
import shu.sag.anno.service.SuperAdminService;
import shu.sag.anno.utils.NameGen;
import shu.sag.anno.utils.PwdSecurity;
import shu.sag.anno.utils.TokenUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    // 获取用户列表(已调试)
    @RequestMapping("super/user/list")
    @ResponseBody
    public Object getUserList(@RequestHeader("token") String token, int currentIndex, int pageSize, String searchRole, String searchValue){
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
            if (role.trim().equals("0") & status.trim().equals("0")) { //  权限检查通过
                if(searchValue == null){
                    searchValue = "%";
                }
                if(searchValue.equals("")){
                    searchValue = "%";
                }
                // 获取用户列表
                List<User> userList= superAdminService.searchUser(currentIndex,pageSize,searchRole, searchValue);
                if (userList == null){
                    res.put("code", 1);
                    res.put("message", "系统错误，获取失败！");
                    return 0;
                }
                // 对应role的用户总数量
                int userTotal = superAdminService.searchUserResCount(searchRole, searchValue);
                res.put("code", 0);
                res.put("message", "获取成功！");
                res.put("data",new JSONObject());
                res.getJSONObject("data").put("userList", userList);
                res.getJSONObject("data").put("userTotal",userTotal);
                return res;
            } else {
                res.put("code", 1);
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 删除用户(已调试)
    @RequestMapping("super/user/delete")
    @ResponseBody
    public Object deleteUser(@RequestHeader("token") String token, String username){
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
            if (role.trim().equals("0")) {
                //删除用户以及与该用户相关的任务
                int status_1 = superAdminService.deleteUserByUsername(username);
                if(status_1>0){
                    // 删除该用户相关的任务
                    superAdminService.deleteUserTaskByUsername(username);
                    res.put("code",0);
                    res.put("message","删除成功！");
                    return res;
                }else{
                    res.put("code", 1);
                    res.put("message", "用户不存在或系统错误");
                    return res;
                }
            } else {
                res.put("code", "1");
                res.put("message", "没有超级管理员权限！拒绝访问");
                return res;
            }

        }
    }

    // 重置用户密码(已调试)
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
            if(password==null){
                password = "123456";
            }
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.trim().equals("0")) {
                //修改用户密码
                int status = superAdminService.updateUserPassword(username, PwdSecurity.encode(password.trim()));
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

    // 禁用或者启动用户（已调试）
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
            // 判断用户状态是否合法
            if(!(status.equals("0") || status.equals("1"))){
                res.put("code", 1);
                res.put("message","设置用户状态不合法");
                return res;
            }
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.trim().equals("0")) {
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

    // 添加管理员或者普通账户（已调试）
    @RequestMapping(value = "super/user/add",method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(@RequestHeader("token") String token, String userList){
        //System.out.println(userList);
        JSONObject res = new JSONObject();
        // 检测用户登录状态
        String verifyRes = TokenUtil.verify(token);
        if (verifyRes.equals("-1")) {
            res.put("code", 1);
            res.put("message", "获取登录信息失效，请重新登录！");
            return res;
        } else {
            JSONObject loginUser = JSON.parseObject(verifyRes);
            // 检查用户权限
            String role = loginUser.getString("role");
            if (role.trim().equals("0")) {
                int count = 0;
                // 将json字符串转化成为java对象数组
                List<User> users = null;
        try {
            users = JSONArray.parseArray(userList, User.class);
        }catch (JSONException e){
            res.put("code",1);
            res.put("message","用户列表格式出错，系统无法识别！");
            System.out.println(e.getStackTrace());
            return res;
        }
        //添加用户
        for(User user: users){
            String username = user.getUsername();
            if(username == null || username.equals("")){
                count++;
                continue;
            }
            int userCount = superAdminService.UserisExist(username);
            if (userCount==0 ) {// 若用户不存在
                if(user.getName()==null || user.getName().trim().equals("")){
                    user.setName(NameGen.randomName());
                }
                // 用户密码加密
                if(user.getPassword()==null || user.getPassword().trim().equals("") ){
                    user.setPassword(PwdSecurity.encode("123456"));
                }else{
                    user.setPassword(PwdSecurity.encode(user.getPassword().trim()));
                }
                superAdminService.addUser(user);
            }else{
                count ++;
            }
        }
        res.put("code", 0);
        res.put("message","已执行，有 "+count+" 个用户账号未添加！");
        return res;
        }else{
            res.put("code", 1);
            res.put("message", "没有超级管理员权限！拒绝访问");
            return res;
        }
        }
    }

}
