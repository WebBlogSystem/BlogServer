package Interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Object user = request.getSession().getAttribute("user");
        ResultInfo rs=new ResultInfo();
        if(user==null){
            rs.setFlag(false);
            rs.setInfo("对不起,您还没有登录");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(rs));
            return false;
        }else{
            return true;
        }
    }
}
