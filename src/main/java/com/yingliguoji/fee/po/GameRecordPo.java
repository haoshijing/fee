package com.yingliguoji.fee.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class GameRecordPo {
    private String billNo;
    private Integer memberId;
    private Integer gameType;
    private String name;
    private Integer playType;
    private BigDecimal reAmount;
    private BigDecimal betAmount;
    private Timestamp betTime;
}
