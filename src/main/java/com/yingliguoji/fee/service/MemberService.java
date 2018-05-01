package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.response.BranchAgentVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.MemberPo;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    private DefaultEventExecutor defaultEventExecutor;

    public MemberService(){

    }

    public List<MemberPo> getMemberIds(Integer proxyId) {
        MemberPo queryPo = new MemberPo();
        queryPo.setTop_id(proxyId);
        List<MemberPo> totals = Lists.newArrayList();
        List<MemberPo> list  = memberMapper.selectList(queryPo);

        list.forEach(memberPo -> {
            if(memberPo.getIs_daili() == 1){
                totals.addAll(getMemberIds(memberPo.getId()));
            }
            totals.add(memberPo);
        });
        return totals;
    }

    public List<MemberPo> getAllUnderProxy(Integer branchId){
        MemberPo queryPo = new MemberPo();
        queryPo.setBranch_id(branchId);
        queryPo.setIs_daili(1);
        return memberMapper.selectList(queryPo);
    }

    public List<BranchAgentVo> branchAgentVoList(Integer branchId) {
        List<MemberPo> proxyList = getMemberIds(branchId);
        return proxyList.stream().map(
                memberPo -> {
                    BranchAgentVo branchAgentVo = new BranchAgentVo();
                    return branchAgentVo;
                }
        ).collect(Collectors.toList());
    }
}
