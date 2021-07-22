package com.wangli.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 * @TableName ums_gitee_auth
 */
@TableName(value ="ums_gitee_auth")
@Data
public class GiteeAuthEntity implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 商城会员id
     */
    private Long memberId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * gitee用户唯一id
     */
    private Long giteeUserId;
    /**
     * login
     */
    private String loginName;
    /**
     * name
     */
    private String nickName;
    /**
     * avatar_url
     */
    private String avatarUrl;

    /**
     * token
     */
    private String accessToken;

    /**
     * 
     */
    private String tokenType;

    /**
     * token多久过期
     */
    private Integer expiresIn;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 作用域
     */
    private String scope;

    /**
     * token创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}