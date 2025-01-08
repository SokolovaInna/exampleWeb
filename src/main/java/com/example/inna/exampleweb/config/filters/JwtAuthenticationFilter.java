package com.example.inna.exampleweb.config.filters;

import com.example.inna.exampleweb.config.auth.AuthService;
import com.example.inna.exampleweb.config.auth.LoginDetails;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.example.inna.exampleweb.config.auth.JwtHelper.asHeader;



@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

@Autowired private  PasswordEncoder passwordEncoder;

  private final AuthService authService;
  private final AuthenticationManager authManager;

  public JwtAuthenticationFilter( AuthenticationManager authManager, AuthService authService) {
    super(authManager);
    Assert.notNull(authManager, "authenticationManager must be specified");
    this.setFilterProcessesUrl("/authenticate");
    this.authService = authService;
    this.authManager = authManager;
  }
  @PostConstruct
  public void init() {
    Assert.notNull( authManager, "AuthenticationManager is null!");
    Assert.notNull(authService, "AuthService is null!");
    System.out.println("JwtAuthenticationFilter initialized successfully.");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws AuthenticationException {
    String msisdn = request.getParameter("msisdn");
    String password = request.getParameter("password");

//    String encodedPassword = passwordEncoder.encode(password); // Encode the password


     return authManager.authenticate(new UsernamePasswordAuthenticationToken(msisdn, password));
  }
  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain filterChain,
                                          Authentication auth) {

    LoginDetails loginDetails = (LoginDetails)auth.getPrincipal();

    String token = authService.marshal(loginDetails);

    ResponseCookie authCookie = authService.getTokenAsCookie(token);
    response.setHeader(HttpHeaders.SET_COOKIE, authCookie.toString());

    response.setContentType("application/json");
    response.setHeader("token", asHeader(token));

    String xForwardedFor = request.getHeader("X-Forwarded-For");
    String remoteAddr = xForwardedFor != null ? xForwardedFor : request.getRemoteAddr();

  }
}

