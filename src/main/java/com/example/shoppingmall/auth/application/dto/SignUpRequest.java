package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.Position;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "이메일은 필수입니다")
  @Email(message = "올바른 이메일 형식이 아닙니다")
  @Size(max = 100, message = "이메일은 100자 이하여야 합니다")
  private String email;

  @NotBlank(message = "닉네임은 필수입니다")
  @Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
  private String nickname;

  private Position position;

  private ProviderType provider;
}