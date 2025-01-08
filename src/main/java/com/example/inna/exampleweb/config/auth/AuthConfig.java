package com.example.inna.exampleweb.config.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;
import java.time.Duration;


@Configuration
@ConfigurationProperties(prefix = "auth")
 public class AuthConfig {
  private String secret;
  private Duration softTTL;
  private Duration hardTTL;
  private Duration refreshAfter;

  public AuthConfig() {
  }

  public AuthConfig(String secret, Duration softTTL, Duration hardTTL, Duration refreshAfter) {
    this.secret = secret;
    this.softTTL = softTTL;
    this.hardTTL = hardTTL;
    this.refreshAfter = refreshAfter;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Duration getSoftTTL() {
    return softTTL;
  }

  public void setSoftTTL(Duration softTTL) {
    this.softTTL = softTTL;
  }

  public Duration getHardTTL() {
    return hardTTL;
  }

  public void setHardTTL(Duration hardTTL) {
    this.hardTTL = hardTTL;
  }

  public Duration getRefreshAfter() {
    return refreshAfter;
  }

  public void setRefreshAfter(Duration refreshAfter) {
    this.refreshAfter = refreshAfter;
  }
}