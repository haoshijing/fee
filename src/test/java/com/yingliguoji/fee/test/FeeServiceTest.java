/*
 * @(#) FeeServiceTest.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

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


    @Test
    public void testFs() {
        DateTime endDateTime = new DateTime().plusDays(-1).withTime(0, 0, 0, 0);
        DateTime startDateTime = endDateTime.plusDays(-1);

        fsZcService.backMoney(startDateTime, endDateTime);
    }
}
