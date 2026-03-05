package uk.gov.justice.laa.bc.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtRequest;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtResponse;

/**
 * DwpClient.
 */
@Slf4j
public class DwpClient extends WebServiceGatewaySupport {

  /**
   * getBenefitStatusExtResponse.
   *
   * @param request GetBenefitStatusExtRequest
   * @return GetBenefitStatusExtResponse
   */
  public GetBenefitStatusExtResponse getBenefitStatusExtResponse(
          GetBenefitStatusExtRequest request) {

    log.info("Calling with {}", request.getNino());
    GetBenefitStatusExtResponse response = (GetBenefitStatusExtResponse) getWebServiceTemplate()
            .marshalSendAndReceive(request);
    return response;
  }
}