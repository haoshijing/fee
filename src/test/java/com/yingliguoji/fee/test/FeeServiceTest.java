/*
 * @(#) FeeServiceTest.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

import com.yingliguoji.fee.controller.FeeController;
import com.yingliguoji.fee.service.FsZcService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
public class FeeServiceTest extends BaseApiTest {

    @Autowired
    private FsZcService fsZcService;

    @Autowired
    private FeeController feeController;

    @Test
    public void testBcFs() {
        DateTime start = new DateTime(1555171200000L);
        DateTime end = new DateTime(1555257600000L);

        fsZcService.bcBack(start, end);
    }


    @Test
    public void testBackFee() {

        feeController.back(1561824000000L, 1561910400000L);
    }

}
