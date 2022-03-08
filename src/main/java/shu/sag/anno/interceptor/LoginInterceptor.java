package shu.sag.anno.interceptor;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.Token;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shu.sag.anno.utils.TokenUtil;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        String isPass = TokenUtil.verify(token);

        System.out.println("==============execute preHandle=================");
        return false;
    }
}
