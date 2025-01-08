package com.example.inna.exampleweb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description =  "Данные для регистрации пользователя")
public class UserDto {
  @Schema(description = "Электронная почта")
  String email;

  @Schema(description = "Пароль")
  String password;
  @Schema(description = "Номер телефона")
  String msisdn;

  public UserDto() {
  }

  public UserDto(String email, String password, String msisdn) {
    this.email = email;
    this.password = password;
    this.msisdn = msisdn;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDto userDto = (UserDto) o;
    return Objects.equals(email, userDto.email) && Objects.equals(password, userDto.password) && Objects.equals(msisdn, userDto.msisdn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, msisdn);
  }
}
