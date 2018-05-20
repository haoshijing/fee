package com.yingliguoji.fee.controller;


import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.service.SyncRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SyncRecordController {

    @Autowired
    private SyncRecordService syncRecordService;
    @RequestMapping("/syncData")
    public ApiResponse<Integer> syncData(@RequestBody PlayRecordRequest playRecordRequest){
        log.info("playRecordRequest = {}",playRecordRequest);
        int ret = syncRecordService.syncData(playRecordRequest);
        return new ApiResponse<>(ret);
    }

    @RequestMapping("/checkTradeNo")
    public Boolean checkTradeNo(String tradeNo){
        if(StringUtils.isEmpty(tradeNo)){
            return false;
        }
        return syncRecordService.checkRecord(tradeNo);
    }
}
