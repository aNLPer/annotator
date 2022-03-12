package shu.sag.anno.utils;


import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

public class JSONUtils {
    public static boolean isJsonObject(String content) {
         try {
                 JSONObject jsonStr = JSONObject.parseObject(content);
                 return true;
             } catch (Exception e) {
                 return false;
           }
     }
}
