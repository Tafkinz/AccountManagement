package ee.tafkin.errorhandling;

import org.springframework.http.HttpStatus;

public class InternalException extends CustomException {
  public InternalException(String message) {
    super(message);
  }

  @Override
  HttpStatus getStatusCode() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
