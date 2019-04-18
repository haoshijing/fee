package com.yingliguoji.fee.service.money;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.request.MoneyQueryRequest;
import com.yingliguoji.fee.controller.response.MoneyResponseVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberMoneyService {
    public List<MoneyResponseVo> queryMoneyData(MoneyQueryRequest moneyQueryRequest) {
        return  Lists.newArrayList();
    }
}
