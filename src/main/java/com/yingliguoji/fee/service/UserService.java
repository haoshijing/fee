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

import java.util.List;
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
    public Boolean upgradeToAgent(MemberUpgradeRequest request) {
        Integer memberId = request.getMemberId();
        MemberPo memberPo = memberMapper.findById(memberId);
        if(memberPo != null && memberPo.getIs_daili() == 1){
            String name = memberPo.getName();
            String newUserName = name;
            if(!name.contains("@")){
                newUserName+="@qq.com";
            }
            UserPo userPo = new UserPo();
            userPo.setIsSuperAdmin(2);
            userPo.setName(newUserName);
            Integer branchId = userPo.getId();

            List<MemberPo> memberPoList = memberService.getMemberIds(memberId);
            List<Integer> memberIds = memberPoList.stream().map(
                    memberPo1 -> {
                       return memberPo.getId();
                    }
            ).collect(Collectors.toList());
            memberMapper.batchUpdateBranchId(branchId,memberIds);
            return true;
        }
        return false;
    }

}
