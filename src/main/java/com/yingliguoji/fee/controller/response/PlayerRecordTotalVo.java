package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class PlayerRecordTotalVo {
    private Integer memberId;
    private String name;
    private String realName;
    private List<ClassiFyItem> classiFyItemList;


    @Data
    public static class ClassiFyItem{
        private Integer classiFyId;
        private Double money = 0.0;
        private Double betMoney = 0.0;
        private String name;
    }
}
