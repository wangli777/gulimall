package com.wangli.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author WangLi
 * @date 2021/7/22 22:05
 * @description
 */
@Data
public class BaseEntity {
    private Long id;
    private Integer version;
    private Date createTime;
    private Date updateTime;
}
