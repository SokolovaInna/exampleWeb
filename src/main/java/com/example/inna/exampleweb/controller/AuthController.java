package com.example.inna.exampleweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/authenticate")
public class AuthController {


  @GetMapping
  @Operation(
      summary = "Вход в систему",
      description = "MediaType=application/x-www-form-urlencoded. Пример содержимого body: msisdn=mylogin&password=mypassword",
      responses = {
          @ApiResponse(responseCode = "200", description = "OK")
      },
      parameters = {
          @Parameter(name = "msisdn", description = "Логин пользователя"),
          @Parameter(name = "password", description = "Пароль пользователя")
      }
  )
  public ModelAndView login(@RequestParam("msisdn") String username, @RequestParam("password") String password) {
    try {

      ModelAndView modelAndView = new ModelAndView();
      return modelAndView;

    } catch (Exception e) {

      ModelAndView modelAndView = new ModelAndView();
      modelAndView.addObject("errorMessage", "Неверное имя пользователя или пароль.");
      return modelAndView;
    }
  }
}