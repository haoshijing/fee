package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class FeeService {

    private Logger logger = LoggerFactory.getLogger(FeeService.class);

    @Autowired
    private GameRecordService gameRecordService;

    @Autowired
    private MemberClassifyMapper memberClassifyMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private DividendMapper dividendMapper;

    @Value("${fireData}")
    private Integer fireData;

    public void backFeeToAgent(Integer memberId, Integer start, Integer end) {

    }

    public BigDecimal getTotalFee(Integer branchId, Integer type, List<Integer> memberIds, Integer start, Integer end) {
        BigDecimal total = new BigDecimal(0);

        return total;
    }

    public void beginToBack(Integer classifyId, Integer memberId, Integer end, BigDecimal sumMoney,boolean needReAdd) {

    }

    public void handlerMemFee(Integer memberId, Integer classifyId, BigDecimal sumMoney) {
//        Integer kouchu = 0;
//        MemberPo memberPo;
//        Integer branchId = 0;
//        MemberPo currentPo = memberMapper.findById(memberId);
//        if(currentPo == null || currentPo.getTop_id() == null){
//            return;
//        }
//        if(currentPo.getIs_daili() == 0) {
//            memberId = currentPo.getTop_id();
//        }
//        while ((memberPo = memberMapper.findById(memberId)) != null) {
//            RebatePo dataPo = rebateMapper.find(memberId, classifyId, 1);
//            logger.info("memberId = {}",memberId);
//            if (dataPo != null) {
//                //增加反水记录
//                if(branchId == 0){
//                    branchId = memberPo.getBranch_id();
//                }
//                MemberPo beforeMemberPo = memberMapper.findById(memberId);
//                DividendPo log = new DividendPo();
//                if(beforeMemberPo.getFs_money() == null){
//                    beforeMemberPo.setFs_money(new BigDecimal(0));
//                }
//                log.setBeforeMoney(beforeMemberPo.getFs_money());
//                if(dataPo.getQuota() == null){
//                    logger.warn("getQuota is null ,memberId = {}",memberId);
//                    dataPo.setQuota(0);
//                    memberId = memberPo.getTop_id();
//                    continue;
//                }
//                Integer getMoney = dataPo.getQuota() - kouchu;
//                ClassifyPo classifyPo = classifyMapper.getById(classifyId);
//                String classifyName = "";
//                if(classifyPo != null){
//                    classifyName = classifyPo.getName();
//                }
//                BigDecimal money = sumMoney.divide(new BigDecimal(fireData)).multiply(new BigDecimal(getMoney));
//                log.setDescribe("返水-类别:" + classifyName);
//                log.setMoney(money);
//                log.setType(3);
//                log.setMemberId(memberId);
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                log.setCreatedAt(timestamp);
//                MemberPo updatePo = new MemberPo();
//                updatePo.setId(memberId);
//                updatePo.setFs_money(log.getMoney());
//                memberMapper.update(updatePo);
//                MemberPo afterPo = memberMapper.findById(memberId);
//                log.setAfterMoney(afterPo.getFs_money());
//                dividendMapper.insert(log);
//                kouchu = dataPo.getQuota();
//            }
//            memberId = memberPo.getTop_id();
//        }

    }

    public void updateReAmount() {
        gameRecordService.updateReAmount();
    }

}
