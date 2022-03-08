package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anno {
    private int taskID;

    private int userTaskID;
    //
    private String datasetTableName;
    //结果集
    private String resultTableName;

    private int currentAnnoIndex;
    // 当前标注内容id
    private int textID;
    // 当前的标注内容
    private String currentText;
}
