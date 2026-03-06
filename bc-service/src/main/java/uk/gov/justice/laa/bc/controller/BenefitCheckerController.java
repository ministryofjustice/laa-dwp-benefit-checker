package uk.gov.justice.laa.bc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.bc.api.BenefitsCheckerApi;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;
import uk.gov.justice.laa.bc.service.BenefitCheckerService;

/**
 * Controller for handling items requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class BenefitCheckerController implements BenefitsCheckerApi {
  private final BenefitCheckerService service;


  @Override
  public ResponseEntity<BenefitCheckResponseBody> benefitCheck(
          @RequestBody BenefitCheckRequestBody benefitCheckRequestBody) {
    log.info("YESS {}", benefitCheckRequestBody);
    return ResponseEntity.ok(service.performCheck(benefitCheckRequestBody));
  }
}
