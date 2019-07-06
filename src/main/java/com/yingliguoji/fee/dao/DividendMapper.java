package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.DividendPo;
import com.yingliguoji.fee.po.money.CzDividendPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DividendMapper {

    Integer insert(DividendPo dividendPo);

    List<DividendPo> queryList(Integer type);

    void batchInsertData(@Param("datas") List<DividendPo> updatePos);

    List<CzDividendPo> queryNeedBackUser(@Param("start") String start, @Param("end") String end);
}
