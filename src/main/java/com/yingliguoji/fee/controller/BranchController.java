package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.response.FeeTotalVo;
import com.yingliguoji.fee.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BranchController {


    @Autowired
    private MemberService memberService;

    @RequestMapping("/queryBranchAgentFeetList")
    public ApiResponse<List<FeeTotalVo>> queryBranchAgentFeetList(Integer branchId, Integer start , Integer end){

        List<FeeTotalVo> vos = memberService.branchAgentVoList(branchId,start,end);
        return new ApiResponse<>(vos);
    }
    @RequestMapping("/queryBranchFeetList")
    public ApiResponse<List<FeeTotalVo>> queryBranchFeetList(Integer start ,Integer end){
        List<FeeTotalVo> vos = memberService.getBranchFeeList(start,end);
        return new ApiResponse<>(vos);
    }

}
