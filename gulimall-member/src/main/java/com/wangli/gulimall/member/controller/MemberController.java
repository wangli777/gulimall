package com.wangli.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.wangli.common.exception.BizCodeEnum;
import com.wangli.gulimall.member.exception.PhoneException;
import com.wangli.gulimall.member.exception.UsernameException;
import com.wangli.gulimall.member.feign.CouponFeignService;
import com.wangli.gulimall.member.vo.MemberUserLoginVo;
import com.wangli.gulimall.member.vo.MemberUserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wangli.gulimall.member.entity.MemberEntity;
import com.wangli.gulimall.member.service.MemberService;
import com.wangli.common.utils.PageUtils;
import com.wangli.common.utils.R;



/**
 * 会员
 *
 * @author wangli
 * @email 1151723225@qq.com
 * @date 2021-05-30 13:53:33
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @PostMapping("/register")
    public R register(@RequestBody MemberUserRegisterVo registerVo) {

        try {
            memberService.register(registerVo);
        } catch (UsernameException e) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        } catch (PhoneException e) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginVo loginVo) {

        MemberEntity memberEntity = memberService.login(loginVo);
        if (memberEntity == null) {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }else{
            return R.ok().setData(memberEntity);
        }
    }

    /**
     * 远程调用 gulimall-coupon 模块的接口
     */
    @RequestMapping("/coupons")
    public R coupons(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R memberCoupons = couponFeignService.memberCoupons();
        return R.ok().put("member", memberEntity).put("coupons",memberCoupons.get("coupons"));
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
