package com.wangli.gulimall.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.wangli.gulimall.member.dao.GiteeAuthEntityDao;
import com.wangli.gulimall.member.dao.MemberLevelDao;
import com.wangli.gulimall.member.dto.GiteeTokenDto;
import com.wangli.gulimall.member.entity.GiteeAuthEntity;
import com.wangli.gulimall.member.entity.MemberLevelEntity;
import com.wangli.gulimall.member.exception.PhoneException;
import com.wangli.gulimall.member.exception.UsernameException;
import com.wangli.gulimall.member.vo.MemberUserLoginVo;
import com.wangli.gulimall.member.vo.MemberUserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangli.common.utils.PageUtils;
import com.wangli.common.utils.Query;

import com.wangli.gulimall.member.dao.MemberDao;
import com.wangli.gulimall.member.entity.MemberEntity;
import com.wangli.gulimall.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Autowired
    GiteeAuthEntityDao giteeAuthDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberUserRegisterVo registerVo) {


        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setCreateTime(new Date());
        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));
        memberEntity.setLevelId(levelEntity.getId());
        //设置用户名、手机号
        //检查用户名、手机号是否唯一
        checkUsernameUnique(registerVo.getUserName());
        checkPhoneUnique(registerVo.getPhone());

        memberEntity.setUsername(registerVo.getUserName());
        memberEntity.setNickname(registerVo.getUserName());
        memberEntity.setMobile(registerVo.getPhone());

        //密码进行MD5加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(registerVo.getPassword());
        memberEntity.setPassword(encode);

        this.baseMapper.insert(memberEntity);
    }

    @Override
    public MemberEntity login(MemberUserLoginVo loginVo) {
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginVo.getLoginacct()).or().eq("mobile", loginVo.getLoginacct()));

        if (memberEntity == null) {
            return null;

        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(loginVo.getPassword(), memberEntity.getPassword());
            if (matches) {
                return memberEntity;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MemberEntity login(GiteeTokenDto token) {
        MemberEntity member;

        if (token == null) {
            return null;
        }
        //先查询获取授权用户的资料 https://gitee.com/api/v5/swagger#/getV5User
        //主要是获取gitee用户唯一id
        Map<String, Object> map = Maps.newHashMap();
        map.put("access_token", token.getAccessToken());
        String userInfo = HttpUtil.get("https://gitee.com/api/v5/user", map);
        JSONObject jsonObject = JSONUtil.parseObj(userInfo);
        if (jsonObject != null) {
            Long id = jsonObject.getLong("id");
            String loginName = jsonObject.getStr("login");
            String nickName = jsonObject.getStr("name");
            String avatarUrl = jsonObject.getStr("avatar_url");
            token.setGiteeUserId(id);
            token.setLoginName(loginName);
            token.setNickName(nickName);
            token.setAvatarUrl(avatarUrl);
        }
        GiteeAuthEntity giteeAuthEntity = giteeAuthDao.selectOne(new QueryWrapper<GiteeAuthEntity>().eq("gitee_user_id", token.getGiteeUserId()));
        if (giteeAuthEntity == null) {
            //首次登录
            //自动注册
            member = new MemberEntity();
            member.setCreateTime(new Date());
            member.setLevelId(1L);
            member.setHeader(token.getAvatarUrl());
            member.setNickname(token.getNickName());
            this.save(member);

            GiteeAuthEntity authEntity = BeanUtil.toBean(token, GiteeAuthEntity.class,
                    CopyOptions.create().setIgnoreProperties("createdAt"));
            authEntity.setCreatedAt(new Date(token.getCreatedAt() * 1000L));
            authEntity.setCreateTime(new Date());
            authEntity.setUpdateTime(new Date());
            authEntity.setMemberId(member.getId());
            giteeAuthDao.insert(authEntity);
        } else {
            //更新数据
            BeanUtil.copyProperties(token, giteeAuthEntity, "createdAt");
            giteeAuthEntity.setCreatedAt(new Date(token.getCreatedAt() * 1000L));
            giteeAuthDao.updateById(giteeAuthEntity);

            member = this.getById(giteeAuthEntity.getMemberId());
        }
        return member;
    }

    /**
     * 校验手机号是否唯一
     *
     * @param phone
     * @throws PhoneException
     */
    private void checkPhoneUnique(String phone) throws PhoneException {

        Integer mobileCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobileCount > 0) {
            throw new PhoneException();
        }
    }

    /**
     * 校验用户名是否唯一
     *
     * @param userName
     * @throws UsernameException
     */
    private void checkUsernameUnique(String userName) throws UsernameException {

        Integer usernameCount = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (usernameCount > 0) {
            throw new UsernameException();
        }
    }

}