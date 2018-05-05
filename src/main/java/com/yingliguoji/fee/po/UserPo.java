package com.yingliguoji.fee.po;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserPo {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Integer isSuperAdmin;
    private Integer roleId;
    private String rememberToken;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String invitation;
    private String telnum = "";
    private String qq = "";
    private Integer superVal = 0;
    private String wechat = "";
    private Integer proportion = 0;
    private String lines = "";
}
