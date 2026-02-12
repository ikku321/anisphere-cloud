package com.iikun.userservice.domain.request;

import lombok.Data;
/**
 * author iikun
 * time 2026/2/8 12:56
 * version 1.0.0
 * msg:
 */
@Data
public class UserBlackListModel {
    private String uid;
    private String BlackUid;
    private String NickName;
    private String Email;
}
