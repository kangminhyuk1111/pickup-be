package com.example.shoppingmall.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {

  private String token;
  private boolean isNewUser;
  private String name;
}