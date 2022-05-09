package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Config;

import java.util.List;

public interface ConfigMapper {
    // 增加任务配置
    int addConfig(Config config);
    // 分页获取任务配置
    List<Config> getAllConfig(@Param("currentIndex") int currentIndex, @Param("pageSize") int pageSize);
    // 获取配置项总数
    int getConfigNum();
    // 通过id删除任务配置
    int deleteConfig(@Param("id") int id);
    // 更新任务配置
    int updateConfig(Config config);
    // 通过id获取任务配置
    Config getConfigByID(@Param("id") int id);
    // 通过标注类型获取任务配置
    Config getConfigByType(String type);

    // 模糊搜索config
    List<Config> searchConfig(@Param("currentIndex") int currentIndex,
                              @Param("pageSize") int pageSize,
                              @Param("creator") String creator,
                              @Param("searchValue") String searchValue);

    // 模糊搜索结果数目
    int searchConfigResCount(@Param("creator")String creator,
                             @Param("searchValue") String searchValue);
}
