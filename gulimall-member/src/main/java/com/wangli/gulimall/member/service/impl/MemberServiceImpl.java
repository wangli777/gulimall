package com.wangli.gulimall.member.service.impl;

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

    @Override
    public MemberEntity login(GiteeTokenDto token) {

        MemberEntity memberEntity = null;
        
        GiteeAuthEntity giteeAuthEntity = giteeAuthDao.selectOne(new QueryWrapper<GiteeAuthEntity>().eq("gitee_user_id", token.getGiteeUserId()));
        if (giteeAuthEntity == null) {
            //首次登录，自动注册
            // TODO: 2021/7/22  
        }else {
            memberEntity = this.getById(giteeAuthEntity.getMemberId());
        }
        return memberEntity;
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