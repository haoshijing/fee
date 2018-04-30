package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.MemberClassifyPo;

import java.math.BigDecimal;

public interface MemberClassifyMapper {

    int insert(MemberClassifyPo classifyPo);

    int queryCount(MemberClassifyPo classifyPo);

    int update(MemberClassifyPo memberClassifyPo);

    BigDecimal sumMoney(Integer memberId);
}
