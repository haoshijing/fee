package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.BranchMoneyLogPo;

import java.math.BigDecimal;

public interface BranchMoneyLogDao {
    int insert(BranchMoneyLogPo branchMoneyLogPo);

    BigDecimal querySum();
}
