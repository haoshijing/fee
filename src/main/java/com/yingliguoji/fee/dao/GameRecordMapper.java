package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.GameRecordPo;

import java.math.BigDecimal;
import java.util.Map;

public interface GameRecordMapper {

    BigDecimal getPlayerTotal(Map<String,Object> data);

    BigDecimal getValidBetTotal(Map<String,Object> data);

    Integer queryByTradeNo(String tradeNo);

    Integer insert(GameRecordPo gameRecordPo);
}


