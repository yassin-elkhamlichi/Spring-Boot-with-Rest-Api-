package com.codewithmosh.store.users;

import lombok.Data;

@Data
public class ChangePasswordReqeust {
    private String oldPassword;
    private String newPassword;
}
