package shu.sag.anno.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import shu.sag.anno.pojo.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args){
        TokenUtil a = new TokenUtil();
        String username = "222";
        String status = "0";
        String role = "1";
        String token = a.sign(username,status,role);
        System.out.println(token);

    }


}
