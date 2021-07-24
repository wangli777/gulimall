package com.wangli.gulimall.cart.interceptor;

import cn.hutool.core.util.StrUtil;
import com.wangli.common.constant.AuthServerConstant;
import com.wangli.common.constant.CartConstant;
import com.wangli.common.vo.resp.MemberRespVo;
import com.wangli.gulimall.cart.to.UserInfoTo;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author WangLi
 * @date 2021/7/24 22:26
 * @description 购物车的验证拦截器，获取购物车数据前，判断用户的登录状态，并封装传递给Controller等
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 前置拦截执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        //1、判断用户是否登录
        HttpSession session = request.getSession();
        MemberRespVo member = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member != null) {
            //登录了，获取已登录的购物车数据
            userInfoTo.setUserId(member.getId());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (CartConstant.TEMP_USER_COOKIE_NAME.equals(cookie.getName())) {
                    userInfoTo.setUserKey(cookie.getValue());
                    //本次请求 本来就有user-key
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //不管登录与否，只要没有user-key，都设置一个临时用户标识user-key
        if (StrUtil.isBlank(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

        //目标方法执行之前将用户信息设置到ThreadLocal
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 后置拦截 将user-key写入响应的cookie中
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        if (!userInfoTo.isTempUser()) {
            //没有user-key，才给响应新设置一个
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
