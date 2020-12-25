package com.dhitha.lms.clientbackend.config;

import com.dhitha.lms.clientbackend.client.AuthClient;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import com.dhitha.lms.clientbackend.util.Constants;
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
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/** @author Dhiraj */
@Component
@Log4j2
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final AuthClient authClient;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    log.info("Authentication filter called for : {}", request.getServletPath());
    Cookie sid = WebUtils.getCookie(request, Constants.ID_COOKIE_NAME);
    Cookie sig = WebUtils.getCookie(request, Constants.SIGNATURE_COOKIE_NAME);
    if (Objects.isNull(sid) || Objects.isNull(sig)) {
      log.info("Cookie not present");
      filterChain.doFilter(request, response);
      return;
    }

    String token = sid.getValue() + "." + sig.getValue();
    try {
      log.info("Cookies token: {}", token);
      AuthResponseDTO authResponseDTO = authClient.verifyToken("Bearer "+token);
      String idToken = authResponseDTO.getHeader() + "." +authResponseDTO.getPayload();
      String cookiePath = request.getContextPath() + "/";
      Cookie sidUpdated = new Cookie(Constants.ID_COOKIE_NAME, idToken);
      sidUpdated.setPath(cookiePath);
      response.addCookie(sidUpdated);
      Cookie sigUpdated = new Cookie(Constants.SIGNATURE_COOKIE_NAME, authResponseDTO.getSignature());
      sigUpdated.setHttpOnly(true);
      sigUpdated.setPath(cookiePath);
      response.addCookie(sigUpdated);
      UserDTO user = authResponseDTO.getUserDTO();
      log.info("Filter user: {} ", user);
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
