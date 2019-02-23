package com.yingliguoji.fee.controller.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RebateVo {


    /**
     * 数额
     */
    private Integer quato;

    /**
     * 游戏类别
     */
    private Integer gameType;

    private String gameName;

}
