package com.yingliguoji.fee.controller.request;

import lombok.Data;

@Data
public class PlayRecordRequest {
    private String tradeNo;
    private String reAmount;
    private String betAmount;
    private String userName;
    private Long betTime;
}
