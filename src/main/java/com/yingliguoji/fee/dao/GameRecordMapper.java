package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.GameRecordPo;
import com.yingliguoji.fee.po.GameTypePo;
import com.yingliguoji.fee.po.js.GameSumPo;
import com.yingliguoji.fee.po.js.MemberGamePo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GameRecordMapper {


    List<String> querySameBillNo();

    Integer selectByBillNo(String billNo);

    Integer deleteById(Integer id);

    List<GameTypePo> queryBetClient(@Param("start") String start , @Param("end") String end);


    GameSumPo querySum(GameRecordPo gameRecordPo);

    List<MemberGamePo> queryMemberGamePos(GameRecordPo queryPo);
}


