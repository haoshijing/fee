package com.yingliguoji.fee.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class PlayerRecordRequest {
    private List<Integer> memberIds;
    private Integer start;
    private Integer end;
}
