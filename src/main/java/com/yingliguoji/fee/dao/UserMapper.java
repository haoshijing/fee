package com.yingliguoji.fee.dao;

import com.yingliguoji.fee.po.UserPo;

import java.util.List;

public interface UserMapper {
    Integer insert(UserPo userPo);

    UserPo findByName(String name);

    List<UserPo> getAllBranch();
}
