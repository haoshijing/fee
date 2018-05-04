package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MemberPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    List<MemberPo> selectAll();

    MemberPo findById(Integer memberId);

    int update(MemberPo memberPo);

    MemberPo findByName(String name);

    List<MemberPo> selectList(MemberPo queryPo);

    void batchUpdateBranchId(@Param("branchId") Integer branchId,@Param("memberIds") List<Integer> memberIds);
}
