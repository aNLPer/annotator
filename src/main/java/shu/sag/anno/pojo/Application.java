package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private int id;
    private int taskid;
    private String taskName;
    private String username;
    private String applytime;
    private String applystatus;
}
