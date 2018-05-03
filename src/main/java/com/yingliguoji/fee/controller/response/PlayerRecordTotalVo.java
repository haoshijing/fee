package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class PlayerRecordTotalVo {
    private Integer memberId;
    private List<ClassiFyItem> classiFyItemList;


    @Data
    public static class ClassiFyItem{
        private Integer classiFyId;
        private Double money;
        private String name;
    }
}
