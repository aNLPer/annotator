package shu.sag.anno.utils;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    //过期时间1天
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    //私钥
    private static final String TOKEN_SECRET = "AnnoPrivateKey";
    /**
     * 生成签名
     * @param **account**
     * @param **pwd**
     * @return
     */
    public static String sign(String username, String status, String role) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 添加header——>设置payload——>过期时间——>签名并返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("role", role)
                    .withClaim("status",status)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
       public static String verify(String token){
        JSONObject UJson = new JSONObject();
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            UJson.put("username",jwt.getClaim("username").asString());
            UJson.put("status",jwt.getClaim("status").asString());
            UJson.put("role",jwt.getClaim("role").asString());
            return UJson.toJSONString();
        } catch (Exception e){
            return "-1";
        }

    }




}
