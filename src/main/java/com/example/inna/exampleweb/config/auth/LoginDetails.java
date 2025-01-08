package com.example.inna.exampleweb.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


 public class LoginDetails extends User {

    String msisdn;

    public LoginDetails(String msisdn, String password, Collection<? extends GrantedAuthority> authorities) {
      super(msisdn, password, authorities);
      this.msisdn = msisdn;
    }
  }

