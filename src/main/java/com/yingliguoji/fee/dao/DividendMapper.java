package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.DividendPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DividendMapper {

    Integer insert(DividendPo dividendPo);

    List<DividendPo> queryList(Integer type);

    void batchInsertData(@Param("datas") List<DividendPo> updatePos);
}
