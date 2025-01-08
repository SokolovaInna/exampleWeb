package com.example.inna.exampleweb.controller;


import com.example.inna.exampleweb.dto.TodoDto;
import com.example.inna.exampleweb.dto.UserDto;
import com.example.inna.exampleweb.model.Todo;
import com.example.inna.exampleweb.model.User;
import com.example.inna.exampleweb.service.TodoService;
import com.example.inna.exampleweb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
  @Autowired
  TodoService todoService;

  @GetMapping
  @Operation(summary = "Получить список всех дел")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список всех дел",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = User[].class)))
  })
  public ResponseEntity<List<TodoDto>> getAllTodo(Authentication authentication) {
    List<TodoDto>  todos =  todoService.findAllTodos(authentication);
    return ResponseEntity.ok(todos);
  }

  @PostMapping("save")
   @Operation(
      summary = "Добавление нового дела",
      responses = {@ApiResponse(responseCode = "200", description = "Дело добавлено")},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = TodoDto.class)))
  )
  public void saveTodo(@RequestBody TodoDto dto, Authentication authentication) {
   todoService.saveTodo(dto, authentication);

  }

  @GetMapping("active")
  @Operation(summary = "Получить список не завершенных дел")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список дел",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = User[].class)))
  })
  public ResponseEntity<List<TodoDto>> getAllActiveTodo(Authentication authentication) {
    List<TodoDto>  todos =  todoService.findAllActiveTodos(authentication);
    return ResponseEntity.ok(todos);
  }

}