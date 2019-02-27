/*
 * @(#) MemberController.java 2019-02-27
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoshijing
 * @version 2019-02-27
 */

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/queryUnderMember")
    public ApiResponse<List<Integer>> queryIds(Integer agentId) {
        List<MemberPo> memberPos = memberService.getMemberIds(agentId);
        List<Integer> ids = memberPos.stream().map(memberPo -> memberPo.getId()).collect(Collectors.toList());
        return new ApiResponse<>(ids);
    }
}
