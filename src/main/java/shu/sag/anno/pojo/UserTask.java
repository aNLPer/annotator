package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTask {
    private int id;
    private String username;
    private int taskID;
    private String taskName;
    private int currentAnnoIndex;
    private int startAnnoIndex;
    private int endAnnoIndex;
    private String creator;
    // 配置项文本用于识别标注类型
    private String taskConfig;
}
