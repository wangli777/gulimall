package com.wangli.gulimall.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangLi
 * @date 2021/7/22 21:53
 * @description
 */
@NoArgsConstructor
@Data
public class GiteeTokenDto {


    /**
     * accessToken
     */
    private String accessToken;
    /**
     * tokenType
     */
    private String tokenType;
    /**
     * expiresIn
     */
    private Integer expiresIn;
    /**
     * refreshToken
     */
    private String refreshToken;
    /**
     * scope
     */
    private String scope;
    /**
     * createdAt
     */
    private Integer createdAt;
}
