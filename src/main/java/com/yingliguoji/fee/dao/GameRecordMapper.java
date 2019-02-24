package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.GameRecordPo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GameRecordMapper {

    Integer queryByTradeNo(String tradeNo);

    Integer insert(GameRecordPo gameRecordPo);

    BigDecimal getValidBetTotal(@Param("memberIds") List<Integer> memberIds, @Param("start") String start, @Param("end") String end, @Param("gameTypes") List<Integer> gameTypes);

    BigDecimal getReAmountTotal(@Param("memberIds") List<Integer> memberIds, @Param("start") String start, @Param("end") String end, @Param("gameTypes") List<Integer> gameTypes);

    void updateReAmount();

    void clearData(String date);

    List<String> querySameBillNo();

    Integer selectByBillNo(String billNo);

    Integer deleteById(Integer id);

    List<Integer> queryBetClient();
}


