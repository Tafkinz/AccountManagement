package ee.tafkin.errorhandling;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {
  public BadRequestException(String message) {
    super(message);
  }

  @Override
  HttpStatus getStatusCode() {
    return HttpStatus.BAD_REQUEST;
  }
}
