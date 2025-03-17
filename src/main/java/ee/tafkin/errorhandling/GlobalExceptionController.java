package ee.tafkin.errorhandling;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

  @ExceptionHandler({CustomException.class})
  @ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Invalid request input",
      content = @Content),
    @ApiResponse(responseCode = "404", description = "Resource not found",
      content = @Content)})
  public ResponseEntity<Object> handleCustomException(Exception ex) {
    CustomException customException = (CustomException) ex;
    ErrorResponse error = ErrorResponse.builder()
      .message(ex.getMessage())
      .stackTrace(getStackTrace(ex)).build();

    log.error(ex.getMessage(), ex);
    return new ResponseEntity<>(error, customException.getStatusCode());
  }

  @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
  @ApiResponse(responseCode = "400", description = "Invalid request input")
  public ResponseEntity<Object> handleValidationException(Exception ex) {
    ErrorResponse error = ErrorResponse.builder()
      .message(ex.getMessage())
      .stackTrace(getStackTrace(ex)).build();

    log.error(ex.getMessage(), ex);
    return new ResponseEntity<>(error, BAD_REQUEST);
  }
}
