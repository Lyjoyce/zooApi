package com.zoo.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeLoginResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String token; // JWT
}
