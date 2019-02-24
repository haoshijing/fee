package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.RebatePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RebateMapper {

    RebatePo findByRebateTypeAndMemberIdAndGameType(@Param("memberId") Integer memberId,
                                                    @Param("rebateType") Integer rebateType,
                                                    @Param("gameType")Integer gameType);

    List<RebatePo> queryList(@Param("memberId") Integer memberId, @Param("rebateType") Integer rebateType);

    void insert(RebatePo rebatePo);

    void updateById(RebatePo updatePo);
}
