package com.example.inna.exampleweb.config;

import com.example.inna.exampleweb.config.auth.AuthService;

import com.example.inna.exampleweb.config.filters.JwtAuthenticationFilter;
import com.example.inna.exampleweb.config.filters.JwtCookieAuthorizationFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;

import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

  @Autowired
  private AuthService authService;


  @Primary
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager() ;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
   return new BCryptPasswordEncoder();
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(eh -> eh.authenticationEntryPoint(new AuthenticationEntryPoint() {
          @Override
          public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            if (response.getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) {
              // Already set.
            } else {
              // Default as in `HttpStatusEntryPoint`
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
          }
        }))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(antMatcher("/actuator/**")).permitAll()
            .requestMatchers(
                antMatcher("/v3/api-docs/**"),
                antMatcher("/swagger-ui/**"),
                antMatcher("/users/**")
            ).authenticated()
            .anyRequest().authenticated()
        )
        .addFilter(new JwtAuthenticationFilter(authenticationManager,   authService))
        .addFilter(new JwtCookieAuthorizationFilter(authenticationManager, authService));
    return http.build();
  }
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//    http
//        .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
//        .csrf(AbstractHttpConfigurer::disable)
//        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .exceptionHandling(eh -> eh.authenticationEntryPoint(new AuthenticationEntryPoint() {
//          @Override
//          public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex) throws IOException {
//            if (resp.getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) {
//              resp.getWriter().write("Too many requests");
//            } else {
//              resp.setStatus(HttpStatus.UNAUTHORIZED.value());
//              resp.getWriter().write("Unauthorized");
//            }
//          }
//        }))
//        .authorizeHttpRequests(r -> r
//            .requestMatchers("/actuator/**").permitAll()
//            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/users/**").authenticated()
//            .anyRequest().authenticated())
//        .addFilter(new JwtAuthenticationFilter(authenticationManager,  authService))
//        .addFilter(new JwtCookieAuthorizationFilter(authenticationManager, authService));
//    return http.build();
//  }
}

//@EnableWebSecurity
//@EnableMethodSecurity
//@Configuration
//public class WebSecurityConfig {
//
//  @Autowired private AuthService authService;
//
//
//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    return authenticationConfiguration.getAuthenticationManager();
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//   return new BCryptPasswordEncoder();
//  }
//
//  @DependsOn("authenticationManager")
//  @Bean
//  public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//    return new JwtAuthenticationFilter(authenticationManager,  authService);
//  }
//
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//    http
//        .cors(AbstractHttpConfigurer::disable)
//        .csrf(AbstractHttpConfigurer::disable)
//        .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
//        .exceptionHandling(eh -> eh.authenticationEntryPoint(new AuthenticationEntryPoint() {
//          @Override
//          public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex) {
//            if (resp.getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) {
//            } else {
//              resp.setStatus( HttpStatus.UNAUTHORIZED.value());
//            }
//          }
//        }))
//        .authorizeHttpRequests(r -> r
//            .requestMatchers(
//                antMatcher("/actuator/**")
//            ).permitAll()
//            .requestMatchers(
//                antMatcher("/v3/api-docs/**"),
//                antMatcher("/swagger-ui/**"),
//                antMatcher("/users/**")
//            ).authenticated()
//            .anyRequest().authenticated())
//        .addFilter(jwtAuthenticationFilter(authenticationManager))
//        .addFilter(new JwtCookieAuthorizationFilter(authenticationManager, authService));
//    return http.build();
//  }
//
//
//}





//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .authorizeHttpRequests((requests) -> requests
//            .requestMatchers("/", "/home").permitAll()
//            .anyRequest().authenticated()
//        )
//        .formLogin((form) -> form
//            .loginPage("/login")
//            .permitAll()
//        )
//        .logout((logout) -> logout.permitAll());
//
//    return http.build();
//  }
//
//  @Bean
//  public UserDetailsService userDetailsService() {
//    UserDetails user =
//        User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//
//    return new InMemoryUserDetailsManager(user);
//  }