package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import com.yingliguoji.fee.service.FeeService;
import com.yingliguoji.fee.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
@Slf4j
public class MemberController {

    @Value("${betId}")
    private Integer betId;

    @Autowired
    private UserService userService;

    @Autowired
    private FeeService feeService;

    @RequestMapping("/upgrade")
    public ApiResponse<Boolean> upgrade(Integer memberId){
        try {
            Boolean ret = userService.upgradeToAgent(memberId);
            return new ApiResponse(ret);
        }catch (Exception e){
            log.error("",e);
            return new ApiResponse(false);
        }
    }
}
