package com.yingliguoji.fee.controller;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.controller.response.PlayerRecordRequest;
import com.yingliguoji.fee.controller.response.PlayerRecordTotalVo;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.service.GameRecordService;
import com.yingliguoji.fee.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayRecordsController {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private GameRecordService gameRecordService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/getPlayerRecordTotal")
    public List<PlayerRecordTotalVo> getPlayerRecordTotal(@RequestBody PlayerRecordRequest recordRequest) {

        List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        Integer proxyId = recordRequest.getProxyId();
        if (proxyId == null) {
            return Lists.newArrayList();
        }
        List<Integer> memberIds = memberService.getMemberIds(proxyId).stream()
                .filter(memberPo -> {
                    return memberPo != null;
                }).map(memberPo -> {
                    return memberPo.getId();
                }).collect(Collectors.toList());
        List<PlayerRecordTotalVo> recordTotalVos = memberIds.stream().map(
                memberId -> {
                    PlayerRecordTotalVo recordTotalVo = new PlayerRecordTotalVo();
                    List<PlayerRecordTotalVo.ClassiFyItem> items =
                            classifyPos.stream().map(classifyPo -> {
                                PlayerRecordTotalVo.ClassiFyItem item = new PlayerRecordTotalVo.ClassiFyItem();
                                item.setClassiFyId(classifyPo.getId());
                                item.setName(classifyPo.getName());
                                List<Integer> gameTypes = getGameTypes(classifyPo);
                                List<Integer> queryMembers = Lists.newArrayList(memberId);
                                BigDecimal bigDecimal = gameRecordService.getReAmountTotal(queryMembers, gameTypes,
                                        recordRequest.getStart(), recordRequest.getEnd());
                                item.setMoney(bigDecimal.doubleValue());
                                return item;
                            }).collect(Collectors.toList());
                    recordTotalVo.setMemberId(memberId);
                    recordTotalVo.setClassiFyItemList(items);

                    return recordTotalVo;
                }
        ).collect(Collectors.toList());
        return recordTotalVos;
    }

    private List<Integer> getGameTypes(ClassifyPo classifyPo) {
        String type = classifyPo.getSmallType();
        String[] typeArr = type.split(",");
        List<Integer> gameTypes = Lists.newArrayList();
        for (String typeStr : typeArr) {
            gameTypes.add(Integer.valueOf(typeStr));
        }
        return gameTypes;
    }
}
