package com.yingliguoji.fee.po;

import lombok.Data;

@Data
public class RebatePo {
    private Integer id;
    private Integer class_id;
    private Integer user_id;
    private Integer percentage;
    private Integer quota;
}
