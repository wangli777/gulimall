package com.wangli.gulimall.cart.to;


import lombok.Data;

/**
 * @author WangLi
 * @date 2021/7/24 22:30
 * @description
 */
@Data
public class UserInfoTo {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 标识临时用户
     */
    private String userKey;

    /**
     * 是否有设置临时用户
     */
    private boolean tempUser;

}
