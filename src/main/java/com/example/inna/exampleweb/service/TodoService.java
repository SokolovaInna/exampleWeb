package com.example.inna.exampleweb.service;

import com.example.inna.exampleweb.dto.TodoDto;
import com.example.inna.exampleweb.model.Todo;
import com.example.inna.exampleweb.model.User;
import com.example.inna.exampleweb.model.repo.TodoRepository;
import com.example.inna.exampleweb.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
  @Autowired private TodoRepository todoRepository;
  @Autowired private UserRepository userRepository;


  public void createTodo(User user, String comments) {
    Todo todo = new Todo(user, comments);
    todoRepository.save(todo);
  }

  public List<TodoDto> findAllTodos(Authentication authentication) {
   String  msisdn = (String) authentication.getPrincipal();
   Optional<User> optionalUser = userRepository.findByMsisdn(msisdn);
   User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User with msisdn: " + msisdn + " not found"));

//   return todoRepository.findAllNotCompleteByUserId();
   return todoRepository.findAll().stream()
       .map(this::convertToDto)
       .collect(Collectors.toList());
  }

  public void saveTodo(TodoDto dto,  Authentication authentication) {
    String  msisdn = (String) authentication.getPrincipal();
    Optional<User> optionalUser = userRepository.findByMsisdn(msisdn);
    User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User with msisdn: " + msisdn + " not found"));
    Todo todo = new Todo(new Date(), user, dto.getComments());
    todoRepository.save(todo);
  }

  public List<TodoDto> findAllActiveTodos(Authentication authentication) {
    String  msisdn = (String) authentication.getPrincipal();
    Optional<User> optionalUser = userRepository.findByMsisdn(msisdn);
    User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User with msisdn: " + msisdn + " not found"));

   return todoRepository.findAllNotCompleteByUserId(user).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  private TodoDto convertToDto(Todo todo) {
    TodoDto todoDto = new TodoDto();
    todoDto.setComments(todo.getComments());
    return todoDto;
  }
}
