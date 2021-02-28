package com.dhitha.lms.clientbackend.config;

import com.dhitha.lms.clientbackend.client.AuthClient;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import com.dhitha.lms.clientbackend.util.Constants;
import com.dhitha.lms.clientbackend.util.CookieUtil;
import feign.FeignException.FeignClientException;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * Filter to verify and refresh session cookie. On each request if the token are valid, updated
 * tokens are set in the session which increases the duration of session expiry
 *
 * @author Dhiraj
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final AuthClient authClient;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    Cookie sid = WebUtils.getCookie(request, Constants.ID_COOKIE_NAME);
    Cookie sig = WebUtils.getCookie(request, Constants.SIGNATURE_COOKIE_NAME);
    if (Objects.isNull(sid) || Objects.isNull(sig)) {
      log.info("Cookie not present");
      filterChain.doFilter(request, response);
      return;
    }

    String token = sid.getValue() + "." + sig.getValue();
    try {
      AuthResponseDTO authResponseDTO = authClient.verifyToken("Bearer " + token);
      String idToken = authResponseDTO.getHeader() + "." + authResponseDTO.getPayload();
      CookieUtil.addCookie(Constants.ID_COOKIE_NAME, idToken, false, response);
      CookieUtil.addCookie(
          Constants.SIGNATURE_COOKIE_NAME, authResponseDTO.getSignature(), true, response);
      UserDTO user = authResponseDTO.getUserDTO();
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
              user,
              null,
              user.getUserRoles().stream()
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toList()));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    } catch (FeignClientException e) {
      log.error("Token validation failed", e);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getServletPath().equals("/login");
  }
}
