package uk.gov.justice.laa.bc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.client.SoapFaultClientException;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtRequest;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtResponse;
import uk.gov.justice.laa.bc.client.DwpClient;
import uk.gov.justice.laa.bc.exception.ValidationException;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerResponse;

/**
 * BcService.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BcService {

  private final DwpClient dwpClient;

  /**
   * perform.
   *
   * @param request BenefitCheckerRequest
   * @return BenefitCheckerResponse
   */
  public BenefitCheckerResponse perform(BenefitCheckerRequest request) {

    final BenefitCheckerResponse response = new BenefitCheckerResponse();

    // TODO: Validate client id etc..
    GetBenefitStatusExtRequest dwpRequest = new GetBenefitStatusExtRequest();
    dwpRequest.setNino(request.getNino());
    dwpRequest.setSurname(request.getSurname());
    dwpRequest.setDateOfAward(request.getDateOfAward());
    dwpRequest.setDateOfBirth(request.getDateOfBirth());

    GetBenefitStatusExtResponse dwpResponse = dwpClient.getBenefitStatusExtResponse(dwpRequest);

    dwpResponse.getItemList().forEach(item -> {
      log.info("Got item {}", item);
    });

    return response;
  }
}
