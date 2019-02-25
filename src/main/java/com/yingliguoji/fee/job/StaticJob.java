package com.yingliguoji.fee.job;


import com.google.common.collect.Maps;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.enums.GameType;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.*;
import com.yingliguoji.fee.po.js.GameSumPo;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class StaticJob {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private DividendMapper dividendMapper;

    @Autowired
    private ProxyFsZcLogMapper proxyFsZcLogMapper;

    @Value("${fireData}")
    private Integer fireData;
    private DefaultEventExecutorGroup group = new DefaultEventExecutorGroup(16);

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

    }


    @Scheduled(cron = "0 0 1 * * ?")

    public void execute() {
        try {
            log.info("start work");
            work();
            log.info(" end work");
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    private void work() {
        DateTime endDatetime = new DateTime().withHourOfDay(0).withMillisOfSecond(0).withMinuteOfHour(0);
        DateTime startDateTime = endDatetime.plusDays(-1);

        String start = startDateTime.toString("yyyy-MM-dd HH:mm:ss");
        String end = endDatetime.toString("yyyy-MM-dd HH:mm:ss");


        List<GameTypePo> gameTypePos = gameRecordMapper.queryBetClient(start, end);
        gameTypePos.forEach(gameTypePo -> {
            GameRecordPo gameRecordPo = new GameRecordPo();
            gameRecordPo.setStart(start);
            gameRecordPo.setEnd(end);
            gameRecordPo.setGameType(gameTypePo.getGameType());
            gameRecordPo.setMemberId(gameTypePo.getMemberId());
            GameSumPo gameSumPo = gameRecordMapper.querySum(gameRecordPo);
            if(gameSumPo.getTotalBetAmount().doubleValue() > 0){

                handleJs(gameTypePo.getMemberId(),gameTypePo.getGameType(),gameSumPo);
            }

        });

    }


    private void handleJs(Integer memberId, Integer gameType,GameSumPo gameSumPo) {

        //计算反水总值

        Integer kouchu = 0;
        MemberPo memberPo;

        Integer jsMemberId = memberId;

        BigDecimal sumFs = new BigDecimal("0.0");
        MemberPo currentPo = memberMapper.findById(memberId);
        if(currentPo == null || currentPo.getTop_id() == null){
            return;
        }
        if(currentPo.getIs_daili() == 0) {
            jsMemberId = currentPo.getTop_id();
        }
        while ((memberPo = memberMapper.findById(jsMemberId)) != null) {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(jsMemberId, RebateType.CS, gameType);
            log.info("memberId = {} , rebatePo = {}",jsMemberId, rebatePo);
            if (rebatePo != null) {
                //增加反水记录
                MemberPo beforeMemberPo = memberMapper.findById(jsMemberId);
                DividendPo dividendPo = new DividendPo();
                if(beforeMemberPo.getFs_money() == null){
                    beforeMemberPo.setFs_money(new BigDecimal(0));
                }
                dividendPo.setBeforeMoney(beforeMemberPo.getFs_money());
                if(rebatePo.getQuota() == null){
                    log.warn("getQuota is null ,memberId = {}",jsMemberId);
                    rebatePo.setQuota(0);
                    memberId = memberPo.getTop_id();
                    continue;
                }
                Integer getMoney = rebatePo.getQuota() - kouchu;

                BigDecimal money = gameSumPo.getTotalBetAmount().divide(new BigDecimal(fireData)).multiply(new BigDecimal(getMoney));
                dividendPo.setDescribe("返水-类别:" + maps.get(gameType));
                dividendPo.setMoney(money);
                dividendPo.setType(3);
                dividendPo.setMemberId(jsMemberId);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                dividendPo.setCreatedAt(timestamp);
                MemberPo updatePo = new MemberPo();
                updatePo.setId(memberId);
                updatePo.setFs_money(dividendPo.getMoney());
                memberMapper.update(updatePo);
                MemberPo afterPo = memberMapper.findById(jsMemberId);
                dividendPo.setAfterMoney(afterPo.getFs_money());
                dividendMapper.insert(dividendPo);
                kouchu = rebatePo.getQuota();
                sumFs = sumFs.add(money);
            }
            jsMemberId = memberPo.getTop_id();
        }

        //占成值 = 总输赢 - 反水值

        Integer jsZcMemberId = memberId;

        BigDecimal zcMoney = gameSumPo.getTotalNetAmount().add(sumFs.multiply(new BigDecimal(-1)));

        currentPo = memberMapper.findById(jsZcMemberId);

        if(currentPo.getIs_daili() == 0) {
            memberId = currentPo.getTop_id();
        }
        while ((memberPo = memberMapper.findById(jsZcMemberId)) != null) {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(jsZcMemberId, RebateType.ZC,0);
            log.info("memberId = {} , zcRebatePo = {}",jsZcMemberId, rebatePo);
            if (rebatePo != null) {
                //增加占成日志

                ProxyFsZcLogPo proxyFsZcLogPo = new ProxyFsZcLogPo();

                Integer quota = rebatePo.getQuota();
                if(quota == null || quota == 0){
                    continue;
                }

                BigDecimal money = zcMoney.multiply(new BigDecimal(quota)).divide(new BigDecimal(100));
                proxyFsZcLogPo.setQuota(quota);
                proxyFsZcLogPo.setMoney(money.doubleValue());
                proxyFsZcLogPo.setGameType(gameType);
                proxyFsZcLogPo.setMemberId(memberId);
                proxyFsZcLogPo.setInsertTime(System.currentTimeMillis());
                proxyFsZcLogPo.setStatTime(new DateTime().withTime(0,0,0,0)
                .plusDays(-1).getMillis());
                proxyFsZcLogPo.setRebateType(RebateType.ZC);
                proxyFsZcLogPo.setAgentId(jsMemberId);

                proxyFsZcLogMapper.insert(proxyFsZcLogPo);

            }
            jsMemberId = memberPo.getTop_id();
        }

    }

}
