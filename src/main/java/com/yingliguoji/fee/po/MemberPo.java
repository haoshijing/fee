package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPo {
    private Integer id;
    private String name;
    private String real_name;
    private String remember_token;
    private String password;
    private BigDecimal money;
    private Integer status;
    private BigDecimal fs_money;
    private Integer top_id;
    private Integer is_daili;
    private Integer branch_id;
}
