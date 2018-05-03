package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.dao.*;
import com.yingliguoji.fee.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FeeService extends BaseService {

    @Autowired
    private ClassifyMapper classifyMapper;

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


    public void backFeeToAgent(Integer memberId, Long start, Long end) {
        List<ClassifyPo> classifyPoList = classifyMapper.selectAll();
        classifyPoList.forEach(classifyPo -> {
            String type = classifyPo.getType();
            String[] typeArr = type.split(",");
            List<Integer> gameTypes = Lists.newArrayList();
            for (String typeStr : typeArr) {
                gameTypes.add(Integer.valueOf(typeStr));
            }
            BigDecimal money = gameRecordService.getBetMoney(memberId, start, end, gameTypes);
            if(money != null && money.intValue() > 0) {
                beginToBack(classifyPo.getId(), memberId, end, money);
           }
        });

    }

    @Transactional
    public void beginToBack(Integer classifyId,Integer memberId,Long end,BigDecimal sumMoney){
        MemberClassifyPo memberClassifyPo = new MemberClassifyPo();
        memberClassifyPo.setClassifyId(classifyId);
        memberClassifyPo.setMemberId(memberId);
        memberClassifyPo.setFeeTime(end);
        Integer count = memberClassifyMapper.queryCount(memberClassifyPo);
        if(count == 0){
            handlerMemFee(memberId,classifyId,sumMoney);
        }
        //写入到日志
        MemberClassifyPo insertPo = new MemberClassifyPo();
        insertPo.setClassifyId(classifyId);
        insertPo.setMemberId(memberId);
        insertPo.setFeeTime(end);
        insertPo.setMoney(sumMoney);
        insertPo.setCreateTime(System.currentTimeMillis());
        memberClassifyMapper.insert(insertPo);
    }

    public void handlerMemFee(Integer memberId,Integer classifyId,BigDecimal sumMoney){
        Integer kouchu = 0;
        MemberPo memberPo;
        while ((memberPo = memberMapper.findById(memberId)) != null){
            RebatePo dataPo = rebateMapper.find(memberId,classifyId);
            if(dataPo != null){
                //增加反水记录
                MemberPo beforeMemberPo = memberMapper.findById(memberId);
                DividendPo log = new DividendPo();
                log.setBeforeMoney(beforeMemberPo.getMoney());
                Integer getMoney = dataPo.getQuota() -kouchu;
                BigDecimal money = sumMoney.divide(new BigDecimal(fireData)).multiply(new BigDecimal(getMoney));
                log.setDescribe("返水-类别:"+classifyId+"金钱:"+getMoney);
                log.setMoney(money);
                log.setType(3);
                log.setMemberId(memberId);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                log.setCreatedAt(timestamp);

                MemberPo updatePo = new MemberPo();
                updatePo.setId(memberId);
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
