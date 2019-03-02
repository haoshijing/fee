package com.yingliguoji.fee.controller.request;

import lombok.Data;

@Data
public class MemberBetRequest {

    private Integer gameType;
    private String name;
    private Integer currentAgentId;
    private Long startTime;
    private Long endTime;
}
