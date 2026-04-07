package uk.gov.justice.laa.bc.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.bc.api.BenefitsCheckerApi;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;
import uk.gov.justice.laa.bc.service.BcWebService;

/**
 * Controller for handling items requests via http.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class BcWebController implements BenefitsCheckerApi {
  private final BcWebService service;


  @Override
  public ResponseEntity<BenefitCheckResponseBody> benefitCheck(
          @RequestBody BenefitCheckRequestBody benefitCheckRequestBody) {
    log.info("YESS {}", benefitCheckRequestBody);
    try {
      return ResponseEntity.ok(service.performCheck((benefitCheckRequestBody)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
