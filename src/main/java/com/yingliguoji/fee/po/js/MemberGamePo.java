package com.yingliguoji.fee.po.js;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberGamePo {
    private BigDecimal totalNetAmount;
    private BigDecimal totalValidBetAmount;
    private BigDecimal totalBetAmount;
    private Integer gameType;
    private Integer memberId;
}
