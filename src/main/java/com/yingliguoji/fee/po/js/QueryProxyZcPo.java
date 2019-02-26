/*
 * @(#) QueryProxyZcPo.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.po.js;

import lombok.Data;

import java.util.List;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Data
public class QueryProxyZcPo {

    private List<Integer> agentIds;
    private Long startTime;
    private Long endTime;
}
