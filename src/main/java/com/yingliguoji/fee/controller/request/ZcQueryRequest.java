/*
 * @(#) ZcQueryRequest.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.controller.request;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Data
public class ZcQueryRequest {
    //1-占成 2-盈亏 3-有效投注 4- 总投注 5- 输赢总额
    private Integer queryType = 1;
    private Integer currentAgentId;
    private String name;
    private Long start;
    private Long end;
}
