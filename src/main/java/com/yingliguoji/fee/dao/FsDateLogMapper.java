package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.FsDateLogPo;

public interface FsDateLogMapper {

    int insert(FsDateLogPo fsDateLogPo);

    FsDateLogPo queryLastLog();
}
