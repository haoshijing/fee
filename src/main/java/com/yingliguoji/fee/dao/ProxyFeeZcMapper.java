package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.ProxyFeeZcLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface ProxyFeeZcMapper {
    int insert(ProxyFeeZcLog proxyFeeZcLog);

    BigDecimal queryFee(@Param("start") String start, @Param("end") String end, @Param("agentId") Integer agentId);
}
