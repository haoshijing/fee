package com.yingliguoji.fee.service;


import com.yingliguoji.fee.controller.request.RebateSetDataRequestVo;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.dao.RebateMapper;
import com.yingliguoji.fee.enums.RebateType;
import com.yingliguoji.fee.po.MemberPo;
import com.yingliguoji.fee.po.RebatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
public class RebateWriteService {
    @Autowired
    private RebateMapper rebateMapper;


    @Autowired
    private MemberMapper memberMapper;


    public void settingData(RebateSetDataRequestVo requestVo) {

        Boolean isAdmin = requestVo.getIsAdmin();

        Integer memberId = requestVo.getMemberId();

        Integer rebateType = requestVo.getRebateType();

        List<RebateSetDataRequestVo.RebateSettingVo> rebateSettingVos = requestVo.getDatas();

        MemberPo memberPo = memberMapper.findById(requestVo.getMemberId());
        if (memberPo == null) {
            throw new RuntimeException("该会员不存在");
        }
        boolean checkCanSet = true;
        if (memberPo.getTop_id() == 0) {

            if (rebateType == RebateType.ZC) {
                if (rebateSettingVos.get(0).getQuota() > 70) {
                    checkCanSet = false;
                }
            } else {
                boolean find = rebateSettingVos.stream().anyMatch(rebateSettingVo -> rebateSettingVo.getQuota() > 200);
                if (find) {
                    checkCanSet = false;
                }
            }
        } else {
            List<RebatePo> rebatePos = rebateMapper.queryList(memberPo.getTop_id(), rebateType);
            if (rebateType == RebateType.ZC) {
                if (CollectionUtils.isEmpty(rebatePos)) {
                    checkCanSet = false;
                } else {
                    if (rebateSettingVos.get(0).getQuota() > rebatePos.get(0).getQuota()) {
                        checkCanSet = false;
                    }
                }
            } else {
                boolean find = rebateSettingVos.stream().anyMatch(rebateSettingVo -> {

                    Optional<RebatePo> optional = rebatePos.stream().filter(rebatePo1 -> rebatePo1.getGameType().equals(rebateSettingVo.getGameType())).findFirst();
                    return !optional.isPresent() || optional.get().getQuota().intValue() < rebateSettingVo.getQuota();
                });
                if (find) {
                    checkCanSet = false;
                }
            }
        }
        if (!checkCanSet) {
            throw new RuntimeException("超出额度");
        }

        saveSetting(memberId, rebateType, rebateSettingVos);

    }

    private void saveSetting(Integer memberId, Integer rebateType, List<RebateSetDataRequestVo.RebateSettingVo> rebateSettingVos) {

        rebateSettingVos.forEach(rebateSettingVo -> {
            RebatePo rebatePo = rebateMapper.findByRebateTypeAndMemberIdAndGameType(memberId, rebateType, rebateSettingVo.getGameType());
            if (rebatePo == null) {
                rebatePo = new RebatePo();
                rebatePo.setQuota(rebateSettingVo.getQuota());
                rebatePo.setGameType(rebateSettingVo.getGameType());
                rebatePo.setRebateType(rebateType);
                rebatePo.setMemberId(memberId);
                rebatePo.setInsertTime(System.currentTimeMillis());
                rebatePo.setLastUpdateTime(System.currentTimeMillis());
                rebateMapper.insert(rebatePo);
            } else {
                RebatePo updatePo = new RebatePo();
                updatePo.setId(rebatePo.getId());
                updatePo.setLastUpdateTime(System.currentTimeMillis());
                updatePo.setQuota(rebateSettingVo.getQuota());
                rebateMapper.updateById(updatePo);
            }

        });

    }
}
