package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.service.BackService;
import com.yingliguoji.fee.service.FsZcService;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fee")
public class FeeController {
    @Autowired
    private FsZcService fsZcService;

    @Autowired
    private BackService backService;

    @GetMapping("/back")
    public ApiResponse<Boolean> back(Long startTime, Long endTime) {
        for (Long t = startTime; t < endTime; t += DateUtils.MILLIS_PER_DAY) {
            fsZcService.backMoney(new DateTime(t), new DateTime(t + DateUtils.MILLIS_PER_DAY));
        }
        return new ApiResponse<>(true);
    }

    @GetMapping("/backMoney")
    public ApiResponse<Boolean> backMoney() {
        backService.backMoney();
        return new ApiResponse<>(true);
    }

}
