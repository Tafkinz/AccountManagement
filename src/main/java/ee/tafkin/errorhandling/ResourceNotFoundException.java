package ee.tafkin.errorhandling;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  @Override
  HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
