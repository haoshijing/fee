package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.RebatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class MemberController {

    @Value("${betId}")
    private Integer betId;

    @Autowired
    private RebateMapper rebateMapper;

    @RequestMapping("/getFee")
    public Integer getFee(Integer memberId){
        RebatePo rebatePo = rebateMapper.find(memberId,betId);
        if(rebatePo != null){
            return rebatePo.getQuota();
        }
        return 0;
    }
}
