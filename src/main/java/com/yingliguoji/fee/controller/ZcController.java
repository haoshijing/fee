/*
 * @(#) ZcController.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.request.MemberBetRequest;
import com.yingliguoji.fee.controller.request.MoneyQueryRequest;
import com.yingliguoji.fee.controller.request.ZcQueryRequest;
import com.yingliguoji.fee.controller.request.ZcResponseData;
import com.yingliguoji.fee.controller.response.MemberBetResponse;
import com.yingliguoji.fee.controller.response.MoneyResponseVo;
import com.yingliguoji.fee.service.MemberBetService;
import com.yingliguoji.fee.service.ZcQueryService;
import com.yingliguoji.fee.service.money.MemberMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author haoshijing
 * @version 2019-02-26
 */

@RestController
@RequestMapping("/zc")
public class ZcController {

    @Autowired
    private ZcQueryService zcQueryService;

    @Autowired
    private MemberBetService memberBetService;

    @Autowired
    private MemberMoneyService memberMoneyService;

    @PostMapping("/queryZcList")
    public ApiResponse<ZcResponseData> queryZcList(@RequestBody ZcQueryRequest zcQueryRequest) {
        ZcResponseData zcResponseData = zcQueryService.queryZcList(zcQueryRequest);
        return new ApiResponse<>(zcResponseData);
    }

    @PostMapping("/queryMemberBetTotal")
    public ApiResponse<MemberBetResponse> queryMemberBetTotal(@RequestBody MemberBetRequest request){
        MemberBetResponse memberBetResponse = memberBetService.queryMemberBetTotal(request);
        return new ApiResponse<>(memberBetResponse);
    }

    public ApiResponse<List<MoneyResponseVo>> queryMoneyData(@RequestBody MoneyQueryRequest moneyQueryRequest){
        List<MoneyResponseVo> moneyResponseVos = memberMoneyService.queryMoneyData(moneyQueryRequest);
        return new ApiResponse<>(moneyResponseVos);
    }
}
