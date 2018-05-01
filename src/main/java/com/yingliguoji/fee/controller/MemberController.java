package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import com.yingliguoji.fee.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/")
@RestController
public class MemberController {

    @Value("${betId}")
    private Integer betId;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private MemberMapper memberMapper;


    @RequestMapping("/getFee")
    public Integer getFee(String name) {
        MemberPo memberPo = memberMapper.findByName(name);
        if (memberPo == null) {
            return 0;
        }
        RebatePo rebatePo = rebateMapper.find(memberPo.getId(), betId);
        if (rebatePo != null) {
            return rebatePo.getQuota();
        }
        return 0;
    }

    @RequestMapping

    @RequestMapping("/memberList")
    public String test(Integer memberId) {
        List<MemberPo> memberPoList = memberService.getMemberIds(278);
        memberPoList.forEach(memberPo -> {
            System.out.println("memberId = [" + memberPo.getId() + "]");
        });
        return "ok";
    }
}
