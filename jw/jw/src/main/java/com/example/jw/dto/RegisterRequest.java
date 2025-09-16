package com.example.jw.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {

    private String username;
    private String password;
    private String scope;


}
