package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.MemberPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    List<MemberPo> selectAll();

    MemberPo findById(Integer memberId);

    int updateById(MemberPo memberPo);

    int update(MemberPo memberPo);

    MemberPo findByName(String name);

    List<MemberPo> selectList(MemberPo queryPo);

    void batchUpdateBranchId(@Param("branchId") Integer branchId, @Param("memberIds") List<Integer> memberIds);

    List<Integer> queryZcMember(@Param("topId") Integer topId, @Param("name") String name);
}
