package com.yingliguoji.fee.po;


import lombok.Data;

@Data
public class ProxyZcLogPo {

    private Integer id;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 计算值
     */
    private Integer quota;
    /**
     * 会员id
     */
    private Integer memberId;
    /**
     * 输赢总额
     */
    private Double netAmount;

    private Double jsAmount;
    /**
     * 返水总额
     */
    private Double fsAmount;

    private Double betAmount;

    private Double validBetAmount;

    /**
     * 实际钱
     */
    private Double money;
    private String name;

    private Long statTime;

    private Integer gameType;

    private Long insertTime;
}
