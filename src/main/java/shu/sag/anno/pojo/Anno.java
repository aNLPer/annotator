package shu.sag.anno.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anno {
    private int id;//待标注数据id
    private String text;//待标注文本
    private String resultTableName;//标注结果表名字
    private String rawTableName;//待标注数据表名字
    private String config;//config表congfig字段
    private String label;//若已标注则返回标注标签
}
