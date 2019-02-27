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
    //1-盈亏 2-有效投注 3-总投注 4- 返水 5- 占成抽成 6-输赢总额
    private Integer queryType = 1;
    private Integer currentAgentId;
    private String name;
    private Long start;
    private Long end;
}
