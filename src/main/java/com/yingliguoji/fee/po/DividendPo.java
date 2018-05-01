package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class DividendPo {
    private Integer memberId;
    private Integer type;
    private String describe;
    private BigDecimal money;
    private BigDecimal beforeMoney;
    private BigDecimal afterMoney;
    private Integer status;
    private Timestamp createdAt;
}
