package com.yingliguoji.fee.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.controller.response.PlayerRecordRequest;
import com.yingliguoji.fee.controller.response.PlayerRecordTotalVo;
import com.yingliguoji.fee.dao.ClassifyMapper;
import com.yingliguoji.fee.dao.GameRecordMapper;
import com.yingliguoji.fee.po.ClassifyPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PlayRecordsController {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private GameRecordMapper gameRecordMapper;

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
                                String type = classifyPo.getType();
                                String []typeArr = type.split(",");
                                List<Integer> gameTypes = Lists.newArrayList();
                                for(String typeStr:typeArr){
                                    gameTypes.add(Integer.valueOf(typeStr));
                                }

                                Map<String,Object> params  = Maps.newHashMap();
                                params.put("memberId",memberId);
                                params.put("startTime",recordRequest.getStartTime());
                                params.put("endTime",recordRequest.getEndTime());
                                params.put("gameTypes",gameTypes);

                                Integer money = gameRecordMapper.getPlayerTotal(params);
                                item.setMoney(money);
                                return item;
                            }).collect(Collectors.toList());

                    return recordTotalVo;
                }
        ).collect(Collectors.toList());
        return recordTotalVos;
    }
}
