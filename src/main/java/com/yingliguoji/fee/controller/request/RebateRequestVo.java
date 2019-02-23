package com.yingliguoji.fee.controller.request;

import lombok.Data;

@Data
public class RebateRequestVo {

    /**
     * 会员id
     */
    private Integer memberId;

    /**
     * 类别
     */
    private Integer rebateType;

    /**
     * 是不是管理员
     */
    private Boolean isAdmin;
}
