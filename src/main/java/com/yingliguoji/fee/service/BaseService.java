package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.po.ClassifyPo;

import java.util.List;

public class BaseService {
    protected List<Integer> getGameTypes(ClassifyPo classifyPo) {
        String type = classifyPo.getSmallType();
        String[] typeArr = type.split(",");
        List<Integer> gameTypes = Lists.newArrayList();
        for (String typeStr : typeArr) {
            gameTypes.add(Integer.valueOf(typeStr));
        }
        return gameTypes;
    }

}
