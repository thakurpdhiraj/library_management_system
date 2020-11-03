package com.dhitha.lms.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Add the lms-key header to every request to external services
 * @author Dhiraj
 */
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {

  @Value("${lms.client.key}")
  private String lmsKey;

  @Override
  public void apply(RequestTemplate template) {
    template.header("lms-key",lmsKey);
  }
}
