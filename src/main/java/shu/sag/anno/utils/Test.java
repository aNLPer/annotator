package shu.sag.anno.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import shu.sag.anno.pojo.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Test {

    public static void main(String[] args){
//        TokenUtil a = new TokenUtil();
//        String username = "222";
//        String status = "0";
//        String role = "1";
//        String token = a.sign(username,status,role);
//        System.out.println(token);
//        String token = "eyJhbGciOiJIUzI1NiIsIlR5cGUiOiJKd3QiLCJ0eXAiOiJKV1QifQ.eyJwd2QiOiIxMTEiLCJleHAiOjE2NDYyMTA4NTQsImFjY291bnQiOiJ4dXFpIn0.CodW2mfFKZDqCg6ACT3LqAeEwGKmbXi2VLDikQzCiww";
//        String VerEnd = TokenUtil.verify(token);
//        System.out.println(VerEnd);
//        System.out.println(TokenUtil.verify(token));
        String timestr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"));
        System.out.println(timestr);
    }


}
