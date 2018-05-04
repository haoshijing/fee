package com.yingliguoji.fee.service;

import com.yingliguoji.fee.controller.request.MemberUpgradeRequest;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.UserMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.UserPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Transactional
    public Boolean upgradeToAgent(Integer memberId) {
        MemberPo memberPo = memberMapper.findById(memberId);
        if(memberPo != null && memberPo.getIs_daili() == 1){
            UserPo queryPo = userMapper.findByName(memberPo.getName());
            if(queryPo != null){
                return  false;
            }
            String name = memberPo.getName();
            String newUserName = name;
            if(!name.contains("@")){
                newUserName+="@qq.com";
            }
            UserPo userPo = new UserPo();
            userPo.setIsSuperAdmin(2);
            userPo.setEmail(newUserName);
            userPo.setName(newUserName);
            userPo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userPo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userPo.setPassword(memberPo.getPassword());
            userPo.setRoleId(0);
            userPo.setInvitation(UUID.randomUUID().toString().replace("-","").substring(0,8));
            userPo.setRememberToken(memberPo.getRemember_token());
            userMapper.insert(userPo);
            Integer branchId = userPo.getId();
            List<MemberPo> memberPoList = memberService.getMemberIds(memberId);
            List<Integer> memberIds = memberPoList.stream().map(
                    memberPo1 -> {
                       return memberPo.getId();
                    }
            ).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(memberIds)) {
                memberMapper.batchUpdateBranchId(branchId, memberIds);
            }
            return true;
        }
        return false;
    }

}
