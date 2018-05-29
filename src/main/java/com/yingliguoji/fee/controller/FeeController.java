package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author haoshijing
 * @version 2018年05月29日 12:16
 **/

@RestController
public class FeeController {

    @Autowired
    private FeeService feeService;
    @RequestMapping("/back")
    public String back(Integer memberId,Integer cid,Integer end,String money){
        feeService.beginToBack(cid,memberId,end,new BigDecimal(money),true);
        return "ok";
    }
}
