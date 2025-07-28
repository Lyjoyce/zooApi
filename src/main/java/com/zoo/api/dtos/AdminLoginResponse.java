package com.zoo.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminLoginResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String token;
}
