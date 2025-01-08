package com.example.inna.exampleweb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description =  "Данные для сохранения дела")
public class TodoDto {
  @Schema(description = "Дело")
  String comments;

  public TodoDto() {
  }

  public TodoDto(String comments) {
    this.comments = comments;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
