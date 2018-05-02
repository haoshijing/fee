package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.response.BranchAgentVo;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.MemberPo;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private GameRecordMapper gameRecordMapper;

    private DefaultEventExecutor defaultEventExecutor;

    public MemberService() {
        defaultEventExecutor = new DefaultEventExecutor(new DefaultThreadFactory("QueryMemberDataService"))
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

    public List<BranchAgentVo> branchAgentVoList(Integer branchId, Long start, Long end) {

        List<MemberPo> proxyList = getAllUnderProxy(branchId);
        return proxyList.stream().map(
                memberPo -> {
                    BranchAgentVo branchAgentVo = new BranchAgentVo();
                    branchAgentVo.setName(memberPo.getName());
                    branchAgentVo.setRealName(memberPo.getReal_name());
                    List<MemberPo> memberPos = getMemberIds(memberPo.getId());
                    List<Integer> memberIds = memberPos.stream().map(memberPo1 -> {
                        return memberPo1.getId();
                    }).collect(Collectors.toList());

                    BigDecimal totalBet = new BigDecimal(0);
                    if (!CollectionUtils.isEmpty(memberIds)) {
                        String startTimeStampStr = "";
                        String endTimeStampStr = null;
                        if (start != null) {
                            Timestamp startTimeStamp = new Timestamp(start);
                            startTimeStampStr = startTimeStamp.toString();
                        }
                        if (end != null) {
                            Timestamp endTimeStamp = new Timestamp(end);
                            endTimeStampStr = endTimeStamp.toString();
                        }
                        totalBet = gameRecordMapper.getTotalValidBet(memberIds, startTimeStampStr, endTimeStampStr);
                    }

                    BigDecimal reAmountMoney = new BigDecimal(0);
                    if (!CollectionUtils.isEmpty(memberIds)) {
                        String startTimeStampStr = "";
                        String endTimeStampStr = null;
                        if (start != null) {
                            Timestamp startTimeStamp = new Timestamp(start);
                            startTimeStampStr = startTimeStamp.toString();
                        }
                        if (end != null) {
                            Timestamp endTimeStamp = new Timestamp(end);
                            endTimeStampStr = endTimeStamp.toString();
                        }
                        totalBet = gameRecordMapper.getBets(memberIds, startTimeStampStr, endTimeStampStr);
                    }
                    branchAgentVo.setTotalBet(totalBet);
                    return branchAgentVo;
                }
        ).collect(Collectors.toList());
    }
}
