package com.yingliguoji.fee.service;

import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.ProxyFeeZcLog;
import com.yingliguoji.fee.po.RebatePo;
import com.yingliguoji.fee.po.SystemConfigPo;
import com.yingliguoji.fee.po.money.TotalFeeMoneyPo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class MoneyFeeService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private ProxyFeeZcMapper proxyFeeZcMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public void handlerFee(DateTime startTime, DateTime endTime) {
        String start = startTime.toString("yyyy-MM-dd HH:mm:ss");
        String end = endTime.toString("yyyy-MM-dd HH:mm:ss");
        List<TotalFeeMoneyPo> totalFeeMoneyPos = rechargeMapper.queryRechargeMember(start, end);
        totalFeeMoneyPos.forEach(totalFeeMoneyPo -> {
            jsZc(totalFeeMoneyPo.getMemberId(), totalFeeMoneyPo.getMoney(), 1, endTime);
        });

        List<TotalFeeMoneyPo> totalDrawingFeeMoneyPos = withdrawMapper.queryDrawingMember(start, end);
        totalDrawingFeeMoneyPos.forEach(totalFeeMoneyPo -> {
            jsZc(totalFeeMoneyPo.getMemberId(), totalFeeMoneyPo.getMoney(), 2, endTime);
        });

    }

    private void jsZc(Integer memberId, BigDecimal totalMoney, Integer type, DateTime endDate) {
        //占成值 = 总输赢 - 反水值
        Integer jsZcMemberId = memberId;
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
                ProxyFeeZcLog proxyFeeZcLog = new ProxyFeeZcLog();
                Integer quota = rebatePo.getQuota();
                if (quota == null || quota == 0) {
                    log.warn("getQuota is null ,memberId = {}", jsZcMemberId);
                    rebatePo.setQuota(0);
                    jsZcMemberId = memberPo.getTop_id();
                    continue;
                }
                final SystemConfigPo configPo = systemConfigMapper.getConfig();
                BigDecimal czFeeRate = configPo.getCzFee() != null ? configPo.getCzFee() : new BigDecimal(0);
                BigDecimal tkFeeRate = configPo.getTkFee() != null ? configPo.getTkFee() : new BigDecimal(0);
                BigDecimal totalFee;
                if (type == 1) {
                    totalFee = totalMoney.multiply(czFeeRate).divide(new BigDecimal(100));

                } else {
                    totalFee = totalMoney.multiply(tkFeeRate).divide(new BigDecimal(1000));
                }
                totalFee = totalFee.multiply(new BigDecimal(quota)).divide(new BigDecimal(100));
                ;
                proxyFeeZcLog.setQuota(quota);
                proxyFeeZcLog.setTotalMoney(totalMoney.doubleValue());
                proxyFeeZcLog.setMemberId(memberId);
                proxyFeeZcLog.setType(type);
                proxyFeeZcLog.setTotalFee(totalFee.doubleValue());
                proxyFeeZcLog.setName(memberPo.getName());
                proxyFeeZcLog.setInsertTime(System.currentTimeMillis());
                proxyFeeZcLog.setStatTime(endDate.getMillis());
                proxyFeeZcLog.setAgentId(jsZcMemberId);
                try {
                    proxyFeeZcMapper.insert(proxyFeeZcLog);
                } catch (Exception e) {
                    log.error("proxyFeeZcLog = {}", proxyFeeZcLog, e);

                }
            }
            jsZcMemberId = memberPo.getTop_id();
        }
    }

}
