package Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Administrator;
import domain.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    @Qualifier("mapper")
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Administrator admin= (Administrator) request.getSession().getAttribute("admin");
        ResultInfo rs=new ResultInfo();
        if(admin==null){
            response.setContentType("application/json;charset=utf-8");
            rs.setFlag(false);
            rs.setIslogin(false);
            rs.setInfo("请先登录管理员账号");
            response.getWriter().write(objectMapper.writeValueAsString(rs));
            return false;
        }else{
            return true;
        }
    }
}
