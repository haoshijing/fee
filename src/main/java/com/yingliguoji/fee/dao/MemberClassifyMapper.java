package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.MemberClassifyPo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface MemberClassifyMapper {

    Integer insert(MemberClassifyPo classifyPo);

    Integer queryCount(MemberClassifyPo classifyPo);

    Integer update(MemberClassifyPo memberClassifyPo);

    BigDecimal sumMoney(@Param("memberId") Integer memberId, @Param("cid") Integer classifyId);

    Long seletcMinFee(Integer memberId);
}
