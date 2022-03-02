package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.AnnoResult;
import shu.sag.anno.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    // 用户登录
    User login(@Param("username") String username, @Param("password") String password);
    //返回当前标注数据
    Map<String,Object> getCurrentAnnoData(@Param("tableName") String tableName, @Param("currentIndex") int currentIndex);
    // 添加标注结果
    int addAnnoResult(AnnoResult annoResult);
}
