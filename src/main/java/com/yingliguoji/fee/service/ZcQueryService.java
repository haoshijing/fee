/*
 * @(#) ZcQueryService.java 2019-02-26
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.request.ZcQueryRequest;
import com.yingliguoji.fee.controller.request.ZcResponseData;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.ProxyZcLogMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import com.yingliguoji.fee.po.js.QueryProxyZcPo;
import com.yingliguoji.fee.po.js.ZcSumPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haoshijing
 * @version 2019-02-26
 */
@Component
public class ZcQueryService {


    @Autowired
    private ProxyZcLogMapper proxyZcLogMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private MemberMapper memberMapper;

    public ZcResponseData queryZcList(ZcQueryRequest zcQueryRequest) {
        Integer queryType = zcQueryRequest.getQueryType();
        ZcResponseData responseData = new ZcResponseData();
        List<Integer> agentIds = memberMapper.queryZcMember(zcQueryRequest.getCurrentAgentId(), zcQueryRequest.getName());

        List<ZcResponseData.ZcResponseListData> list = agentIds.stream().map(agentId -> {
            ZcResponseData.ZcResponseListData zcResponseListData = new ZcResponseData.ZcResponseListData();
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(agentId, RebateType.ZC, 0);
            zcResponseListData.setQuota(0);
            if (rebatePo != null) {
                zcResponseListData.setQuota(rebatePo.getQuota());
            }
            MemberPo memberPo = memberMapper.findById(agentId);
            if (memberPo != null) {
                zcResponseListData.setName(memberPo.getName());
                zcResponseListData.setRealName(memberPo.getReal_name());
            } else {
                zcResponseListData.setName("unknow");
                zcResponseListData.setName("unknow");
            }
            zcResponseListData.setMemberId(agentId);
            return zcResponseListData;
        }).collect(Collectors.toList());
        if (zcQueryRequest.getCurrentAgentId() != 0) {
            agentIds.add(0, zcQueryRequest.getCurrentAgentId());
        }
        responseData.setZcResponseListData(list);
        QueryProxyZcPo queryProxyZcPo = new QueryProxyZcPo();
        queryProxyZcPo.setStartTime(zcQueryRequest.getStart());
        queryProxyZcPo.setEndTime(zcQueryRequest.getEnd());
        queryProxyZcPo.setAgentIds(Lists.newArrayList(agentIds));

        List<ZcSumPo> zcSumPos = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(agentIds)) {
            zcSumPos = proxyZcLogMapper.queryZcList(queryProxyZcPo);
        }
        zcSumPos.forEach(zcSumPo -> {
            Optional<ZcResponseData.ZcResponseListData> zcResponseListDataOptional =
                    list.stream().filter(zcResponseListData ->
                            zcResponseListData.getMemberId().equals(zcSumPo.getAgentId())).
                            findFirst();
            if (zcResponseListDataOptional.isPresent()) {
                ZcResponseData.ZcResponseListData data = zcResponseListDataOptional.get();
                data.resetData(zcSumPo, queryType);

            }
            if (!zcSumPo.getAgentId().equals(zcQueryRequest.getCurrentAgentId())) {
                responseData.addGamePo(zcSumPo, queryType);
            }
        });
        responseData.setData(Double.valueOf(new DecimalFormat("0.00").format(responseData.getData())));
        responseData.getGameZcDataList().forEach(gameZcData -> {
            gameZcData.setData(Double.valueOf(new DecimalFormat("0.00").format(gameZcData.getData())));
        });
        responseData.getZcResponseListData().forEach(responseListData -> {
            responseListData.setData(Double.valueOf(new DecimalFormat("0.00").format(responseListData.getData())));
            responseListData.getGameZcDataList().forEach(gameZcData1 -> {
                gameZcData1.setData(Double.valueOf(new DecimalFormat("0.00").format(gameZcData1.getData())));
            });
        });
        return responseData;
    }
}
