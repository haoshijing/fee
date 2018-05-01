package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MemberPo;

import java.util.List;

public interface MemberMapper {
    List<MemberPo> selectAll();

    MemberPo findById(Integer memberId);

    int update(MemberPo memberPo);

    MemberPo findByName(String name);
}
