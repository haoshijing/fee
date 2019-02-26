/*
 * @(#) ProxyFsLogMapper.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.ProxyFsLogPo;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
public interface ProxyFsLogMapper {
    Integer insert(ProxyFsLogPo proxyFsLogPo);
}
