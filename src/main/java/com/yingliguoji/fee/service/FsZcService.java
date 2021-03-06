package com.yingliguoji.fee.service;

import com.google.common.collect.Maps;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.enums.GameType;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.*;
import com.yingliguoji.fee.po.js.GameSumPo;
import com.yingliguoji.fee.po.money.CzDividendPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FsZcService {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private DividendMapper dividendMapper;

    @Autowired
    private ProxyZcLogMapper proxyZcLogMapper;

    @Autowired
    private ProxyFsLogMapper proxyFsLogMapper;

    @Value("${fireData}")
    private Integer fireData;

    static Map<Integer, String> maps = Maps.newConcurrentMap();

    static {
        maps.put(0, "占成");
        maps.put(GameType.ZR, "真人");
        maps.put(GameType.DZ, "电子");
        maps.put(GameType.CP, "彩票");
        maps.put(GameType.TY, "体育");
        maps.put(GameType.DJ, "电竞");
        maps.put(GameType.BY, "捕鱼");
        maps.put(GameType.QP, "棋牌");
        maps.put(8, "充值占成");

    }


    @Transactional(rollbackFor = Exception.class)
    public void backMoney(DateTime startDate, DateTime endDate) {
        String start = startDate.toString("yyyy-MM-dd HH:mm:ss");
        String end = endDate.toString("yyyy-MM-dd HH:mm:ss");

        List<GameTypePo> gameTypePos = gameRecordMapper.queryBetClient(start, end);


        handlerData(gameTypePos,start, end, endDate,"NORMAL");
        gameRecordMapper.updateFs(start, end);

    }

    public void handlerCzFee(String start, String end, DateTime endDate) {
        List<CzDividendPo> czDividendPos = dividendMapper.queryNeedBackUser(start, end);
        czDividendPos.forEach(dividendPo -> {
            jsBackZc(dividendPo.getMemberId(), dividendPo.getMoney(), endDate);
        });
    }
    public void bcBack(DateTime startTime, DateTime endTime) {
        String start = startTime.toString("yyyy-MM-dd HH:mm:ss");
        String end = endTime.toString("yyyy-MM-dd HH:mm:ss");

        List<GameTypePo> gameTypePos = gameRecordMapper.queryBcBetClient(start, end);
        handlerData(gameTypePos,start, end, endTime, "BC");
    }




    private void handleJs(Integer memberId, Integer gameType, GameSumPo gameSumPo, DateTime endDate) {
        //计算反水总值
        BigDecimal sumFs = jsFs(memberId, gameType, gameSumPo.getTotalValidBetAmount(), endDate);
        if (sumFs != null) {
            jsZc(memberId, gameType, gameSumPo, sumFs, endDate);
        }
    }

    private void jsBackZc(Integer memberId, BigDecimal sumFs, DateTime endDate) {
        //占成值 = 总输赢 - 反水值
        Integer jsZcMemberId = memberId;
        log.info("memberId = {},sumFs = {}", memberId, sumFs);
        BigDecimal zcMoney = sumFs.multiply(new BigDecimal(-1));
        MemberPo memberPo;
        MemberPo currentPo = memberMapper.findById(jsZcMemberId);

        if (currentPo.getIs_daili() == 0) {
            jsZcMemberId = currentPo.getTop_id();
        }
        while ((memberPo = memberMapper.findById(jsZcMemberId)) != null) {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(jsZcMemberId, RebateType.ZC, 0);
            log.info("memberId = {} , zcRebatePo = {}", jsZcMemberId, rebatePo);
            if (rebatePo != null) {
                //增加占成日志
                ProxyZcLogPo proxyZcLogPo = new ProxyZcLogPo();
                Integer quota = rebatePo.getQuota();
                if (quota == null || quota == 0) {
                    log.warn("getQuota is null ,memberId = {}", jsZcMemberId);
                    rebatePo.setQuota(0);
                    jsZcMemberId = memberPo.getTop_id();
                    continue;
                }
                BigDecimal money = zcMoney.multiply(new BigDecimal(quota)).divide(new BigDecimal(100));
                proxyZcLogPo.setQuota(quota);
                proxyZcLogPo.setMoney(money.doubleValue());
                proxyZcLogPo.setMemberId(memberId);
                proxyZcLogPo.setFsAmount(sumFs.doubleValue());
                proxyZcLogPo.setGameType(8);
                proxyZcLogPo.setBetAmount(sumFs.doubleValue());
                proxyZcLogPo.setValidBetAmount(0.0);
                proxyZcLogPo.setName(memberPo.getName());
                proxyZcLogPo.setInsertTime(System.currentTimeMillis());
                proxyZcLogPo.setStatTime(endDate.getMillis());
                proxyZcLogPo.setAgentId(jsZcMemberId);
                proxyZcLogPo.setJsAmount(zcMoney.doubleValue());
                proxyZcLogPo.setNetAmount(0.0);
                try {
                    proxyZcLogMapper.insert(proxyZcLogPo);
                } catch (Exception e) {
                    log.error("proxyZcLogPo = {}", proxyZcLogPo, e);

                }
            }
            jsZcMemberId = memberPo.getTop_id();
        }
    }


    private void jsZc(Integer memberId, Integer gameType, GameSumPo gameSumPo, BigDecimal sumFs, DateTime endDate) {
        //占成值 = 总输赢 - 反水值
        Integer jsZcMemberId = memberId;
        BigDecimal totalNetAmount = gameSumPo.getTotalNetAmount();
        log.info("memberId = {},totalNetAmount = {}", memberId, totalNetAmount);
        BigDecimal totalBetAmount = gameSumPo.getTotalBetAmount();
        BigDecimal totalValidBetAmount = gameSumPo.getTotalValidBetAmount();
        totalNetAmount = totalNetAmount.multiply(new BigDecimal(-1));
        BigDecimal zcMoney = totalNetAmount.add(sumFs.multiply(new BigDecimal(-1)));
        MemberPo memberPo;
        MemberPo currentPo = memberMapper.findById(jsZcMemberId);

        if (currentPo.getIs_daili() == 0) {
            jsZcMemberId = currentPo.getTop_id();
        }
        while ((memberPo = memberMapper.findById(jsZcMemberId)) != null) {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(jsZcMemberId, RebateType.ZC, 0);
            log.info("memberId = {} , zcRebatePo = {}", jsZcMemberId, rebatePo);
            if (rebatePo != null) {
                //增加占成日志
                ProxyZcLogPo proxyZcLogPo = new ProxyZcLogPo();
                Integer quota = rebatePo.getQuota();
                if (quota == null || quota == 0) {
                    log.warn("getQuota is null ,memberId = {}", jsZcMemberId);
                    rebatePo.setQuota(0);
                    jsZcMemberId = memberPo.getTop_id();
                    continue;
                }
                BigDecimal money = zcMoney.multiply(new BigDecimal(quota)).divide(new BigDecimal(100));
                proxyZcLogPo.setQuota(quota);
                proxyZcLogPo.setMoney(money.doubleValue());
                proxyZcLogPo.setMemberId(memberId);
                proxyZcLogPo.setFsAmount(sumFs.doubleValue());
                proxyZcLogPo.setGameType(gameType);
                proxyZcLogPo.setBetAmount(totalBetAmount.doubleValue());
                proxyZcLogPo.setValidBetAmount(totalValidBetAmount.doubleValue());
                proxyZcLogPo.setName(memberPo.getName());
                proxyZcLogPo.setInsertTime(System.currentTimeMillis());
                proxyZcLogPo.setStatTime(endDate.getMillis());
                proxyZcLogPo.setAgentId(jsZcMemberId);
                proxyZcLogPo.setJsAmount(zcMoney.doubleValue());
                proxyZcLogPo.setNetAmount(totalNetAmount.doubleValue());
                try {
                    proxyZcLogMapper.insert(proxyZcLogPo);
                } catch (Exception e) {
                    log.error("proxyZcLogPo = {}", proxyZcLogPo, e);

                }
            }
            jsZcMemberId = memberPo.getTop_id();
        }
    }

    private BigDecimal jsFs(Integer memberId, Integer gameType, BigDecimal betAmount, DateTime endDate) {
        MemberPo memberPo;
        Integer detectQuota = 0;
        Integer jsMemberId = memberId;

        BigDecimal sumFs = new BigDecimal("0.0");
        MemberPo currentPo = memberMapper.findById(memberId);
        if (currentPo == null || currentPo.getTop_id() == null) {
            return null;
        }
        if (currentPo.getIs_daili() == 0) {
            jsMemberId = currentPo.getTop_id();
        }
        while ((memberPo = memberMapper.findById(jsMemberId)) != null) {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(jsMemberId, RebateType.CS, gameType);
            log.info("memberId = {} , rebatePo = {}", jsMemberId, rebatePo);
            if (rebatePo != null) {
                //增加反水记录
                MemberPo beforeMemberPo = memberMapper.findById(jsMemberId);
                DividendPo dividendPo = new DividendPo();
                if (beforeMemberPo.getMoney() == null) {
                    beforeMemberPo.setMoney(new BigDecimal(0));
                }
                dividendPo.setBeforeMoney(beforeMemberPo.getMoney());
                if (rebatePo.getQuota() == null || rebatePo.getQuota() == 0) {
                    log.warn("getQuota is null ,memberId = {}", jsMemberId);
                    rebatePo.setQuota(0);
                    jsMemberId = memberPo.getTop_id();
                    continue;
                }
                Integer getMoney = rebatePo.getQuota() - detectQuota;

                BigDecimal money = betAmount.divide(new BigDecimal(fireData)).multiply(new BigDecimal(getMoney));
                dividendPo.setDescribe("返水-类别:" + maps.get(gameType));
                dividendPo.setMoney(money);
                dividendPo.setType(3);
                dividendPo.setMemberId(jsMemberId);
                Timestamp timestamp = new Timestamp(endDate.getMillis());
                dividendPo.setCreatedAt(timestamp);
                MemberPo updatePo = new MemberPo();
                updatePo.setId(jsMemberId);
                updatePo.setMoney(dividendPo.getMoney());
                memberMapper.update(updatePo);
                MemberPo afterPo = memberMapper.findById(jsMemberId);
                dividendPo.setAfterMoney(afterPo.getMoney());
                if (dividendPo.getMoney().doubleValue() > 0) {
                    dividendMapper.insert(dividendPo);
                }
                detectQuota = rebatePo.getQuota();

                ProxyFsLogPo proxyFsLogPo = new ProxyFsLogPo();
                Integer quota = rebatePo.getQuota();
                if (quota == null || quota == 0) {
                    continue;
                }

                proxyFsLogPo.setQuota(getMoney);
                proxyFsLogPo.setMoney(money.doubleValue());
                proxyFsLogPo.setGameType(gameType);
                proxyFsLogPo.setMemberId(memberId);
                proxyFsLogPo.setName(memberPo.getName());
                proxyFsLogPo.setJsAmount(betAmount.doubleValue());
                proxyFsLogPo.setInsertTime(System.currentTimeMillis());
                proxyFsLogPo.setStatTime(endDate.getMillis());

                proxyFsLogPo.setAgentId(jsMemberId);
                try {
                    proxyFsLogMapper.insert(proxyFsLogPo);
                } catch (Exception e) {
                    log.error("proxyFsLogPo = {}", proxyFsLogPo, e);
                }
                sumFs = sumFs.add(money);
            }
            jsMemberId = memberPo.getTop_id();
        }
        return sumFs;
    }


    private void handlerData(List<GameTypePo> gameTypePos, String start, String end, DateTime endTime, String type){
        gameTypePos.forEach(gameTypePo -> {
            GameRecordPo gameRecordPo = new GameRecordPo();
            gameRecordPo.setStart(start);
            gameRecordPo.setEnd(end);
            gameRecordPo.setGameType(gameTypePo.getGameType());
            gameRecordPo.setMemberId(gameTypePo.getMemberId());
            GameSumPo gameSumPo = null;
            if(StringUtils.equals(type,"NORMAL")) {
                gameSumPo =   gameRecordMapper.querySum(gameRecordPo);
            }else{
                gameSumPo =   gameRecordMapper.queryBcSum(gameRecordPo);
            }
            if (gameSumPo.getTotalBetAmount().doubleValue() > 0) {
                handleJs(gameTypePo.getMemberId(), gameTypePo.getGameType(), gameSumPo, endTime);
            }

        });


    }
}
