/*
 * @(#) TestBase.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.yingliguoji.fee.ApplicationStarter.class, properties = "spring.profiles.active=dev")
public class BaseApiTest {
}
