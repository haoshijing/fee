package com.yingliguoji.fee.service;

import com.yingliguoji.fee.dao.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserService {

    @Autowired
    private MemberMapper memberMapper;

}
