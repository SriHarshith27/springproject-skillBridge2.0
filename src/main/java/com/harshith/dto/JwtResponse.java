package com.harshith.dto;


import lombok.Data;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}