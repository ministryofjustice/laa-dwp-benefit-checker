package uk.gov.justice.laa.bc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.client.DwpSoapClient;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;

/**
 * Service class for handling items requests.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BenefitCheckerService {

  private final DwpSoapClient dwpSoapClient;

  /**
   * Performs a benefit check.
   *
   * @param request the request.
   * @return the result of the request.
   */
  public BenefitCheckResponseBody performCheck(@Nullable BenefitCheckRequestBody request) {
    log.info("Hi the request is {}", request);

    dwpSoapClient.performCheck();

    // TODO: Call DWP
    return BenefitCheckResponseBody.builder().benefitCheckerStatus("Yes").originalClientRef(request.getClientReference())
            //.confirmationRef()
            .build();
  }
}
