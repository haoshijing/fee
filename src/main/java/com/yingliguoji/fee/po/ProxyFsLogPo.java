/*
 * @(#) ProxyFsLogPo.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.po;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Data
public class ProxyFsLogPo {

    private Integer id;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 计算值
     */
    private Integer quota;
    /**
     * 会员id
     */
    private Integer memberId;

    /**
     * 实际钱
     */
    private Double money;

    private Double jsAmount;
    private String name;

    private Long statTime;

    private Long insertTime;

    private Integer gameType;
}
