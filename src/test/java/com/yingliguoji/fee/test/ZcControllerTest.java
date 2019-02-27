/*
 * @(#) ZcControllerTest.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.ZcController;
import com.yingliguoji.fee.controller.request.ZcQueryRequest;
import com.yingliguoji.fee.controller.request.ZcResponseData;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
public class ZcControllerTest extends BaseApiTest {

    @Autowired
    private ZcController zcController;

    @Test
    public void testZcQuery() {
        ZcQueryRequest zcQueryRequest = new ZcQueryRequest();
        zcQueryRequest.setCurrentAgentId(0);
        zcQueryRequest.setName("");
        zcQueryRequest.setStart(1551024000000L);
        zcQueryRequest.setEnd(new DateTime().withTime(0, 0, 0, 0).getMillis());
        zcQueryRequest.setQueryType(2);
        ApiResponse<ZcResponseData> apiResponse = zcController.queryZcList(zcQueryRequest);
        Assert.assertTrue(apiResponse.getCode() == 200);
    }
}
