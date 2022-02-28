package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTask {
    private int id;
    private String userAccount;
    private int taskID;
    private String taskName;
    private int currentAnnoIndex;
}
