package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
    private DividendMapper dividendMapper;

    @Value("${fireData}")
    private Integer fireData;


    @Transactional
    public void calMemberBet(Integer memberId) {
        List<ClassifyPo> classifyPoList = classifyMapper.selectAll();

        classifyPoList.forEach(classifyPo -> {

            BigDecimal money = getBetMoney(memberId,null,null,classifyPo);
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
               if(sumMoney != null && sumMoney.intValue() >= fireData){
                //触发反水
                handlerMemFee(memberId,classifyPo.getId());
                //增加一条扣除总数的记录
                MemberClassifyPo newPo = new MemberClassifyPo();
                newPo.setClassifyId(classifyPo.getId());
                newPo.setMemberId(memberId);
                newPo.setType(2);
                newPo.setCreateTime(System.currentTimeMillis());
                newPo.setMoney(new BigDecimal(0-fireData));
                memberClassifyMapper.insert(newPo);

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
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;

    }

    public BigDecimal getBetMoney(Integer memberId, Integer start, Integer end, ClassifyPo classifyPo) {

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

        BigDecimal money = gameRecordMapper.getValidBetTotal(params);
        if(money == null){
            return new BigDecimal(0);
        }
        return  money;

    }

    public void handlerMemFee(Integer memberId,Integer classifyId){
        Integer kouchu = 0;
        MemberPo memberPo;
        while ((memberPo = memberMapper.findById(memberId)) != null){
            RebatePo dataPo = rebateMapper.find(memberId,classifyId);
            if(dataPo != null){
                //增加反水记录
                MemberPo beforeMemberPo = memberMapper.findById(memberId);
                DividendPo log = new DividendPo();
                log.setBeforeMoney(beforeMemberPo.getMoney());
                log.setDescribe("返水:"+(dataPo.getQuota() -kouchu));
                log.setMoney(new BigDecimal(dataPo.getQuota() -kouchu));
                log.setType(3);
                log.setMemberId(memberId);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                log.setCreatedAt(timestamp);

                MemberPo updatePo = new MemberPo();
                updatePo.setId(memberId);
                updatePo.setMoney(log.getMoney());
                updatePo.setFs_money(log.getMoney());

                memberMapper.update(updatePo);

                MemberPo afterPo = memberMapper.findById(memberId);

                log.setAfterMoney(afterPo.getMoney());

                dividendMapper.insert(log);
                kouchu = dataPo.getQuota();
            }
            memberId = memberPo.getTop_id();
        }

    }
}
