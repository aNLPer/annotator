package shu.sag.anno.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shu.sag.anno.pojo.Anno;
import shu.sag.anno.pojo.AnnoResult;
import shu.sag.anno.pojo.User;
import shu.sag.anno.service.UserService;

@RestController
public class UserAsynController {
    @Autowired
    private UserService userService;

    //@RequestMapping("submitAnnoResult")
//    public String submitAnnoResult(AnnoResult annoResult, int userTaskID){
//        //存储标注结果
//        userService.addAnnoResult(annoResult, userTaskID);
//        //修改当前标注index
//
//        //获取下一个标注对象并返回
//        return "";
//    }

}
