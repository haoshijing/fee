package com.yingliguoji.fee.service;

import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.GameRecordPo;
import com.yingliguoji.fee.po.MemberPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SyncRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    public Integer syncData(PlayRecordRequest playRecordRequest) {

        if(!checkRecord(playRecordRequest.getTradeNo())){
            return  2;
        }
        MemberPo memberPo = memberMapper.findByName(playRecordRequest.getUserName());
        if(memberPo == null){
            return  3;
        }
        GameRecordPo gameRecordPo  = new GameRecordPo();
        gameRecordPo.setBillNo(playRecordRequest.getTradeNo());
        gameRecordPo.setMemberId(memberPo.getId());
        gameRecordPo.setName(memberPo.getName());
        gameRecordPo.setPlayType(20000);
        gameRecordPo.se

        return gameRecordMapper.insert(ga)

    }

    public Boolean checkRecord(String tradeNo){
        Integer count = gameRecordMapper.queryByTradeNo(tradeNo);
        return  count >0;
    }
}
