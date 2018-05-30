package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class UnderPlayerRecordDataVo {

    private List<PlayerRecordTotalVo> details;
    private PlayerRecordTotalVo total;
}
