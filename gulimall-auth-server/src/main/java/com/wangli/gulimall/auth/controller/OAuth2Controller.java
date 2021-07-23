package com.wangli.gulimall.auth.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.wangli.common.utils.R;
import com.wangli.gulimall.auth.dto.GiteeTokenDto;
import com.wangli.gulimall.auth.feign.MemberFeignService;
import com.wangli.gulimall.auth.vo.resp.MemberRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author WangLi
 * @date 2021/7/22 21:40
 * @description 社交登录
 */
@Controller
@Slf4j
public class OAuth2Controller {

    @Value("${auth.gitee.clientId}")
    private String giteeClientId;

    @Value("${auth.gitee.redirectUri}")
    private String giteeRedirectUri;

    @Value("${auth.gitee.clientSecret}")
    private String giteeClientSecret;

    @Autowired
    MemberFeignService memberFeignService;

    /**
     * gitee登录回调处理
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/oauth2/gitee/login")
    public String giteeLogin(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state, HttpSession session) {
        log.debug("/oauth2/gitee/login, code:{}; state:{}", code, state);
        //获取accesstoken
        // https://gitee.com/oauth/token?grant_type=authorization_code
        // &code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
//        HttpUtil.createPost("https://gitee.com/oauth/token").form();
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("code", code);
        paramMap.put("client_id", giteeClientId);
        paramMap.put("redirect_uri", giteeRedirectUri);
        paramMap.put("client_secret", giteeClientSecret);
        String response = HttpUtil.post("https://gitee.com/oauth/token", paramMap);
        log.debug("/oauth2/gitee/login, response:{}", response);

        //{"access_token":"2ea25fb0922d1d3b3703f8ff02e50f6e","token_type":"bearer",
        // "expires_in":86400,"refresh_token":"b0f3bda973e13acc676df86def5b3f5e54c2caceacdb62b7c151afcabf8e5524",
        // "scope":"user_info","created_at":1626960837}
        if (StrUtil.isNotBlank(response)) {
            GiteeTokenDto token = JSONUtil.toBean(response, GiteeTokenDto.class);
            log.debug("/oauth2/gitee/login,token:{}", token);

            R login = memberFeignService.login(token);
            if (login.getCode() != 0) {
                //失败
                return "redirect:http://auth.mall.com/login.html";
            } else {
                MemberRespVo memberRespVo = login.getData(new TypeReference<MemberRespVo>() {
                });
                log.debug("login success memberRespVo:{}", memberRespVo);
                session.setAttribute("loginUser", memberRespVo);
                return "redirect:http://mall.com";
            }
        } else {
            //登录失败
            return "redirect:http://auth.mall.com/login.html";
        }
    }
}
