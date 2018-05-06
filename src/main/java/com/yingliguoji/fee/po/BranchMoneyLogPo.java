package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BranchMoneyLogPo {
    private Integer id;
    private BigDecimal money;
    private Integer branchId;
    private Long createTime;
    private Integer classifyId;
}
