package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberClassifyPo {
    private Integer id;
    private Integer memberId;
    private Integer classifyId;
    private BigDecimal money;
    private Long feeTime;
    private Long createTime;
}
