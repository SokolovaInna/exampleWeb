package com.example.inna.exampleweb.config.filters;


import com.example.inna.exampleweb.config.auth.AuthService;
import com.example.inna.exampleweb.config.auth.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class JwtCookieAuthorizationFilter extends BasicAuthenticationFilter {

  private final AuthService authService;

  public JwtCookieAuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService) {
    super(authenticationManager);
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    handleAuthToken (request, response);
    filterChain.doFilter(request, response);
  }

  private void handleAuthToken(HttpServletRequest request,
                               HttpServletResponse response) {


    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0){
      return;
    }
    Optional<Cookie> cookie = Arrays.stream(cookies)
        .filter(c -> JwtHelper.TOKEN_COOKIE_NAME.equals(c.getName()))
        .findFirst();

    if (cookie.isEmpty()) {
      return;
    }

    String cookieValue = cookie.get().getValue();

    String tokenValue;
    try {
      tokenValue = JwtHelper.asToken(URLDecoder.decode(cookieValue, StandardCharsets.UTF_8.name()));
    } catch (UnsupportedEncodingException e) {
      // Log the error
      return;
    }

    JwtHelper.AccessToken token = authService.unmarshal(tokenValue);

    if (token == null){
      return;
    }

    List<SimpleGrantedAuthority> authorities = token.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());


    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(token.getMsisdn(), null, authorities);

    authenticationToken.setDetails(token);


    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

  }

}
