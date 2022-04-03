package shu.sag.anno.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class JSONUtils {

    public static boolean isJson(String content) {
         try {
                 JSONObject jsonStr = JSONObject.parseObject(content);
                 if (jsonStr==null){
                     return false;
                 }else{
                     return true;
                 }
             } catch (Exception e) {
                 return false;
           }
     }

     public static void main(String[] args){
         System.out.println(UUID.randomUUID().toString());
     }
}
