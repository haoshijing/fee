package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MoneyStaticsPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WithdrawMapper {
    MoneyStaticsPo queryStaticsData(@Param("ids") List<Integer> ids, @Param("startTime")String start, @Param("endTime")String end);
}
