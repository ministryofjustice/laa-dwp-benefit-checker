package uk.gov.justice.laa.bc.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to generate 400.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

  String errorCode;
  String message;
}
