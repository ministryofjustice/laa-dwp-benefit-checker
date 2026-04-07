package uk.gov.justice.laa.bc.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.exception.BadRequestException;
import uk.gov.justice.laa.bc.validator.ReturnCodes;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;

/**
 * Authorises clients.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorisationService {

  private final ConfigurationService configurationService;

  /**
   * Check authorisation for inbound Benefit Check Request.
   * Use the Clients credentials embedded in the request message.
   * Note: The credentials are compared against those
   * defined in the configuration of this service.
   * At present this is an application properties file.
   * In future releases this will be changed to a
   * better more extensible external solution (LDAP via the common code).
   *
   * @param benefitCheckerRequest benefitCheckerRequest
   */
  public void performSecurityCheck(BenefitCheckerRequest benefitCheckerRequest) {

    // i.g. "xx_xxxx_xx_xx"
    String requestGroupId = benefitCheckerRequest.getClientOrgId();
    // i.g. "xx_xxxx"
    String requestServiceName = benefitCheckerRequest.getLscServiceName();
    // i.g. "xx_xxxx_xx_xxxx"
    String requestUserId = benefitCheckerRequest.getClientUserId();

    if (!configurationService.getConfiguration().getServiceName().equals(requestServiceName)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_SVRNAME + "Did not match request 'Service Context' ["
                      + requestServiceName + "] with those registered with the application");
    }

    if (!configurationService.getConfiguration().getClientOrgs().containsKey(requestGroupId)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_CLIENTORG + "Did not match request 'Client Org. Id' ["
                      + requestGroupId + "] with those registered with the application "
                      + configurationService.getConfiguration().getClientOrgs().keySet());
    }

    Collection<String> userIds = configurationService.getConfiguration()
            .getClientOrgs().get(requestGroupId).getUserIds();

    if (!userIds.contains(requestUserId)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_USERID + "Did not match request 'Client User Id' ["
                      + requestUserId + "] for Client [" + requestGroupId
                      + "] with those registered with the application e.g. " + userIds);
    }

  }

  /**
   * Validate the request containsScopedPrincipal.
   *
   * @param requestServiceName String
   * @param requestGroupId     String
   * @param requestUserId      String
   */
  public void containsScopedPrincipal(String requestServiceName, String requestGroupId,
                                      String requestUserId) {
    if (!configurationService.getConfiguration().getServiceName().equals(requestServiceName)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_SVRNAME + "Did not match request 'Service Context' ["
                      + requestServiceName + "] with those registered with the application");
    }

    if (!configurationService.getConfiguration().getClientOrgs().containsKey(requestGroupId)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_CLIENTORG + "Did not match request 'Client Org. Id' ["
                      + requestGroupId + "] with those registered with the application "
                      + configurationService.getConfiguration().getClientOrgs().keySet());
    }

    Collection<String> userIds = configurationService.getConfiguration().getClientOrgs()
            .get(requestGroupId).getUserIds();

    if (!userIds.contains(requestUserId)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_USERID + "Did not match request 'Client User Id' ["
                      + requestUserId + "] for Client [" + requestGroupId
                      + "] with those registered with the application e.g. " + userIds);
    }
  }
}
