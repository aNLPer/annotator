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
    private String config;
    private int datasetID;
    private String datasetTableName;
    private String resultTableName;
    private String taskStatus;
    private String taskScope;
    private int startAnnoIndex;
    private int endAnnoIndex;
    private String creator;
}
