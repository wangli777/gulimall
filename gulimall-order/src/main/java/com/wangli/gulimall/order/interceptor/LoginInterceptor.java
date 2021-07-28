package com.wangli.gulimall.order.interceptor;

import com.wangli.common.constant.AuthServerConstant;
import com.wangli.common.vo.resp.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author WangLi
 * @date 2021/7/27 21:59
 * @description 整个订单系统的服务都需要先进行登录拦截
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> LOGIN_USER = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberRespVo memberRespVo = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (memberRespVo == null) {
            //未登录
            session.setAttribute("msg","请先登录");
            response.sendRedirect("http://auth.mall.com/login.html");
            return false;
        }
        LOGIN_USER.set(memberRespVo);
        return true;
    }
}
