package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.dao.DividendMapper;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.*;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Slf4j
public class SyncRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;


    private DefaultEventExecutorGroup defaultEventExecutor = new DefaultEventExecutorGroup(8, new DefaultThreadFactory("LotteryThread"));

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DividendMapper dividendMapper;

    @Value("${fireData}")
    private Integer fireData;

    public Integer syncData(PlayRecordRequest playRecordRequest) {

        if (checkRecord(playRecordRequest.getTradeNo())) {
            return 2;
        }
        MemberPo memberPo = memberMapper.findByName(playRecordRequest.getUserName());
        if (memberPo == null) {
            return 3;
        }
        if (playRecordRequest.getMinOdd() == null) {
            playRecordRequest.setMinOdd("0");
        }
        if (playRecordRequest.getMaxOdd() == null) {
            playRecordRequest.setMaxOdd("0");
        }
        GameRecordPo gameRecordPo = new GameRecordPo();
        gameRecordPo.setBillNo(playRecordRequest.getTradeNo());
        gameRecordPo.setMemberId(memberPo.getId());
        gameRecordPo.setName(memberPo.getName());
        gameRecordPo.setGameType(20000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(playRecordRequest.getBetTime());
            Timestamp timestamp = new Timestamp(date.getTime());
            gameRecordPo.setBetTime(timestamp);
        }catch (Exception e){
            log.error("",e);
        }

        gameRecordPo.setReAmount(new BigDecimal(playRecordRequest.getReAmount()));
        gameRecordPo.setBetAmount(new BigDecimal(playRecordRequest.getBetAmount()));
        gameRecordPo.setNetAmount(gameRecordPo.getBetAmount().add(gameRecordPo.getReAmount()));
        gameRecordPo.setCreated_at(new Timestamp(System.currentTimeMillis()).toString());
        gameRecordPo.setUpdated_at(new Timestamp(System.currentTimeMillis()).toString());
        getTotalFee(memberPo.getId(), playRecordRequest);
        return gameRecordMapper.insert(gameRecordPo);

    }

    public Boolean checkRecord(String tradeNo) {
        Integer count = gameRecordMapper.queryByTradeNo(tradeNo);
        return count > 0;
    }


    private void getTotalFee(Integer memberId, PlayRecordRequest playRecordRequest) {

        BigDecimal kouchu = new BigDecimal(0);
        BigDecimal betMoney = new BigDecimal(playRecordRequest.getBetAmount());
        MemberPo memberPo;
        while ((memberPo = memberMapper.findById(memberId)) != null) {
            BigDecimal tie = memberPo.getTie().add(kouchu.multiply(new BigDecimal(-1)));
            MemberPo beforeMemberPo = memberMapper.findById(memberId);
            DividendPo dividendPo = new DividendPo();
            dividendPo.setBeforeMoney(beforeMemberPo.getFs_money());
            BigDecimal money = (betMoney.divide(new BigDecimal(1000))).multiply(tie);
            dividendPo.setDescribe("返水-类别:彩票拉杆返水" + "金钱:" + money.doubleValue());
            dividendPo.setType(3);
            dividendPo.setMoney(money);
            dividendPo.setMemberId(memberId);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            dividendPo.setCreatedAt(timestamp);
            MemberPo updatePo = new MemberPo();
            updatePo.setId(memberId);
            updatePo.setFs_money(dividendPo.getMoney());
            memberMapper.update(updatePo);
            MemberPo afterPo = memberMapper.findById(memberId);
            dividendPo.setAfterMoney(afterPo.getFs_money());
            int insertRet = dividendMapper.insert(dividendPo);
            log.info("insertRet = {}", insertRet);
            kouchu = tie;
            memberId = memberPo.getTop_id();
        }

    }

}
