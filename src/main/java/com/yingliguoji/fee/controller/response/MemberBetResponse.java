package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class MemberBetResponse {

    private List<MemberBetDetailVo> memberBetDetailVoList;

    @Data
    public static class MemberBetDetailVo {
        private Integer gameType;
        private String name;
        private String gameName;
        private Double netAmount;
        private Double validBetAmount;
        private Double betAmount;
    }

    private Double totalNetAmount;
    private Double totalValidNetAmount;
    private Double totalBetAmount;
}
