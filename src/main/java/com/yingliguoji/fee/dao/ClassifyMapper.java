package com.yingliguoji.fee.dao;


import com.yingliguoji.fee.po.ClassifyPo;

import java.util.List;

public interface ClassifyMapper {

    List<ClassifyPo> selectAll();

    ClassifyPo getById(Integer id);
}
