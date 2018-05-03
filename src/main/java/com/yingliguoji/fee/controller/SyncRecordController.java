package com.yingliguoji.fee.controller;


import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.service.SyncRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncRecordController {

    @Autowired
    private SyncRecordService syncRecordService;
    @RequestMapping("/syncData")
    public Boolean syncData(@RequestBody PlayRecordRequest playRecordRequest){
        Boolean ret = syncRecordService.syncData(playRecordRequest);
        return ret;
    }

    @RequestMapping("/checkTradeNo")
    public Boolean checkTradeNo(String tradeNo){
        if(StringUtils.isEmpty(tradeNo)){
            return false;
        }
        return syncRecordService.checkRecord(tradeNo);
    }
}
