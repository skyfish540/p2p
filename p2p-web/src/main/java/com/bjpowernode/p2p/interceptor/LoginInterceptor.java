package com.bjpowernode.p2p.interceptor;

import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.model.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //登录验证
        HttpSession session = httpServletRequest.getSession();
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        if (user==null){
            //重定向到登录页面
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
