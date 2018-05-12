package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminMoneyLogPo {
    private Integer id;
    private BigDecimal money;
    private String name;
    private String realName;
    private Long createTime;
}
