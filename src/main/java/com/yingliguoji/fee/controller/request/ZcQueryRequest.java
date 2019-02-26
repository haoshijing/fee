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
    private Integer currentAgentId;
    private String name;
    private Long start;
    private Long end;
}
