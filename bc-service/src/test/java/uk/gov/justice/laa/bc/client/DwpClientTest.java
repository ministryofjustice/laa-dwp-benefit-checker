package uk.gov.justice.laa.bc.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtRequest;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtResponse;
import uk.gov.justice.laa.bc.config.WebServiceClientConfig;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebServiceClientConfig.class, loader = AnnotationConfigContextLoader.class)
public class DwpClientTest {

  @Autowired
  DwpClient client;

  @Test
  public void givenDwpClient_whenPerformCheck_thenIsSuccess() {

    GetBenefitStatusExtRequest request = new GetBenefitStatusExtRequest();
    request.setNino("foo");
    GetBenefitStatusExtResponse response = client.getBenefitStatusExtResponse(request);
  //  assertEquals("Warsaw", response.getCountry().getCapital());
  }
}