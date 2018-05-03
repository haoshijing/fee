package com.yingliguoji.fee.service;

import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.GameRecordPo;
import com.yingliguoji.fee.po.MemberPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Repository
public class SyncRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    public Boolean syncData(PlayRecordRequest playRecordRequest) {

        if(!checkRecord(playRecordRequest.getTradeNo())){
            return  false;
        }
        MemberPo memberPo = memberMapper.findByName(playRecordRequest.getUserName());
        if(memberPo == null){
            return  false;
        }
        GameRecordPo gameRecordPo  = new GameRecordPo();
        gameRecordPo.setBillNo(playRecordRequest.getTradeNo());
        gameRecordPo.setMemberId(memberPo.getId());
        gameRecordPo.setName(memberPo.getName());
        gameRecordPo.setPlayType(20000);
        Timestamp timestamp = new Timestamp(playRecordRequest.getBetTime());
        gameRecordPo.setBetTime(timestamp);
        gameRecordPo.setReAmount(new BigDecimal(playRecordRequest.getReAmount()));
        gameRecordPo.setBetAmount(new BigDecimal(playRecordRequest.getBetAmount()));
        return gameRecordMapper.insert(gameRecordPo) > 0;

    }

    public Boolean checkRecord(String tradeNo){
        Integer count = gameRecordMapper.queryByTradeNo(tradeNo);
        return  count >0;
    }
}
