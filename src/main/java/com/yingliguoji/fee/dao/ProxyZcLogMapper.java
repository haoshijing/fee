package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.ProxyZcLogPo;
import com.yingliguoji.fee.po.js.QueryProxyZcPo;
import com.yingliguoji.fee.po.js.ZcSumPo;

import java.util.List;

public interface ProxyZcLogMapper {
    Integer insert(ProxyZcLogPo proxyZcLogPo);

    List<ZcSumPo> queryZcList(QueryProxyZcPo queryProxyZcPo);
}
