package com.yingliguoji.fee.service;

import com.yingliguoji.fee.controller.request.MemberBetRequest;
import com.yingliguoji.fee.controller.response.MemberBetResponse;
import com.yingliguoji.fee.po.GameRecordPo;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.js.MemberGamePo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MemberBetService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GameRecordService gameRecordService;

    public MemberBetResponse queryMemberBetTotal(MemberBetRequest request) {
        Integer currentAgentId = request.getCurrentAgentId();
        String name = request.getName();
        List<MemberPo> memberPos =  memberService.getMemberIds(currentAgentId,name);
        memberPos.add(0, memberService.findById(currentAgentId));

        List<Integer> memberIds = memberPos.stream().map(
                memberPo -> memberPo.getId()
        ).collect(Collectors.toList());

        GameRecordPo queryPo = new GameRecordPo();
        if(request.getStartTime() != null) {
            queryPo.setStart(new DateTime(request.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if(request.getEndTime() != null) {
            queryPo.setEnd(new DateTime(request.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if(request.getGameType() != null) {
            queryPo.setGameType(request.getGameType());
        }
        queryPo.setMemberIds(memberIds);

        List<MemberGamePo> memberGamePos = gameRecordService.queryMemberGamePos(queryPo);

        List<MemberBetResponse.MemberBetDetailVo> detailVos = memberGamePos.stream().map(
                memberGamePo -> {
                    MemberBetResponse.MemberBetDetailVo detailVo = new MemberBetResponse.MemberBetDetailVo();
                    detailVo.setBetAmount(memberGamePo.getTotalBetAmount().doubleValue());
                    detailVo.setValidBetAmount(memberGamePo.getTotalValidBetAmount().doubleValue());
                    detailVo.setNetAmount(memberGamePo.getTotalNetAmount().doubleValue());

                    detailVo.setGameType(memberGamePo.getGameType());
                    detailVo.setGameName(GameTypeUtils.getGameName(memberGamePo.getGameType()));

                    Optional<MemberPo> optional = memberPos.stream().filter(memberPo ->
                    memberPo.getId().equals(memberGamePo.getMemberId())).findFirst();
                    if(optional.isPresent()){
                        detailVo.setName(optional.get().getName());
                    }
                    return detailVo;
                }).filter(memberBetDetailVo -> {
            if (StringUtils.isNotEmpty(request.getName())) {
                return memberBetDetailVo.getName().contains(request.getName());
            }
            return true;
        }).collect(Collectors.toList());
        Double totalBetAmount = 0.0;
        Double totalValidBetAmount = 0.0;
        Double totalNetAmount = 0.0;
        for(MemberBetResponse.MemberBetDetailVo memberBetDetailVo : detailVos){
            totalBetAmount += memberBetDetailVo.getBetAmount();
            totalValidBetAmount += memberBetDetailVo.getValidBetAmount();
            totalNetAmount += memberBetDetailVo.getNetAmount();
        }

        MemberBetResponse response = new MemberBetResponse();
        response.setMemberBetDetailVoList(detailVos);
        response.setTotalBetAmount(totalBetAmount);
        response.setTotalNetAmount(totalNetAmount);
        response.setTotalValidNetAmount(totalValidBetAmount);
        return response;
    }
}
