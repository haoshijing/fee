package com.yingliguoji.fee.po;

import lombok.Data;

@Data
public class ProxyFeeZcLog {

    private Integer id;
    private Integer agentId;
    private Integer memberId;
    private Double totalMoney;
    private Double totalFee;
    private Integer quota;
    /**
     * 1-充值 2-提现
     */
    private Integer type;
    private String name;
    private Long statTime;

    private Long insertTime;

}
