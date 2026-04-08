package uk.gov.justice.laa.bc.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.core.WebServiceTemplate;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtRequest;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * DwpClient Test.
 */
@ExtendWith(MockitoExtension.class)
public class DwpClientTest {

  @InjectMocks
  private DwpClient client;

  @Mock
  private WebServiceTemplate webServiceTemplate;

  @Test
  public void givenDwpClient_whenPerformCheck_thenIsSuccess() {
    GetBenefitStatusExtRequest request = new GetBenefitStatusExtRequest();
    GetBenefitStatusExtResponse response = new GetBenefitStatusExtResponse();
    when(webServiceTemplate.marshalSendAndReceive(any())).thenReturn(response);

    assertNotNull(client.getBenefitStatusExtResponse(request));
  }
}