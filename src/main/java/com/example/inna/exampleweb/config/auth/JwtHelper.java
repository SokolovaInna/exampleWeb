package com.example.inna.exampleweb.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
public class JwtHelper {

  private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

  public static final String TOKEN_COOKIE_NAME = "test-token";

  private static final String CLAIM_SOFT_TTL = "ttl";
  private static final String CLAIM_HARD_TTL = Claims.EXPIRATION;

  protected static AccessToken unmarshal(String token, AuthConfig cfg) {
    try {
      JwtParser parser = Jwts.parser()
          .setSigningKey(new SecretKeySpec(cfg.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
          .build();
      Jws<Claims> jws = parser.parseClaimsJws(token);
      Claims claims = jws.getBody();


      long softTTLSeconds = claims.get(CLAIM_SOFT_TTL, Integer.class).longValue();
      long hardTTLSeconds = claims.get(CLAIM_HARD_TTL, Integer.class).longValue();
      Instant now = Instant.now();

      if (Instant.ofEpochSecond(softTTLSeconds).isBefore(now) ||
          Instant.ofEpochSecond(hardTTLSeconds).isBefore(now)) {
        logger.debug("Token expired: " + token);
        return null;
      }

      return new AccessToken(
          claims.getSubject(),
          claims.get("rol", List.class),
          claims.getIssuedAt(),
          hardTTLSeconds,
          softTTLSeconds
      );
    }
    catch (SignatureException e){
      logger.debug("Token has incorrect signature " + token + " exception " + e.getMessage());
      return null;
    }
    catch (MalformedJwtException e){
      logger.debug("Token has malformed exception " + token + " exception " + e.getMessage());
      return null;
    }
    catch (ExpiredJwtException e){
      logger.debug("Token has expired exception " + token + " exception " + e.getMessage());
      return null;
    }
    catch (Exception e) {
      logger.error("Error unmarshalling token: " + token, e);
      return null;
    }
  }


  protected static String marshal(AccessToken token, AuthConfig config) {
    Map<String, Object> claims = Map.of(
        "rol", token.roles,
        CLAIM_HARD_TTL, token.getHardTTL() != null ? token.getHardTTL() :  (token.getIssuedAt().toInstant().getEpochSecond()  +  config.getHardTTL().getSeconds()),
        CLAIM_SOFT_TTL, token.getIssuedAt().toInstant().getEpochSecond() + config.getSoftTTL().getSeconds()
    );


    return Jwts.builder()
        .signWith(Keys.hmacShaKeyFor(config.getSecret().getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS512)
        .setIssuedAt(token.issuedAt)
        .setHeaderParam("typ", "JWT")
        .setSubject(token.msisdn)
        .addClaims(claims)
        .compact();
  }

 public static String asToken(String tokenHeader) {
    if (tokenHeader != null) {
      return tokenHeader.replace("Bearer ", "");
    }
    return null;
  }

  public static String asHeader(String token) {
    return "Bearer " + token;
  }



  public static class AccessToken {
    String msisdn;
    List<String> roles;
    Date issuedAt = new Date();

    private Long hardTTL;
    private Long softTTL;

    public AccessToken(String msisdn, List<String> roles, Date issuedAt, Long hardTTL, Long softTTL) {
      this.msisdn = msisdn;
      this.roles = roles;
      this.issuedAt = issuedAt;
      this.hardTTL = hardTTL;
      this.softTTL = softTTL;
    }
    public AccessToken() {

    }

    public String getMsisdn() {
      return msisdn;
    }

    public void setMsisdn(String msisdn) {
      this.msisdn = msisdn;
    }

    public List<String> getRoles() {
      return roles;
    }

    public void setRoles(List<String> roles) {
      this.roles = roles;
    }

    public Date getIssuedAt() {
      return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
      this.issuedAt = issuedAt;
    }

    public Long getHardTTL() {
      return hardTTL;
    }

    public void setHardTTL(Long hardTTL) {
      this.hardTTL = hardTTL;
    }

    public Long getSoftTTL() {
      return softTTL;
    }

    public void setSoftTTL(Long softTTL) {
      this.softTTL = softTTL;
    }
    public Instant getExpires() {
      return Instant.ofEpochSecond(Math.min(hardTTL, softTTL));
    }

    public static AccessToken token(Authentication authentication) {
      return (AccessToken) authentication.getDetails();
    }

    public static AccessToken token() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      return  authentication != null ? (AccessToken) authentication.getDetails() : null;
    }
  }
}