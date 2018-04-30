package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class GameRecordService {

    @Autowired
    private GameRecordMapper gameRecordMapper;

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private MemberClassifyMapper memberClassifyMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private MemberMoneyLogMapper memberMoneyLogMapper;


    public void calMemberBet(Integer memberId) {
        List<ClassifyPo> classifyPoList = classifyMapper.selectAll();

        classifyPoList.forEach(classifyPo -> {

            BigDecimal money = getMoney(memberId,null,null,classifyPo);
            MemberClassifyPo memberClassifyPo = new MemberClassifyPo();
            memberClassifyPo.setClassifyId(classifyPo.getId());
            memberClassifyPo.setMemberId(memberId);
            memberClassifyPo.setType(1);

            Integer count = memberClassifyMapper.queryCount(memberClassifyPo);
            if(count == 0){
                memberClassifyPo.setMoney(money);
                memberClassifyPo.setCreateTime(System.currentTimeMillis());
                memberClassifyMapper.insert(memberClassifyPo);
            }else{
                memberClassifyPo.setMoney(money);
                memberClassifyPo.setLastUpdateTime(System.currentTimeMillis());
                memberClassifyMapper.update(memberClassifyPo);
            }

            //算一下总数
            Integer classifyId = classifyPo.getId();

            BigDecimal sumMoney = memberClassifyMapper.sumMoney(memberId,classifyId);
            if(sumMoney != null && sumMoney.intValue() >= 10){
                //触发反水
                handlerMemFee(memberId,classifyPo.getId());
                //增加一条扣除总数的记录
                MemberClassifyPo newPo = new MemberClassifyPo();
                newPo.setClassifyId(classifyPo.getId());
                newPo.setMemberId(memberId);
                memberClassifyPo.setType(2);
                memberClassifyPo.setMoney(new BigDecimal(-10));
                memberClassifyMapper.insert(memberClassifyPo);

            }

        });
    }


    public BigDecimal getMoney(Integer memberId, Integer start, Integer end, ClassifyPo classifyPo) {

        String type = classifyPo.getType();
        String[] typeArr = type.split(",");
        List<Integer> gameTypes = Lists.newArrayList();
        for (String typeStr : typeArr) {
            gameTypes.add(Integer.valueOf(typeStr));
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("memberId", memberId);
        params.put("startTime", start);
        params.put("endTime", end);
        params.put("gameTypes", gameTypes);

        BigDecimal money = gameRecordMapper.getPlayerTotal(params);
        return  money;

    }

    private void handlerMemFee(Integer memberId,Integer classifyId){
        Integer kouchu = 0;
        MemberPo memberPo;
        while ((memberPo = memberMapper.findById(memberId)) != null){
            //

            RebatePo dataPo = rebateMapper.find(memberId,classifyId);

            if(dataPo != null){
                //增加反水记录
                MemberPo beforeMemberPo = memberMapper.findById(memberId);
                MemberMoneyLogPo log = new MemberMoneyLogPo();
                log.setBeforeMoney(beforeMemberPo.getMoney());
                log.setMemo("代理返现金额:"+(dataPo.getQuota() -kouchu));
                log.setMoney(new BigDecimal(dataPo.getQuota() -kouchu));
                log.setType(1);

                MemberPo updatePo = new MemberPo();
                updatePo.setId(memberId);
                updatePo.setMoney(log.getMoney());
                updatePo.setFs_money(log.getMoney());

                memberMapper.update(updatePo);

                MemberPo afterPo = memberMapper.findById(memberId);

                log.setAfterMoney(afterPo.getMoney());

                memberMoneyLogMapper.insert(log);
                kouchu = dataPo.getQuota();
            }
            memberId = memberPo.getTop_id();
        }

    }
}
