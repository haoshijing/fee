package com.yingliguoji.fee.service;

import com.google.common.collect.Lists;
import com.yingliguoji.fee.dao.MemberMapper;
import com.yingliguoji.fee.po.MemberPo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;


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

}
