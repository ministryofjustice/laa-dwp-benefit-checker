package uk.gov.justice.laa.bc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.client.DwpSoapClient;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BenefitCheckerServiceTest {

  @InjectMocks
  private BenefitCheckerService benefitCheckerService;

  @Mock
  private DwpSoapClient dwpSoapClient;

  @Test
  public void performCheck() {
    BenefitCheckResponseBody benefitCheckResponseBody =
            benefitCheckerService.performCheck(
                    BenefitCheckRequestBody.builder().build()
            );

    assertEquals("Yes", benefitCheckResponseBody.getBenefitCheckerStatus());

    verify(dwpSoapClient, times(1)).performCheck();
  }
}
