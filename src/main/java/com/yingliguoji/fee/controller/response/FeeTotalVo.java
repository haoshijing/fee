package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeTotalVo {
    private String name;
    private String realName;
    private BigDecimal totalBet;
    private BigDecimal reAmount;
    private BigDecimal realAmount;
    private Integer proportion;
    /**
     * 下属个数
     */
    private Integer underCount;
}
