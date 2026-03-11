package uk.gov.justice.laa.bc.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

  GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

  @Test
  void handleGenericException_returnsInternalServerErrorStatusAndErrorMessage() throws Exception {
    ResponseEntity<String> result = globalExceptionHandler.handleGenericException(
        new RuntimeException("Something went wrong"));

    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody()).isEqualTo("An unexpected application error has occurred.");
  }

  @Test
  void handleHttpMessageNotReadable() throws Exception {
    HttpMessageNotReadableException exception = Mockito.mock(HttpMessageNotReadableException.class);
    HttpHeaders headers = new HttpHeaders();
    HttpStatusCode status = HttpStatus.BAD_REQUEST;
    WebRequest webRequest = Mockito.mock(WebRequest.class);
    ResponseEntity<Object> result = globalExceptionHandler.handleHttpMessageNotReadable(
        exception, headers, status, webRequest);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void handleMethodArgumentNotValid() throws Exception {
    MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
    HttpHeaders headers = new HttpHeaders();
    HttpStatusCode status = HttpStatus.BAD_REQUEST;
    WebRequest webRequest = Mockito.mock(WebRequest.class);
    ResponseEntity<Object> result = globalExceptionHandler.handleMethodArgumentNotValid(
        exception, headers, status, webRequest);
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
