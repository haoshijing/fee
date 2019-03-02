package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class GameRecordPo {
    private String billNo;
    private Integer memberId;
    private Integer gameType;
    private String name;
    private Integer playType;
    private BigDecimal reAmount;
    private BigDecimal netAmount;
    private BigDecimal betAmount;
    private Timestamp betTime;
    private BigDecimal validBetAmount;
    private String start;
    private String end;
    private List<Integer> memberIds;

}
