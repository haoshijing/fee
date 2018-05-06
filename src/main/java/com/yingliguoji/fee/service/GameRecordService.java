package com.yingliguoji.fee.service;

import com.yingliguoji.fee.dao.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class GameRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;


    @Value("${fireData}")
    private Integer fireData;

    public BigDecimal getReAmountTotal(List<Integer> memberIds, List<Integer> gameTypes, Integer start, Integer end) {
        QueryDataVo queryDataVo = getQueryPo(memberIds,start,end,gameTypes);
        BigDecimal money = gameRecordMapper.getReAmountTotal(queryDataVo.getMemberIds(),
                queryDataVo.getStartTime(),
                queryDataVo.getEndTime(),
                queryDataVo.getGameTypes());
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;

    }

    public BigDecimal getTotalValidBet(List<Integer> memberIds,List<Integer> gameTypes, Integer start, Integer end) {
        String startTime = null;
        String endTime = null;
        if(start != null){
            Timestamp startTimestamp = new Timestamp(start*1000L);
            startTime =  startTimestamp.toString();
        }
        if(end != null) {
            startTime =  new Timestamp(end*1000L).toString();
        }
        BigDecimal money = gameRecordMapper.getValidBetTotal(memberIds,startTime,endTime,gameTypes);
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;
    }

    private QueryDataVo getQueryPo(List<Integer> memberIds, Integer start, Integer end, List<Integer> gameTypes) {
        QueryDataVo queryDataVo = new QueryDataVo();
        String startTime = null;
        String endTime = null;
        if(start != null){
            Long startTimeMill = (start*1000L);
            Timestamp startTimestamp = new Timestamp(startTimeMill);
            startTime =  startTimestamp.toString();
        }
        if(end != null) {
            Long endTimeMill = (end*1000L);
            startTime =  new Timestamp(endTimeMill).toString();
        }
        queryDataVo.setEndTime(endTime);
        queryDataVo.setStartTime(startTime);
        queryDataVo.setGameTypes(gameTypes);
        queryDataVo.setMemberIds(memberIds);
        return queryDataVo;
    }

    public void updateReAmount() {
        gameRecordMapper.updateReAmount();
    }

    @Data
    private class QueryDataVo{
        private List<Integer> memberIds;
        private String startTime;
        private String endTime;
        private List<Integer> gameTypes;
    }
}
