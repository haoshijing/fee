package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.service.FsZcService;
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

    @GetMapping("/back")
    public ApiResponse<Boolean> back(Long startTime, Long endTime) {
        fsZcService.backMoney(new DateTime(startTime), new DateTime(endTime));
        return new ApiResponse<>(true);
    }
}
