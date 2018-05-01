package com.yingliguoji.fee.po;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserPo {
    private Integer id;
    private String name;
    private String password;
    private Integer isSuperAdmin;
    private Integer roleId;
    private String rememberToken;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String invitation;
}
