package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MoneyStaticsPo;
import com.yingliguoji.fee.po.money.TotalFeeMoneyPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeMapper {

    MoneyStaticsPo queryStaticsData(@Param("ids") List<Integer> ids, @Param("startTime")String start, @Param("endTime")String end);

    List<TotalFeeMoneyPo> queryRechargeMember(@Param("startTime") String start, @Param("endTime") String end);
}
