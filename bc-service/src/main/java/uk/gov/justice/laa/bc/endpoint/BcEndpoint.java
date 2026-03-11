package uk.gov.justice.laa.bc.endpoint;

import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import uk.gov.justice.laa.bc.service.BcService;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerResponse;

/**
 * Service to receive SOAP request in a modern JDK 25 / Spring Boot 4 way.
 */
@Slf4j
@RequiredArgsConstructor
@Endpoint
public class BcEndpoint {

  public static final String LOCAL_PART = "BenefitCheckerRequest";
  public static final String NAMESPACE_URI =
      "https://lsc.gov.uk/benefitchecker/service/1.0/API_1.0_Check";

  private final BcService bcService;

  /**
   * check.
   *
   * @param request BenefitCheckerRequest
   * @return BenefitCheckerResponse BenefitCheckerResponse
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART)
  @ResponsePayload
  public JAXBElement<BenefitCheckerResponse> check(
      @RequestPayload JAXBElement<BenefitCheckerRequest> request) {
    log.info("Got request nino {}", request.getValue().getNino());
    log.info("Got request client org id {}", request.getValue().getClientOrgId());
    BenefitCheckerResponse response = bcService.perform(request.getValue());
    return new JAXBElement<>(new QName("BenefitCheckerResponse"), BenefitCheckerResponse.class,
        response);
  }
}
