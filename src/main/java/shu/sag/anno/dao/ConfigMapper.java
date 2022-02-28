package shu.sag.anno.dao;

import shu.sag.anno.pojo.Config;

import java.util.List;

public interface ConfigMapper {
    // 增加任务配置
    int addConfig(Config config);
    // 通过id删除任务配置
    int deleteConfig(int id);
    // 通过id获取任务配置
    Config getConfigByID(int id);
    // 通过标注类型获取任务配置
    Config getConfigByType(String type);
    // 分页获取任务配置
    List<Config> getAllConfig(int currentIndex, int pageSize);
    // 更新任务配置
    int updateConfig(Config config);
}
