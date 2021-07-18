package com.wangli.gulimall.member.exception;

/**
 * @Author: WangLi
 * @Date: 2021/7/18
 */
public class PhoneException extends RuntimeException {

    public PhoneException() {
        super("存在相同的手机号");
    }
}
