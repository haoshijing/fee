package com.yingliguoji.fee.po.money;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ZcMoneySumPo {
    private BigDecimal totalMoney;
    private BigDecimal totalFee;
    private Integer agentId;
}
