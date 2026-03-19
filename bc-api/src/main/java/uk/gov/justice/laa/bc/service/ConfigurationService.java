package uk.gov.justice.laa.bc.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
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

    @PostConstruct
    public void initialize() {
//        configuration = Configuration.parse(
//            System.getenv(BenefiteCheckerConstants.CONFIG_SVR_CONTEXT),
//            System.getenv(BenefiteCheckerConstants.CONFIG_CLIENT_IDS)
//        );
        configuration = Configuration.parse("hello", "slab:james,graham;maat:meri,vera,beccy");
    }

    public boolean containsScopedPrincipal(ConstraintValidatorContext constraintValidatorContext, String requestServiceName, String requestGroupId, String requestUserId) {
        if (!configuration.getServiceName().equals(requestServiceName)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                ReturnCodes.MSG_CODE_VALIDATION_SVRNAME + "Did not match request 'Service Context' [" + requestServiceName + "] with those registered with the application");
        }

        if (!configuration.getClientOrgs().containsKey(requestGroupId)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                ReturnCodes.MSG_CODE_VALIDATION_CLIENTORG + "Did not match request 'Client Org. Id' [" + requestGroupId + "] with those registered with the application " + configuration.getClientOrgs().keySet().toString());
        }

        Collection<String> userIds = configuration.getClientOrgs().get(requestGroupId).getUserIds();

        if (!userIds.contains(requestUserId)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                ReturnCodes.MSG_CODE_VALIDATION_USERID + "Did not match request 'Client User Id' [" + requestUserId + "] for Client [" + requestGroupId + "] with those registered with the application e.g. " + userIds.toString());
        }

        return true;
    }
}
