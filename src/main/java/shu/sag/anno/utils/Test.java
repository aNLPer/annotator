package shu.sag.anno.utils;

public class Test {

    public static void main(String[] args){
        TokenUtil a = new TokenUtil();
        String username = "111";
        String pwd = "123456";
        String token = a.sign(username,pwd);
        System.out.println(token);
//        String token = "eyJhbGciOiJIUzI1NiIsIlR5cGUiOiJKd3QiLCJ0eXAiOiJKV1QifQ.eyJwd2QiOiIxMTEiLCJleHAiOjE2NDYyMTA4NTQsImFjY291bnQiOiJ4dXFpIn0.CodW2mfFKZDqCg6ACT3LqAeEwGKmbXi2VLDikQzCiww";
//        String VerEnd = TokenUtil.verify(token);
//        System.out.println(VerEnd);


    }


}
