package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class GameRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;


    @Value("${fireData}")
    private Integer fireData;


    public BigDecimal getMoney(Integer memberId, Integer start, Integer end, List<Integer> gameTypes) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("memberId", memberId);

        if(start != null){
            Long startTime = (start*1000L);
            Timestamp startTimestamp = new Timestamp(startTime);
            params.put("startTime", startTimestamp.toString());
        }
        if(end != null) {
            Long endTime = (end*1000L);
            params.put("endTime", new Timestamp(endTime).toString());
        }
        params.put("gameTypes", gameTypes);

        BigDecimal money = gameRecordMapper.getPlayerTotal(params);
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;

    }

    public BigDecimal getBetMoney(Integer memberId, Long start, Long end,List<Integer> gameTypes) {
        Map<String, Object> params = Maps.newHashMap();
        List<Integer> memberIds = Lists.newArrayList();
        memberIds.add(memberId);
        params.put("memberIds", memberIds);
        if(start != null){
            Timestamp startTimestamp = new Timestamp(start);
            params.put("startTime", startTimestamp.toString());
        }
        if(end != null){
            Timestamp endTimestamp = new Timestamp(end);
            params.put("endTime", endTimestamp.toString());
        }

        params.put("gameTypes", gameTypes);

        BigDecimal money = gameRecordMapper.getValidBetTotal(params);
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;

    }
}
