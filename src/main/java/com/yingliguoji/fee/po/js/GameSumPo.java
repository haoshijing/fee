package com.yingliguoji.fee.po.js;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GameSumPo {

    private BigDecimal totalNetAmount;
    private BigDecimal totalBetAmount;
}
