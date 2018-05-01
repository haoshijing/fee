package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.MemberPo;
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

    @Autowired
    private MemberMapper memberMapper;


    @RequestMapping("/getFee")
    public Integer getFee(String  name){
        MemberPo memberPo = memberMapper.findByName(name);
        if(memberPo == null){
            return  0;
        }
        RebatePo rebatePo = rebateMapper.find(memberPo.getId(),betId);
        if(rebatePo != null){
            return rebatePo.getQuota();
        }
        return 0;
    }
}
