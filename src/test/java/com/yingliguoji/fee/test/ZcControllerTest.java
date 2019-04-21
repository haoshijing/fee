/*
 * @(#) ZcControllerTest.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

import com.alibaba.fastjson.JSON;
import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.ZcController;
import com.yingliguoji.fee.controller.request.MemberBetRequest;
import com.yingliguoji.fee.controller.request.MoneyQueryRequest;
import com.yingliguoji.fee.controller.request.ZcQueryRequest;
import com.yingliguoji.fee.controller.request.ZcResponseData;
import com.yingliguoji.fee.controller.response.MemberBetResponse;
import com.yingliguoji.fee.controller.response.MoneyResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Slf4j
public class ZcControllerTest extends BaseApiTest {

    @Autowired
    private ZcController zcController;

    @Test
    public void testZcQuery() {
        ZcQueryRequest zcQueryRequest = new ZcQueryRequest();
        zcQueryRequest.setCurrentAgentId(2);
        zcQueryRequest.setName("");
        zcQueryRequest.setQueryType(1);
        ApiResponse<ZcResponseData> apiResponse = zcController.queryZcList(zcQueryRequest);
        Assert.assertTrue(apiResponse.getCode() == 200);
    }

    @Test
    public void testQueryMemberBetList(){
        MemberBetRequest request = new MemberBetRequest();
        request.setCurrentAgentId(133);
        request.setName("wu55555");
        ApiResponse<MemberBetResponse> response = zcController.queryMemberBetTotal(request);

        System.out.println(response.getData().getMemberBetDetailVoList().size() > 0);
    }

    @Test
    public void testQueryMoney() {
        MoneyQueryRequest moneyQueryRequest = new MoneyQueryRequest();
        moneyQueryRequest.setAgentId(0);
        moneyQueryRequest.setStart(new DateTime().plusDays(-333).getMillis());
        moneyQueryRequest.setEnd(System.currentTimeMillis());

        ApiResponse<List<MoneyResponseVo>> apiResponse = zcController.queryMoneyData(moneyQueryRequest);
        log.info(JSON.toJSONString(apiResponse.getData()));
        Assert.assertTrue(apiResponse.getCode() == 200);
    }
}
