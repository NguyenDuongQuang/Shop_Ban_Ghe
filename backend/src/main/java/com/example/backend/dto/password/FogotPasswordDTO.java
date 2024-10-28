package com.example.backend.dto.password;

import lombok.Data;

@Data
public class FogotPasswordDTO {
    private String email;

    private String keys;

    private  String keys_return;

    private String password_new;
}
