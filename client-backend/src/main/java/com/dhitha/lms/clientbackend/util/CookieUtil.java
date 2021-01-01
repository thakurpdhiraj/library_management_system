package com.dhitha.lms.clientbackend.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class to work with {@link Cookie}
 *
 * @author Dhiraj
 */
public final class CookieUtil {

  public static void addCookie(
      String name, String value, boolean isHttpOnly, HttpServletResponse response) {

    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(isHttpOnly);
    response.addCookie(cookie);
  }

  public static void removeCookie(String name, HttpServletResponse response) {

    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}
