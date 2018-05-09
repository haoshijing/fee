package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.response.FeeTotalVo;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.UserMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.po.MemberPo;

import com.yingliguoji.fee.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private GameRecordService gameRecordMapper;

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FeeService feeService;

    public MemberService() {
    }

    public List<MemberPo> getMemberIds(Integer proxyId) {
        MemberPo queryPo = new MemberPo();
        queryPo.setTop_id(proxyId);
        List<MemberPo> totals = Lists.newArrayList();
        List<MemberPo> list = memberMapper.selectList(queryPo);

        list.forEach(memberPo -> {
            if (memberPo.getIs_daili() == 1) {
                totals.addAll(getMemberIds(memberPo.getId()));
            }
            totals.add(memberPo);
        });
        return totals;
    }

    public List<MemberPo> getAllUnderProxy(Integer branchId) {
        MemberPo queryPo = new MemberPo();
        queryPo.setBranch_id(branchId);
        queryPo.setIs_daili(1);
        queryPo.setTop_id(0);
        return memberMapper.selectList(queryPo);
    }


    public List<FeeTotalVo> getBranchFeeList(Integer start, Integer end) {
        List<UserPo> userPos = userMapper.getAllBranch();
        List<FeeTotalVo> feeTotalVos = userPos.stream().map(userPo -> {
            List<FeeTotalVo> feeTotalVos1 = branchAgentVoList(userPo.getId(),start,end);
            FeeTotalVo feeTotalVo = new FeeTotalVo();
            feeTotalVo.setReAmount(new BigDecimal(0));
            feeTotalVo.setRealAmount(new BigDecimal(0));
            feeTotalVo.setTotalBet(new BigDecimal(0));
            feeTotalVo.setName(userPo.getEmail());
            feeTotalVo.setRealName(userPo.getName());
            feeTotalVos1.forEach(feeTotalVo1 -> {
                feeTotalVo.setReAmount(feeTotalVo1.getReAmount().add(feeTotalVo.getReAmount()));
                feeTotalVo.setRealAmount(feeTotalVo1.getRealAmount().add(feeTotalVo.getRealAmount()));
                feeTotalVo.setTotalBet(feeTotalVo1.getReAmount().add(feeTotalVo.getTotalBet()));
            });
            return feeTotalVo;

        }).collect(Collectors.toList());
        return feeTotalVos;
    }


    public List<FeeTotalVo> branchAgentVoList(Integer branchId, Integer start, Integer end) {
        List<MemberPo> proxyList = getAllUnderProxy(branchId);
        final List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        UserPo userPo = userMapper.selectById(branchId);
        return proxyList.stream().map(
                memberPo -> {
                    FeeTotalVo branchAgentVo = new FeeTotalVo();
                    branchAgentVo.setName(memberPo.getName());
                    branchAgentVo.setRealName(memberPo.getReal_name());
                    List<MemberPo> memberPos = getMemberIds(memberPo.getId());
                    List<Integer> memberIds = memberPos.stream().map(memberPo1 -> {
                        return memberPo1.getId();
                    }).collect(Collectors.toList());
                    BigDecimal totalBet = new BigDecimal(0);
                    if (!CollectionUtils.isEmpty(memberIds)) {
                        totalBet = gameRecordMapper.getTotalValidBet(memberIds, Lists.newArrayList(), start, end);
                    }

                    BigDecimal reAmountMoney = new BigDecimal(0);
                    if (!CollectionUtils.isEmpty(memberIds)) {
                        reAmountMoney = gameRecordMapper.getReAmountTotal(memberIds, Lists.newArrayList(), start, end);
                    }
                    branchAgentVo.setReAmount(reAmountMoney);
                    BigDecimal feeTotal = new BigDecimal(0);
                    if (!CollectionUtils.isEmpty(memberIds)) {
                        feeTotal = feeService.getTotalFee(memberPo.getId(), 1, memberIds, classifyPos, start, end);
                        feeTotal = feeTotal.multiply(new BigDecimal(-1));
                    }

                    branchAgentVo.setTotalBet(totalBet);
                    if (userPo.getProportion() == null) {
                        userPo.setProportion(0);
                    }
                    branchAgentVo.setProportion(userPo.getProportion());
                    branchAgentVo.setRealAmount(reAmountMoney.multiply(new BigDecimal(-1)).add(feeTotal).
                            multiply(new BigDecimal(userPo.getProportion())).
                            divide(new BigDecimal(100)));
                    return branchAgentVo;
                }
        ).collect(Collectors.toList());
    }

}
