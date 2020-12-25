package com.dhitha.lms.clientbackend.config;

import com.dhitha.lms.clientbackend.dto.ErrorDTO;
import com.dhitha.lms.clientbackend.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration to protect from non internal calls
 *
 * @author Dhiraj
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationFilter authenticationFilter;

  private final ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/login")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .permitAll()
        .deleteCookies(Constants.ID_COOKIE_NAME, Constants.SIGNATURE_COOKIE_NAME)
        .logoutSuccessHandler(
            (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
        .and()
        .exceptionHandling()
        .accessDeniedHandler((request, response, accessDeniedException) -> createAccessDeniedError(response))
        .authenticationEntryPoint((request, response, authException) -> createError(response))
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin()
        .disable();
  }

  private void createError(HttpServletResponse response) throws IOException{
    ErrorDTO error =
        ErrorDTO.builder()
            .error("access_denied")
            .error_description("Access is Denied")
            .status(HttpServletResponse.SC_UNAUTHORIZED)
            .timestamp(LocalDateTime.now())
            .build();
    OutputStream out = response.getOutputStream();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(out, error);
    out.flush();
  }

  private void createAccessDeniedError(HttpServletResponse response) throws IOException {
    ErrorDTO error =
        ErrorDTO.builder()
            .error("forbidden")
            .error_description("Not enough permission")
            .status(HttpServletResponse.SC_FORBIDDEN)
            .timestamp(LocalDateTime.now())
            .build();
    OutputStream out = response.getOutputStream();
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(out, error);
    out.flush();
  }
}
