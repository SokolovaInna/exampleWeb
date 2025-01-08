package com.example.inna.exampleweb.controller;


import com.example.inna.exampleweb.dto.UserDto;
import com.example.inna.exampleweb.model.User;
import com.example.inna.exampleweb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping
  @Operation(summary = "Получить список всех пользователей")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список пользователей",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = User[].class)))
  })
  public ResponseEntity<List<User>> getAllUsers() {
    List<User>  users =  userService.findAllUsers();
    return ResponseEntity.ok(users);
  }

  @PostMapping("save")
   @Operation(
      summary = "Добавление нового пользователя",
      responses = {@ApiResponse(responseCode = "200", description = "Пользователь добавлен")},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = UserDto.class)))
  )
  @PreAuthorize("hasAuthority('ADD')")
  public void saveUser(@RequestBody UserDto dto, Authentication authentication) {
     userService.saveUser(dto);

  }

}