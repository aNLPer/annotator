package shu.sag.anno.dao;

import org.apache.ibatis.annotations.Param;

public interface DatasetMapper {
    String getTextFieldFromDataset(@Param("datasetTableName") String datasetTableName,
                                   @Param("id") int id,
                                   @Param("fieldName") String fieldName);
    int addAnnoResult(@Param("resultTableName") String resultTableName,
                      @Param("username") String username,
                      @Param("textID") int textID,
                      @Param("text") String text,
                      @Param("label") String label,
                      @Param("datasetTableName") String datasetTableName);
}
