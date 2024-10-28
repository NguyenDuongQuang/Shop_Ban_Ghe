package com.example.backend.dto.password;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String email;

    private String password_old;

    private String password_new;
}
