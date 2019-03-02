package com.yingliguoji.fee.service;

import com.yingliguoji.fee.controller.request.MemberBetRequest;
import com.yingliguoji.fee.controller.response.MemberBetResponse;
import com.yingliguoji.fee.po.MemberPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberBetService {

    @Autowired
    private MemberService memberService;

    public MemberBetResponse queryMemberBetTotal(MemberBetRequest request) {
        Integer currentAgentId = request.getCurrentAgentId();
        String name = request.getName();
        List<MemberPo> memberIds =  memberService.getMemberIds(currentAgentId,name);
        MemberBetResponse response = new MemberBetResponse();
        return response;
    }
}
