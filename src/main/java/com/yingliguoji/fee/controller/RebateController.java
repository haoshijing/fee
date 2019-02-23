package com.yingliguoji.fee.controller;


import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.request.RebateRequestVo;
import com.yingliguoji.fee.controller.request.RebateSetDataRequestVo;
import com.yingliguoji.fee.controller.response.RebateVo;
import com.yingliguoji.fee.service.RebateQueryService;
import com.yingliguoji.fee.service.RebateQueryService;
import com.yingliguoji.fee.service.RebateWriteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/rebate")
public class RebateController {

    @Autowired
    private RebateQueryService rebateQueryService;

    @Autowired
    private RebateWriteService rebateWriteService;


    @PostMapping("/queryListForUser")
    public ApiResponse<List<RebateVo>> queryRebateList(@RequestBody RebateRequestVo rebateRequestVo){
        if(rebateRequestVo.getRebateType() == null || rebateRequestVo.getMemberId() == null){
            return new ApiResponse<>(500, "参数错误");
        }
        try {
            List<RebateVo> rebateVos = rebateQueryService.queryRebateList(rebateRequestVo);
            return new ApiResponse<>(rebateVos);
        }catch (Exception e){
            return new ApiResponse<>(501,e.getMessage());
        }
    }


    @PostMapping("/settingData")
    public ApiResponse<Boolean> settingData(@RequestBody RebateSetDataRequestVo requestVo){
        if(requestVo.getMemberId() == null ||
                requestVo.getRebateType() == null ||
                CollectionUtils.isEmpty(requestVo.getDatas())){
            return new ApiResponse<>(500, "参数错误");
        }
        try {
            rebateWriteService.settingData(requestVo);
            return new ApiResponse<>(true);
        }catch (Exception e){
            return new ApiResponse<>(501,e.getMessage());
        }
    }


}
