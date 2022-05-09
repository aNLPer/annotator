package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Application;

import java.util.List;

public interface ApplicationMapper {
    // 添加用户申请任务
    int addApplication(Application AT);

    // 撤回任务申请
    int withdrawApplication(int id);

    // 通过id获取申请
    Application getApplicationByID(int id);

    // 获取用户申请列表
    List<Application> getApplicationList(@Param("username")String username,
                                         @Param("currentIndex")int currentIndex,
                                         @Param("pageSize")int pageSize);

    // 用户申请总数
    int ApplicationCount(@Param("username") String username);

    // 根据username和taskid获取application
    Application getApplicationByTaskIDandUsername(@Param("username") String username, @Param("taskid") int taskid);

    // 获取用户申请列表
    List<Application> seachApplication(@Param("currentIndex")int currentIndex,
                                       @Param("pageSize")int pageSize,
                                       @Param("creator") String creator,
                                       @Param("username")String username,
                                       @Param("applystatus")String applystatus);

    // 用户申请计数
    int countSeachedApplication(@Param("creator") String creator,
                                @Param("username")String username,
                                @Param("applystatus")String applystatus);

    int setApplyStatus(@Param("id") int id, @Param("applystatus") String applystatus);
}
