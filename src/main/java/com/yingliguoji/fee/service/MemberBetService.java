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
        MemberBetResponse response = new MemberBetResponse();
        List<MemberBetResponse.MemberBetDetailVo> detailVos = obtainDetails(request);
        response.setMemberBetDetailVoList(detailVos);
        fillTotalData(response);
        return response;
    }

    /**
     * 查询详情数据
     *
     * @param request
     * @return
     */
    private List<MemberBetResponse.MemberBetDetailVo> obtainDetails(MemberBetRequest request) {
        Integer currentAgentId = request.getCurrentAgentId();
        String name = request.getName();
        List<MemberPo> memberPos = memberService.getMemberIds(currentAgentId, name);
        MemberPo currentMemberPo = memberService.findById(currentAgentId);
        if (currentMemberPo != null) {
            memberPos.add(0, currentMemberPo);
        }

        List<Integer> memberIds = memberPos.stream().map(memberPo -> memberPo.getId()).collect(Collectors.toList());

        GameRecordPo queryPo = fillQueryPoData(request);
        queryPo.setMemberIds(memberIds);
        List<MemberGamePo> memberGamePos = gameRecordService.queryMemberGamePos(queryPo);

        List<MemberBetResponse.MemberBetDetailVo> detailVos = memberGamePos.stream().map(memberGamePo -> {
            MemberBetResponse.MemberBetDetailVo detailVo = new MemberBetResponse.MemberBetDetailVo();
            detailVo.setBetAmount(memberGamePo.getTotalBetAmount().doubleValue());
            detailVo.setValidBetAmount(memberGamePo.getTotalValidBetAmount().doubleValue());
            detailVo.setNetAmount(memberGamePo.getTotalNetAmount().doubleValue());

            detailVo.setGameType(memberGamePo.getGameType());
            detailVo.setGameName(GameTypeUtils.getGameName(memberGamePo.getGameType()));

            Optional<MemberPo> optional = memberPos.stream().filter(memberPo -> memberPo.getId().equals(memberGamePo.getMemberId())).findFirst();
            if (optional.isPresent()) {
                detailVo.setName(optional.get().getName());
            }
            return detailVo;
        }).filter(memberBetDetailVo -> {
            if (StringUtils.isNotEmpty(request.getName())) {
                return memberBetDetailVo.getName().contains(request.getName());
            }
            return true;
        }).collect(Collectors.toList());
        return detailVos;
    }

    /**
     * 填充查询参数
     *
     * @param request
     * @return
     */
    private GameRecordPo fillQueryPoData(MemberBetRequest request) {
        GameRecordPo queryPo = new GameRecordPo();
        Long start = request.getStartTime();
        Long end = request.getEndTime();
        if (start != null) {
            queryPo.setStart(new DateTime(start).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (end != null) {
            queryPo.setEnd(new DateTime(end).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (request.getGameType() != null) {
            queryPo.setGameType(request.getGameType());
        }
        return queryPo;
    }

    /**
     * 填充总数据data
     *
     * @param response
     */
    private void fillTotalData(MemberBetResponse response) {
        Double totalBetAmount = 0.0;
        Double totalValidBetAmount = 0.0;
        Double totalNetAmount = 0.0;
        List<MemberBetResponse.MemberBetDetailVo> detailVos = response.getMemberBetDetailVoList();
        for (MemberBetResponse.MemberBetDetailVo memberBetDetailVo : detailVos) {
            totalBetAmount += memberBetDetailVo.getBetAmount();
            totalValidBetAmount += memberBetDetailVo.getValidBetAmount();
            totalNetAmount += memberBetDetailVo.getNetAmount();
        }

        response.setMemberBetDetailVoList(detailVos);
        response.setTotalBetAmount(totalBetAmount);
        response.setTotalNetAmount(totalNetAmount);
        response.setTotalValidNetAmount(totalValidBetAmount);
    }

}
