package ee.tafkin.config;

import ee.tafkin.errorhandling.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.list;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(HIGHEST_PRECEDENCE)
public class RequestLogFilter extends OncePerRequestFilter {
  static final String PRIVATE = "---PRIVATE---";

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    long start = currentTimeMillis();
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
    try {
      chain.doFilter(requestWrapper, responseWrapper);
    }
    catch (Throwable throwable) {
      correctStatusCodeIfNeeded(response, throwable);
      throw throwable;
    }
    finally {
      logRequest(requestWrapper, responseWrapper, start);
      responseWrapper.copyBodyToResponse();
    }
  }

  @Override
  public boolean shouldNotFilter(HttpServletRequest request) {
    return Stream.of("/actuator/*")
      .anyMatch(path -> ANT_PATH_MATCHER.match(path, request.getServletPath()));
  }

  void logRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long start) {
    log.info("[{} {}] [status={}] [time={}] [requestBody={}] [headers={}] [responseBody={}]", request.getMethod(), getRequestURI(request), response.getStatus(), currentTimeMillis() - start,
      getPayLoadFromByteArray(
        request.getContentAsByteArray(), request.getCharacterEncoding()), getHeadersAsCSV(request), getPayLoadFromByteArray(
        response.getContentAsByteArray(), response.getCharacterEncoding()));
  }

  String getRequestURI(HttpServletRequest request) {
    return request.getQueryString() == null
      ? request.getRequestURI()
      : request.getRequestURI() + "?" + request.getQueryString();
  }

  String getHeadersAsCSV(HttpServletRequest request) {
    return list(request.getHeaderNames()).stream()
      .map(key -> key + "=" + (isSensitiveData(key) ? PRIVATE : request.getHeader(key)))
      .collect(joining("; "));
  }

  void correctStatusCodeIfNeeded(HttpServletResponse httpServletResponse, Throwable throwable) {
    if (throwable instanceof CustomException) {
      httpServletResponse.setStatus(((CustomException) throwable).getHttpStatus());
      return;
    }
    httpServletResponse.setStatus(INTERNAL_SERVER_ERROR.value());
  }

  boolean isSensitiveData(String key) {
    return Stream.of("token", "password", "apikey", "authorization")
      .anyMatch(val -> containsIgnoreCase(key, val));
  }

  private String getPayLoadFromByteArray(byte[] requestBuffer, String charEncoding) {
    try {
      return new String(requestBuffer, charEncoding);
    }
    catch (UnsupportedEncodingException ex) {
      return "Unsupported-Encoding";
    }
  }
}
