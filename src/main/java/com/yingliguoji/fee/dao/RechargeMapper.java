package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MoneyStaticsPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeMapper {

    MoneyStaticsPo queryStaticsData(@Param("ids") List<Integer> ids);
}
