package com.wangli.gulimall.member.dao;

import com.wangli.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:53:33
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
