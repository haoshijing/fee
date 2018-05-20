package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.request.PlayRecordRequest;
import com.yingliguoji.fee.dao.DividendMapper;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.po.*;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;

@Repository
public class SyncRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;


    private DefaultEventExecutor defaultEventExecutor = new DefaultEventExecutor(new DefaultThreadFactory("LotteryThread"));

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
        Timestamp timestamp = new Timestamp(playRecordRequest.getBetTime() * 1000L);
        gameRecordPo.setBetTime(timestamp);
        gameRecordPo.setReAmount(new BigDecimal(playRecordRequest.getReAmount()));
        gameRecordPo.setBetAmount(new BigDecimal(playRecordRequest.getBetAmount()));
        gameRecordPo.setNetAmount(gameRecordPo.getBetAmount().add(gameRecordPo.getReAmount()));
        gameRecordPo.setCreated_at(new Timestamp(System.currentTimeMillis()).toString());
        gameRecordPo.setUpdated_at(new Timestamp(System.currentTimeMillis()).toString());
        defaultEventExecutor.submit(new YlLotteryFeeJob(memberPo.getId(), playRecordRequest));
        return gameRecordMapper.insert(gameRecordPo);

    }

    public Boolean checkRecord(String tradeNo) {
        Integer count = gameRecordMapper.queryByTradeNo(tradeNo);
        return count > 0;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private class YlLotteryFeeJob implements Callable<Integer> {
        private Integer memberId;
        private PlayRecordRequest playRecordRequest;

        @Override
        public Integer call() throws Exception {
            getTotalFee(memberId, playRecordRequest);
            return 0;
        }

        private void getTotalFee(Integer memberId, PlayRecordRequest playRecordRequest) {
            BigDecimal kouchu = new BigDecimal(0);
            MemberPo memberPo;
            BigDecimal betMoney = new BigDecimal(playRecordRequest.getBetAmount());
            BigDecimal feeMoney = new BigDecimal(playRecordRequest.getMaxOdd()).add(new BigDecimal(playRecordRequest.getMinOdd()).multiply(new BigDecimal(-1)));
            if(feeMoney.doubleValue() > 0) {
                while ((memberPo = memberMapper.findById(memberId)) != null) {
                    BigDecimal tie = memberPo.getTie().add(kouchu.multiply(new BigDecimal(-1)));
                    MemberPo beforeMemberPo = memberMapper.findById(memberId);
                    DividendPo log = new DividendPo();
                    log.setBeforeMoney(beforeMemberPo.getFs_money());
                    BigDecimal money = betMoney.multiply(feeMoney.divide(new BigDecimal(1000)).multiply(tie).divide(new BigDecimal(13)));
                    log.setDescribe("返水-类别:彩票拉杆返水" + "金钱:" + money.doubleValue());
                    log.setType(3);
                    log.setMemberId(memberId);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    log.setCreatedAt(timestamp);
                    MemberPo updatePo = new MemberPo();
                    updatePo.setId(memberId);
                    updatePo.setFs_money(log.getMoney());
                    memberMapper.update(updatePo);
                    MemberPo afterPo = memberMapper.findById(memberId);
                    log.setAfterMoney(afterPo.getFs_money());
                    dividendMapper.insert(log);
                    kouchu = tie;
                    memberId = memberPo.getTop_id();
                }
            }
        }
    }
}
