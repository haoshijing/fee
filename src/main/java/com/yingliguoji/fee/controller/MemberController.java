package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.request.MemberUpgradeRequest;
import com.yingliguoji.fee.controller.response.BranchAgentVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import com.yingliguoji.fee.service.FeeService;
import com.yingliguoji.fee.service.MemberService;
import com.yingliguoji.fee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/")
@RestController
public class MemberController {

    @Value("${betId}")
    private Integer betId;

    @Autowired
    private UserService userService;

    @Autowired
    private FeeService feeService;


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

    @RequestMapping("/upgrade")
    public ApiResponse<Boolean> upgrade(Integer memberId){
        try {
            Boolean ret = userService.upgradeToAgent(memberId);
            return new ApiResponse(ret);
        }catch (Exception e){
            return new ApiResponse(false);
        }

    }



    @RequestMapping("/back")
    public Boolean testBackToMember(Integer memberId){
        Long start = 1525190400000L;
        Long end = 1525276800000L;
        feeService.backFeeToAgent(memberId,start,end);
        return true;
    }
}
