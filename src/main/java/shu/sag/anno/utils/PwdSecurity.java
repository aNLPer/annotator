package shu.sag.anno.utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;


public class PwdSecurity {
    public static final String KEY_MD5 = "MD5";

    public static String encode(String str) {
        try {
            //生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            return Base64.getEncoder().encodeToString(md.digest(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
