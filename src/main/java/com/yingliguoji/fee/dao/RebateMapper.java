package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.RebatePo;
import org.apache.ibatis.annotations.Param;

public interface RebateMapper {

    RebatePo find(@Param("userId")Integer userId,@Param("cid")Integer classId);
}
