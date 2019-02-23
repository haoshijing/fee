package com.yingliguoji.fee.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yingliguoji.fee.controller.request.RebateRequestVo;
import com.yingliguoji.fee.controller.response.RebateVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.enums.GameType;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RebateQueryService {

    @Autowired
    private RebateMapper rebateMapper;

    @Autowired
    private MemberMapper memberMapper;


    static Map<Integer, String> maps = Maps.newConcurrentMap();

    static {
        maps.put(GameType.ZR, "真人");
        maps.put(GameType.DZ, "电子");
        maps.put(GameType.CP, "彩票");
        maps.put(GameType.TY, "体育");
        maps.put(GameType.DJ, "电竞");
        maps.put(GameType.BY, "捕鱼");
        maps.put(GameType.QP, "棋牌");

    }


    public List<RebateVo> queryRebateList(RebateRequestVo rebateRequestVo) {

        Integer memberId = rebateRequestVo.getMemberId();
        Boolean isAdmin = rebateRequestVo.getIsAdmin();
        Integer rebateType = rebateRequestVo.getRebateType();
        MemberPo memberPo = memberMapper.findById(memberId);
        if (null == memberPo) {
            throw new RuntimeException("参数错误");
        }
        boolean isTop = memberPo.getTop_id() == 0;

        List<RebatePo> rebatePos = rebateMapper.queryList(memberId, rebateType);
        List<RebateVo> rebateVos = null;
        if (CollectionUtils.isEmpty(rebatePos) && !isTop) {
            rebatePos = rebateMapper.queryList(memberPo.getTop_id(), rebateType);

        }

        if (CollectionUtils.isEmpty(rebatePos)) {
            rebateVos = Lists.newArrayList();
            int zcQuato = 0;

            int csQuato = 0;
            if (isTop && isAdmin) {
                zcQuato = 70;
                csQuato = 150;
            }
            if (rebateType == RebateType.ZC) {
                rebateVos.add(new RebateVo(zcQuato,0,"占成"));
            } else {
                rebateVos.add(new RebateVo(csQuato, GameType.ZR, "真人"));
                rebateVos.add(new RebateVo(csQuato, GameType.DZ, "电子"));
                rebateVos.add(new RebateVo(csQuato, GameType.CP, "彩票"));
                rebateVos.add(new RebateVo(csQuato, GameType.TY, "体育"));
                rebateVos.add(new RebateVo(csQuato, GameType.DJ, "电竞"));
                rebateVos.add(new RebateVo(csQuato, GameType.BY, "捕鱼"));
                rebateVos.add(new RebateVo(csQuato, GameType.QP, "棋牌"));
            }
        } else {
            rebateVos = rebatePos.stream().map(rebatePo -> {
                RebateVo rebateVo = new RebateVo();
                if(rebateType == RebateType.CS){
                    rebateVo.setGameType(rebatePo.getGameType());
                    rebateVo.setGameName(maps.get(rebatePo.getGameType()));
                }

                rebatePo.setQuota(rebatePo.getQuota());

                return rebateVo;
            }).collect(Collectors.toList());
        }

        return rebateVos;
    }
}
