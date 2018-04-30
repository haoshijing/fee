package com.yingliguoji.fee.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.controller.response.PlayerRecordRequest;
import com.yingliguoji.fee.controller.response.PlayerRecordTotalVo;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import com.yingliguoji.fee.service.GameRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PlayRecordsController {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private GameRecordService gameRecordService;
    @RequestMapping("/getPlayerRecordTotal")
    public List<PlayerRecordTotalVo> getPlayerRecordTotal(@RequestBody PlayerRecordRequest recordRequest) {

        List<ClassifyPo> classifyPos = classifyMapper.selectAll();
        if(CollectionUtils.isEmpty(recordRequest.getMemberIds())){
            return Lists.newArrayList();
        }

        List<PlayerRecordTotalVo> recordTotalVos = recordRequest.getMemberIds().stream().map(
                memberId -> {
                    PlayerRecordTotalVo recordTotalVo = new PlayerRecordTotalVo();
                    List<PlayerRecordTotalVo.ClassiFyItem> items =
                            classifyPos.stream().map(classifyPo -> {
                                PlayerRecordTotalVo.ClassiFyItem item = new PlayerRecordTotalVo.ClassiFyItem();
                                item.setClassiFyId(classifyPo.getId());
                                item.setName(classifyPo.getName());
                                BigDecimal bigDecimal = gameRecordService.getMoney(memberId,recordRequest.getStart(),recordRequest.getEnd(),classifyPo);
                                return item;
                            }).collect(Collectors.toList());
                    recordTotalVo.setMemberId(memberId);
                    recordTotalVo.setClassiFyItemList(items);

                    return recordTotalVo;
                }
        ).collect(Collectors.toList());
        return recordTotalVos;
    }
}
