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
        return memberMapper.selectList(queryPo);
    }


    public List<FeeTotalVo> getBranchFeeList(Integer start, Integer end) {
        List<UserPo> userPos = userMapper.getAllBranch();
        final List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        List<FeeTotalVo> feeTotalVos = userPos.stream().map(userPo -> {
            FeeTotalVo branchFeeVo = new FeeTotalVo();
            Integer branchId = userPo.getId();
            MemberPo queryPo = new MemberPo();
            queryPo.setBranch_id(branchId);
            List<MemberPo> memberPos = memberMapper.selectList(queryPo);
            List<Integer> memberIds = memberPos.stream().map(memberPo1 -> {
                return memberPo1.getId();
            }).collect(Collectors.toList());
            branchFeeVo.setName(userPo.getEmail());
            branchFeeVo.setRealName(userPo.getName());
            BigDecimal totalBet = new BigDecimal(0);
            if (!CollectionUtils.isEmpty(memberIds)) {
                totalBet = gameRecordMapper.getTotalValidBet(memberIds, Lists.newArrayList(), start, end);
            }
            branchFeeVo.setTotalBet(totalBet);
            BigDecimal reAmountMoney = new BigDecimal(0);
            if (!CollectionUtils.isEmpty(memberIds)) {
                reAmountMoney = gameRecordMapper.getReAmountTotal(memberIds, Lists.newArrayList(), start, end).multiply(new BigDecimal(-1));
            }
            branchFeeVo.setReAmount(reAmountMoney);
            BigDecimal realAmountMoney = new BigDecimal(0);
            if (userPo.getProportion() == null) {
                userPo.setProportion(0);
            }
            if (!CollectionUtils.isEmpty(memberIds)) {
                BigDecimal feeTotal = feeService.getTotalFee(userPo.getId(), 2, memberIds, classifyPos, start, end);
                branchFeeVo.setRealAmount(reAmountMoney);
                branchFeeVo.setTotalBet(totalBet);
                realAmountMoney = reAmountMoney.add(feeTotal.multiply(new BigDecimal(-1))).multiply(new BigDecimal(userPo.getProportion()))
                        .divide(new BigDecimal(100));
            }
            branchFeeVo.setProportion(userPo.getProportion());
            branchFeeVo.setRealAmount(realAmountMoney);
            return branchFeeVo;

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
                        reAmountMoney = reAmountMoney.multiply(new BigDecimal(-1));
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
                    branchAgentVo.setRealAmount(reAmountMoney.add(feeTotal).
                            multiply(new BigDecimal(userPo.getProportion())).
                            divide(new BigDecimal(100)));
                    return branchAgentVo;
                }
        ).collect(Collectors.toList());
    }

}
