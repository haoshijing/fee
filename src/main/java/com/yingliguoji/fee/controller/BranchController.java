package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.controller.response.BranchAgentVo;
import com.yingliguoji.fee.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BranchController {


    @Autowired
    private MemberService memberService;

    @RequestMapping("/queryAllBranchList")
    public List<BranchAgentVo> queryAllBranchList(Integer branchId){

        List<BranchAgentVo> vos = memberService.branchAgentVoList(branchId,null,null);
        return vos;
    }
}
