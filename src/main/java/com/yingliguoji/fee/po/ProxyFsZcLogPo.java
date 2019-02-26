package com.yingliguoji.fee.po;


import lombok.Data;

@Data
public class ProxyFsZcLogPo {

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

     */
    private Integer gameType;

    private Double jsMoney;

    /**
     * 单位到分
     */
    private Double money;

    /**
     * 类别 1-反水 2-返现
     */
    private Integer rebateType;

    private String name;

    private Long statTime;

    private Long insertTime;
}
