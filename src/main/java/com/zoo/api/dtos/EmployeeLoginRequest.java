package com.zoo.api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoginRequest {
    private String email;
    private String password;
}
