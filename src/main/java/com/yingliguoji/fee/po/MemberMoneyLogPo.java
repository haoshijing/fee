package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberMoneyLogPo {
    private Integer id;
    private BigDecimal money;
    private Integer memberId;
    private Long createTime;
    private BigDecimal beforeMoney;
    private BigDecimal afterMoney;
    private String memo;
    private Integer type;
}
