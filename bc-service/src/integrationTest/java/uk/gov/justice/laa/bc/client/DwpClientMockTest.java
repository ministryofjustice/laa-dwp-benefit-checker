package uk.gov.justice.laa.bc.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.laa.bc.BenefitCheckerApplication;
import uk.gov.justice.laa.bc.service.BcService;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerResponse;

@SpringBootTest(classes = BenefitCheckerApplication.class)
public class DwpClientMockTest {
  @Autowired
  BcService service;

  @Test
  public void perform() {
    BenefitCheckerRequest request = new BenefitCheckerRequest();
    request.setNino("KS351154C");
    request.setSurname("Abley");
    request.setDateOfBirth("19700101");
    request.setDateOfAward("nil");
    BenefitCheckerResponse response = service.perform(request);
    assertTrue(response.getBenefitCheckerStatus().isEmpty());
  }
}
