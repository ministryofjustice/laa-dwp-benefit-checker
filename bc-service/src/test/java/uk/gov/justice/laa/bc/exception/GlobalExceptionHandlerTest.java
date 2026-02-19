package uk.gov.justice.laa.bc.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

class GlobalExceptionHandlerTest {

  GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

  @Test
  void handleGenericException_returnsInternalServerErrorStatusAndErrorMessage() throws Exception {
    ResponseEntity<String> result = globalExceptionHandler.handleGenericException(new RuntimeException("Something went wrong"));

    assertThat(result).isNotNull();
    assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody()).isEqualTo("An unexpected application error has occurred.");
  }
}
