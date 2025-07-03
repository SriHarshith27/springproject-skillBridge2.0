package com.harshith.dto;


import lombok.Data;


import java.util.Set;
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
}
