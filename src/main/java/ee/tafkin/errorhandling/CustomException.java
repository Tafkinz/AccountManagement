package ee.tafkin.errorhandling;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
  public CustomException(String message) {
    super(message);
  }

  public CustomException() {
    super();
  }

  abstract HttpStatus getStatusCode();

  public int getHttpStatus() {
    return getStatusCode().value();
  }
}
