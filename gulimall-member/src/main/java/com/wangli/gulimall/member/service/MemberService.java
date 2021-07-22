package com.wangli.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangli.common.utils.PageUtils;
import com.wangli.gulimall.member.dto.GiteeTokenDto;
import com.wangli.gulimall.member.entity.MemberEntity;
import com.wangli.gulimall.member.vo.MemberUserLoginVo;
import com.wangli.gulimall.member.vo.MemberUserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:53:33
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberUserRegisterVo registerVo);

    MemberEntity login(MemberUserLoginVo loginVo);

    /**
     * gitee登录
     * @param token
     * @return
     */
    MemberEntity login(GiteeTokenDto token);
}

