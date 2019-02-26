/*
 * @(#) ZcController.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.controller;

import com.yingliguoji.fee.ApiResponse;
import com.yingliguoji.fee.controller.request.ZcQueryRequest;
import com.yingliguoji.fee.controller.request.ZcResponseData;
import com.yingliguoji.fee.service.ZcQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haoshijing
 * @version 2019-02-26
 */

@RestController
@RequestMapping("/zc")
public class ZcController {

    @Autowired
    private ZcQueryService zcQueryService;

    @PostMapping("/queryZcList")
    public ApiResponse<ZcResponseData> queryZcList(@RequestBody ZcQueryRequest zcQueryRequest) {
        ZcResponseData zcResponseData = zcQueryService.queryZcList(zcQueryRequest);
        return new ApiResponse<>(zcResponseData);
    }
}
