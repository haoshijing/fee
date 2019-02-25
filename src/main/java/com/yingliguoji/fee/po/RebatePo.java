package com.yingliguoji.fee.po;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RebatePo {
    private Integer id;
    /**
     * 游戏类别
     */
    private Integer gameType;

    /**
     * 代理id
     */
    private Integer memberId;

    /**
     * 数额
     */
    private Integer quota;

    /**
     * 类别 com.yingliguoji.fee.enums.RebateType
     */
    private Integer rebateType;

    /**
     *
     */
    private Long insertTime;
    private Long lastUpdateTime;
}
