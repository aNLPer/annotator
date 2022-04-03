package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;
import shu.sag.anno.pojo.Config;
import shu.sag.anno.pojo.Dataset;

import java.util.List;

public interface DatasetMapper {
    // 获取所有的数据集
    List<Dataset>  getAllDataset(@Param("currentIndex") int currentIndex, @Param("pageSize") int pageSize);
    // 模糊搜索dataset
    List<Dataset> searchDataset(@Param("currentIndex") int currentIndex, @Param("pageSize") int pageSize, @Param("searchValue") String searchValue);
    // 模糊搜索结果数目
    int searchDatasetResCount(String searchValue);
    // 通过id获取数据集
    Dataset getDatasetByID(int id);
    // 获取用户标注文本
    String getTextFieldFromDataset(@Param("datasetTableName")String datasetTableName,
                                   @Param("id") int id,
                                   @Param("fieldName") String fieldName);

    // 查询标注结果表中的数据
    int countAnnoResult(@Param("resultTableName") String resultTableName,
                        @Param("username") String username,
                        @Param("textID") int textID);

    // 创建数据集表
    int createDatasetTable(@Param("datasetTableName")String datasetTableName);

    // 批量添加数据
    int addItemBatch(@Param("datasetTableName") String datasetTableName, @Param("items") List<String> items);

    // 添加数据集信息
    int addDataset(Dataset dataset);

    // 删除数据库表
    void dropDataset(@Param("datasetTableName") String datasetTableName);

    // 删除数据数据库信息
    int deleteDatasetInfo(@Param("id") int id);

}
