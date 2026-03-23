package uk.gov.justice.laa.bc.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.model.Configuration;
import uk.gov.justice.laa.bc.validator.ReturnCodes;

/**
 * Listener responsible for parsing the environment variables, and stopping
 * the application from starting if it isn't configured properly.
 */
@Service
public class ConfigurationService {

  private Configuration configuration;
  @Value("${credentials.service.context}")
  private String serviceName;
  @Value("${client.ids}")
  private String clientDefinitions;

  @PostConstruct
  public void initialize() {
    configuration = Configuration.parse(serviceName, clientDefinitions);
  }

  /**
   * Validate the request containsScopedPrincipal.
   *
   * @param constraintValidatorContext ConstraintValidatorContext
   * @param requestServiceName String
   * @param requestGroupId String
   * @param requestUserId String
   * @return boolean
   */
  public boolean containsScopedPrincipal(ConstraintValidatorContext constraintValidatorContext,
                                         String requestServiceName, String requestGroupId,
                                         String requestUserId) {
    if (!configuration.getServiceName().equals(requestServiceName)) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          ReturnCodes.MSG_CODE_VALIDATION_SVRNAME + "Did not match request 'Service Context' ["
              + requestServiceName + "] with those registered with the application");
      return false;
    }

    if (!configuration.getClientOrgs().containsKey(requestGroupId)) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          ReturnCodes.MSG_CODE_VALIDATION_CLIENTORG + "Did not match request 'Client Org. Id' ["
              + requestGroupId + "] with those registered with the application "
              + configuration.getClientOrgs().keySet());
      return false;
    }

    Collection<String> userIds = configuration.getClientOrgs().get(requestGroupId).getUserIds();

    if (!userIds.contains(requestUserId)) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          ReturnCodes.MSG_CODE_VALIDATION_USERID + "Did not match request 'Client User Id' ["
              + requestUserId + "] for Client [" + requestGroupId
              + "] with those registered with the application e.g. " + userIds);
      return false;
    }

    return true;
  }
}
