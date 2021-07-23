package com.wangli.gulimall.auth.feign;

import com.wangli.common.utils.R;
import com.wangli.gulimall.auth.dto.GiteeTokenDto;
import com.wangli.gulimall.auth.vo.UserLoginVo;
import com.wangli.gulimall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author WangLi
 * @date 2021/7/18 14:34
 * @description 会员模块远程服务
 */
@FeignClient(name = "gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo registerVo);

    @PostMapping("/member/member/login")
    R login(UserLoginVo loginVo);

    @PostMapping("/member/member/gitee/login")
    R login(GiteeTokenDto token);
}
