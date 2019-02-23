package com.yingliguoji.fee.service;


import com.yingliguoji.fee.controller.request.RebateSetDataRequestVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RebateWriteService {
    @Autowired
    private RebateMapper rebateMapper;


    @Autowired
    private MemberMapper memberMapper;


    public void settingData(RebateSetDataRequestVo requestVo) {

    }
}
