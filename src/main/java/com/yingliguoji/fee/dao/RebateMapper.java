package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.RebatePo;

public interface RebateMapper {

    RebatePo findByMemAndClassify(RebatePo rebatePo);
}
