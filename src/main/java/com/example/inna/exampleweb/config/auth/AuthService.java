package com.example.inna.exampleweb.config.auth;

import com.example.inna.exampleweb.model.Role;
import com.example.inna.exampleweb.model.User;
import com.example.inna.exampleweb.model.repo.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.redisson.api.RedissonClient;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;



@Service
public class AuthService implements UserDetailsService {


  @Autowired private RedissonClient redis;
  @Autowired private AuthConfig authConfig;
  @Autowired private UserRepository userRepository;


  public String marshal(LoginDetails loginDetails) {
    JwtHelper.AccessToken accessToken = new JwtHelper.AccessToken();
    accessToken.setMsisdn(loginDetails.getUsername());
    accessToken.setRoles(loginDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()));


    return JwtHelper.marshal(accessToken, authConfig);
  }
 public JwtHelper.AccessToken unmarshal(String tokenValue) {
    try {
      JwtHelper.AccessToken token = JwtHelper.unmarshal(tokenValue, authConfig);

      if (isTokenBanned(tokenValue)) {

        return null;
      }

     return token;

    } catch (ExpiredJwtException e) {

      return null;

    } catch (Exception e) {

      return null;
    }
  }

  private boolean isTokenBanned(String key) {
    try {
      String b = "banned:"+key;
    return redis.getKeys().countExists(b) > 0;

    } catch (Exception e) {
          return false;
    }
  }

  public ResponseCookie getTokenAsCookie(String tokenValue) {
    JwtHelper.AccessToken accessToken = unmarshal(tokenValue) ;

   return ResponseCookie.from(JwtHelper.TOKEN_COOKIE_NAME, tokenValue)
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .maxAge(accessToken.getSoftTTL().longValue())
        .path("/")
        .build();
  }
  @Override
  public UserDetails loadUserByUsername(String msisdn) throws UsernameNotFoundException {


    Optional<User> userOptional = userRepository.findByMsisdn(msisdn);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User with msisdn: " + msisdn + " not found"));


    List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

    Set<Role> roles = user.getRoleSet();
    if (roles != null){
      for (Role role : roles){
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getId()));
             List<String> permissions = role.getPermissions();
        if (permissions != null){
          for (String perm : permissions){
            grantedAuthorities.add(new SimpleGrantedAuthority(perm));
          }
        }
      }
    }
    return new LoginDetails(user.getMsisdn(), user.getPassword(), grantedAuthorities);
  }

  }