package com.yingliguoji.fee.service.money;
import com.yingliguoji.fee.controller.request.MoneyQueryRequest;
import com.yingliguoji.fee.controller.response.MoneyResponseVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RechargeMapper;
import com.yingliguoji.fee.dao.SystemConfigMapper;
import com.yingliguoji.fee.dao.WithdrawMapper;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.MoneyStaticsPo;
import com.yingliguoji.fee.po.SystemConfigPo;
import com.yingliguoji.fee.service.MemberService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMoneyService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private MemberService memberService;


    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public List<MoneyResponseVo>                   queryMoneyData(MoneyQueryRequest moneyQueryRequest) {

        List<Integer> agentIds = memberMapper.queryZcMember(moneyQueryRequest.getAgentId(), moneyQueryRequest.getName());

        if (moneyQueryRequest.getAgentId() != 0) {
            agentIds.add(0, moneyQueryRequest.getAgentId());
        }
        final SystemConfigPo configPo = systemConfigMapper.getConfig();
        return agentIds.stream().map(agentId->{
            String start = "";
            String end = "";
            if(moneyQueryRequest.getStart() != null){
                start = new DateTime(moneyQueryRequest.getStart()).toString("yyyy-MM-dd HH:mm:ss");
            }

            if(moneyQueryRequest.getEnd() != null){
                end = new DateTime(moneyQueryRequest.getEnd()).toString("yyyy-MM-dd HH:mm:ss");
            }
            MoneyResponseVo moneyResponseVo = queryMoney(agentId, start, end, configPo);
            return moneyResponseVo;
        }).collect(Collectors.toList());
    }

    private MoneyResponseVo queryMoney(Integer agentId, String start, String end, SystemConfigPo configPo) {
        MemberPo currentMemberPo = memberMapper.findById(agentId);
        List<MemberPo> memberPos =  memberService.getMemberIds(agentId,"");
        memberPos.add(memberMapper.findById(agentId));

        List<Integer> agentIds = memberPos.stream().map(MemberPo::getId).collect(Collectors.toList());
        MoneyResponseVo responseVo = new MoneyResponseVo();
        responseVo.setName(currentMemberPo.getName());
        responseVo.setRealName(currentMemberPo.getReal_name());
        if(!CollectionUtils.isEmpty(agentIds)){
            MoneyStaticsPo withDrawData =  withdrawMapper.queryStaticsData(agentIds, start, end);
            MoneyStaticsPo rechargeData = rechargeMapper.queryStaticsData(agentIds, start, end);
            if(withDrawData.getTotalMoney() == null){
                withDrawData.setTotalMoney(new BigDecimal("0"));
            }
            if(rechargeData.getTotalMoney() == null){
                rechargeData.setTotalMoney(new BigDecimal("0"));
            }
            responseVo.setTotalDrawWithCount(withDrawData.getTotalCount());
            responseVo.setTotalPickUpCount(rechargeData.getTotalCount());

            responseVo.setTotalPickUp(String.valueOf(rechargeData.getTotalMoney().doubleValue()));
            responseVo.setTotalDrawWith(String.valueOf(withDrawData.getTotalMoney().doubleValue()));

            BigDecimal czFeeRate = configPo.getCzFee() != null ? configPo.getCzFee() : new BigDecimal(0);
            BigDecimal tkFeeRate = configPo.getTkFee() != null ? configPo.getTkFee() : new BigDecimal(0);
            BigDecimal pickFee = rechargeData.getTotalMoney().multiply(czFeeRate).divide(new BigDecimal(100));

            BigDecimal withdrawFee = withDrawData.getTotalMoney().multiply(tkFeeRate).divide(new BigDecimal(1000));
            responseVo.setTotalPickupFee(String.valueOf(pickFee.doubleValue()));
            responseVo.setTotalDrawWithFee(String.valueOf(withdrawFee.doubleValue()));
        }else{
            responseVo.setTotalPickUp("0");
            responseVo.setTotalPickUpCount(0);
            responseVo.setTotalPickupFee("0");
            responseVo.setTotalDrawWithFee("0");
            responseVo.setTotalDrawWithCount(0);
            responseVo.setTotalDrawWith("0");

        }
        return responseVo;

    }
}
