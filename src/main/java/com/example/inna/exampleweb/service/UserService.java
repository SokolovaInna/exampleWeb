package com.example.inna.exampleweb.service;

import com.example.inna.exampleweb.dto.UserDto;
import com.example.inna.exampleweb.model.Role;
import com.example.inna.exampleweb.model.User;
import com.example.inna.exampleweb.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public List<User> findAllUsers(){
    return userRepository.findAll();
  }


  public User saveUser(UserDto dto) {
    Set<Role> role = new HashSet<>();
    List<String> perm= new ArrayList<>();
    perm.add ("VIEW");
    perm.add ("MODIFY");
    role.add(new Role("USER", perm));
    User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getMsisdn(), role );
   return userRepository.save(user);
  }
}
