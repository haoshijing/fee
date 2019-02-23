package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.RebatePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RebateMapper {

    RebatePo find(@Param("userId")Integer userId,@Param("cid")Integer classId,@Param("percentage")Integer percentage);

    List<RebatePo> queryList(@Param("memberId") Integer memberId, @Param("rebateType") Integer rebateType);
}
