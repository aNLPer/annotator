package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnoResult {
    private String datasetTableName;
    private String resultTableName;
    private int textID;
    private String username;
    private String text;
    private String label;
}
