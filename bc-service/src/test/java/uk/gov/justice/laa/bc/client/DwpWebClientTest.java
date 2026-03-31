package uk.gov.justice.laa.bc.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.Item;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

@ExtendWith(MockitoExtension.class)
public class DwpWebClientTest {
  @Test
  void performCheck_shouldNotThrowAnything()
      throws Exception {

    DwpWebClient dwpWebClient = new DwpWebClient();
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setSurname("YESJOHN");
    request.setNino("AB123456C");
    request.setDateOfAward("20080201");
    request.setDateOfBirth("20000101");
    Item response = dwpWebClient.perform(request);
    assertNotNull(response);
    assertEquals("Yes", response.getValue());
  }
}
