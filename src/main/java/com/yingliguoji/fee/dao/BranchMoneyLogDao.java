package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.BranchMoneyLogPo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface BranchMoneyLogDao {
    int insert(BranchMoneyLogPo branchMoneyLogPo);


    BigDecimal querySum(@Param("branchId") Integer branchId,
                        @Param("classifyId") Integer classifyId,
                        @Param("start") Long start,
                        @Param("end") Long end);
}
