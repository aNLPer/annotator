package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
    private int id;
    private String name;
    private String remark;
    private String creator;
    private int sampleNums;
    private String datatype;
}
