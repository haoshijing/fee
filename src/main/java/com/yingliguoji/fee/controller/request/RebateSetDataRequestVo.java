package com.yingliguoji.fee.controller.request;


import lombok.Data;

import java.util.List;

@Data
public class RebateSetDataRequestVo {

    private Integer memberId;

    private Integer rebateType;

    private Boolean isAdmin;

    private List<RebateSettingVo> datas;

    @Data
    public static class RebateSettingVo{
        private Integer quato;
        private Integer gameType;
    }
}
