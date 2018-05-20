package com.yingliguoji.fee.controller.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PlayRecordRequest {
    private String tradeNo;
    private String reAmount;
    private String betAmount;
    private String userName;
    private Long betTime;
    private String netAmount;
    private String maxOdd;
    private String minOdd;
}
