package uk.gov.justice.laa.bc.endpoint;

import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import uk.gov.lsc.benefitchecker.data._1.BenefitCheckerRequest;
import uk.gov.lsc.benefitchecker.data._1.BenefitCheckerResponse;

import javax.xml.namespace.QName;

/**
 * Service to receive SOAP request in a modern JDK 25 / Spring Boot 4 way
 */
@Slf4j
@Endpoint
public class BcEndpoint {

  public static final String LOCAL_PART = "check";
  public static final String NAMESPACE_URI = "https://lsc.gov.uk/benefitchecker/service/1.0/API_1.0_Check";

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART)
  @ResponsePayload
  public JAXBElement<BenefitCheckerResponse> check(@RequestPayload JAXBElement<BenefitCheckerRequest> request) {
    log.info("Got request {}", request.getValue());
    BenefitCheckerResponse response = new BenefitCheckerResponse();
    response.setBenefitCheckerStatus("No");
    response.setConfirmationRef("a ref");
    response.setOriginalClientRef("orig");
    return new JAXBElement<>(new QName("benefitCheckerResponse"), BenefitCheckerResponse.class, response);
  }
}
