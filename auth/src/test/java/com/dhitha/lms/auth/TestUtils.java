package com.dhitha.lms.auth;

import com.dhitha.lms.auth.dto.UserDTO;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/** @author Dhiraj */
public final class TestUtils {

  public static UserDTO createMockUser() {
    return UserDTO.builder()
        .id(1L)
        .name("name")
        .username("username") // pass
        .accountNonExpired(true)
        .accountNonLocked(true)
        .enabled(true)
        .credentialsNonExpired(true)
        .createdAt("2020-12-26T00:00:00")
        .updatedAt("2020-12-26T00:00:00")
        .userRoles(Arrays.asList("ADMIN", "USER"))
        .build();
  }

  public static JWTClaimsSet createMockClaim() {
    return new Builder()
        .subject("1")
        .claim("roles", "ADMIN,USER")
        .claim("name", "name")
        .claim("username", "username")
        .claim("createdAt", "2020-12-26T00:00:00")
        .claim("updatedAt", "2020-12-26T00:00:00")
        .claim("accountNonExpired", true)
        .claim("accountNonLocked", true)
        .claim("enabled", true)
        .claim("credentialsNonExpired", true)
        .issueTime(Date.from(Instant.ofEpochSecond(20)))
        .build();
  }

  public static String createExpiredIdToken() {
    return "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJyb2xlcyI6IkFETUlOLFVTRVIiLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6ODA4MSIsImVuYWJsZWQiOnRydWUsImNyZWF0ZWRBdCI6IjIwMjAtMTItMjZUMDA6MDA6MDAiLCJuYmYiOjE2MDg5OTc3ODMsIm5hbWUiOiJuYW1lIiwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImV4cCI6MTYwODk5OTU4MywiaWF0IjoxNjA4OTk3NzgzLCJ1c2VybmFtZSI6InVzZXJuYW1lIiwidXBkYXRlZEF0IjoiMjAyMC0xMi0yNlQwMDowMDowMCIsImFjY291bnROb25Mb2NrZWQiOnRydWV9.fnOnbqtTVS5O-OiE2wxK4V0CpcL84TUcXSL2GlEmPd47r6dMmuidN5Vw2lStLHfErmPwwQaD4hzyYjIPYGvCQ3uyR8KQAazSN1KLeUJOsEFBJh95GRcJbZNVp3PJFlMrRragdJY5BZzlkGRn3tRcQWWEGVHlHBonJgAiwi_pME7XZ2WubPRs9qv1I6PYcaotuiA6s6zOe_jpEllduTs5_KuWY3fYo51Viud_B8rH60l_pJX1aJKMEXjhqiK8D_HO5bP6NDF_upaYagf6uQws4x3oH-l4XqqP7lFEbG4QU0J7ewehIW29IFEipcoK6iDXWTe9WDPp62HKA9p05729rw";
  }
}
