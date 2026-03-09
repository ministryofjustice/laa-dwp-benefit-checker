package uk.gov.justice.laa.bc.endpoint;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.service.BcService;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerResponse;

@ExtendWith(MockitoExtension.class)
class BcEndpointTest {

  @InjectMocks
  private BcEndpoint bcEndpoint;

  @Mock
  private BcService bcService;

  @Test
  void check() {
    when(bcService.perform(any())).thenReturn(new BenefitCheckerResponse());
    BenefitCheckerRequest request = new BenefitCheckerRequest();
    request.setNino("nino");
    request.setClientOrgId("client-org-id");
    JAXBElement element = new JAXBElement<>(new QName("BenefitCheckerRequest"),
        BenefitCheckerRequest.class,
        request);
    JAXBElement<BenefitCheckerResponse> response = bcEndpoint.check(element);
    assertNotNull(response);
    assertEquals(response.getName().getLocalPart(), "BenefitCheckerResponse");
  }
}