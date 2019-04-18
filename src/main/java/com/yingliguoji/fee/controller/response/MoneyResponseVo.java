package com.yingliguoji.fee.controller.response;

import lombok.Data;

@Data
public class MoneyResponseVo {

    private String totalPickUp;
    private Integer totalPickUpCount;
    private String totalPickupFee;
    private String totalDrawWith;
    private Integer totalDrawWithCount;
    private String totalDrawWithFee;

    private String name;
    private String realName;
}
