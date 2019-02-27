/*
 * @(#) MemberControllerTest.java 2019-02-27
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.MemberController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author haoshijing
 * @version 2019-02-27
 */
public class MemberControllerTest extends BaseApiTest {

    @Autowired
    private MemberController memberController;

    @Test
    public void testQueryIds() {
        ApiResponse<List<Integer>> ids = memberController.queryIds(133);

        Assert.assertTrue(ids.getData().size() > 4);
    }
}
