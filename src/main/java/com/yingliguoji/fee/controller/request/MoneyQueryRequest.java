package com.yingliguoji.fee.controller.request;

import lombok.Data;

@Data
public class MoneyQueryRequest {
    private Integer agentId;
    private Long start;
    private Long end;
    private String name;
}
