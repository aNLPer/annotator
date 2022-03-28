package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private int id;
    private String taskName;
    private String datasetTableName;
    private int configID;
    private int taskStatus;
    private String resultTableName;
    private String taskScope;
    private String taskType;
}
