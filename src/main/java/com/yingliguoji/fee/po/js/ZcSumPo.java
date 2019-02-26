/*
 * @(#) ZcSumpo.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.po.js;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Data
public class ZcSumPo {
    private BigDecimal ycAmount;
    private BigDecimal backAmount;
    private Integer gameType;
    private Integer agentId;
}
