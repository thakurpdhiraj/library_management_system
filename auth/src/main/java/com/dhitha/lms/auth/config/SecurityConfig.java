package com.dhitha.lms.auth.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Security Configuration to protect from non internal calls
 *
 * @author Dhiraj
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${lms.client.key}")
  private String clientKey;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    AbstractPreAuthenticatedProcessingFilter filter = getApiAuthenticatingFilter();

    http.csrf()
        .disable()
        .addFilter(filter)
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin()
        .disable();
  }

  private AbstractPreAuthenticatedProcessingFilter getApiAuthenticatingFilter() {
    AbstractPreAuthenticatedProcessingFilter filter =
        new AbstractPreAuthenticatedProcessingFilter() {

          @Override
          protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
            return request.getHeader("lms-key");
          }

          @Override
          protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
            return "";
          }
        };
    filter.setAuthenticationManager(
        authentication -> {
          Object principal = authentication.getPrincipal();
          if (clientKey == null || !clientKey.equals(principal)) {
            throw new BadCredentialsException("Invalid Api Key");
          }
          authentication.setAuthenticated(true);
          return authentication;
        });
    return filter;
  }
}
