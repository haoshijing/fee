package com.yingliguoji.fee.po.money;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalFeeMoneyPo {
    private Integer memberId;
    private BigDecimal money;

}
