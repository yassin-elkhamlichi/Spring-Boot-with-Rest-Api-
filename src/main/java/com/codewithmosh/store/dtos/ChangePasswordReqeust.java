package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordReqeust {
    private String oldPassword;
    private String newPassword;
}
