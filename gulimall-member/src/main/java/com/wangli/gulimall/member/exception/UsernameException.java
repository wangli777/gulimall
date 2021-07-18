package com.wangli.gulimall.member.exception;

/**
 * @Author: WangLi
 * @Date: 2021/7/18
 */
public class UsernameException extends RuntimeException {


    public UsernameException() {
        super("存在相同的用户名");
    }
}
