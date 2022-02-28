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
    private String datasetTableName;
    private String resultTableName;
    private int currentAnnoIndex;
    private int textID;
    private String currentText;
}
