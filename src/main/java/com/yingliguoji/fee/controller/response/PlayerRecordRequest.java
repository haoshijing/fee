package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class PlayerRecordRequest {
    private Integer proxyId;
    private Integer start;
    private Integer end;
    private Integer type;
    private Integer branchId;
}
