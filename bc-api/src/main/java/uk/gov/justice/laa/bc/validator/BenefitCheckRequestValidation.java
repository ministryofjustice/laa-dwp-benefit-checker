package uk.gov.justice.laa.bc.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

/**
 * Custom validator for BenefitCheckRequest.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BenefitCheckRequestValidation.BenefitCheckRequestValidator.class)
public @interface BenefitCheckRequestValidation {
  /**
   * message.
   */
  String message() default "This isn't correct";
  /**
   * groups.
   */
  Class[] groups() default {};
  /**
   * payload.
   */
  Class[] payload() default {};

  /**
   * BenefitCheckRequestValidator.
   */
  class BenefitCheckRequestValidator implements
      ConstraintValidator<BenefitCheckRequestValidation, BenefitCheckRequestBody> {
    @Override
    public boolean isValid(BenefitCheckRequestBody benefitCheckRequestBody,
                           ConstraintValidatorContext constraintValidatorContext) {
      boolean credentialsOk =
          validateCredentials(benefitCheckRequestBody, constraintValidatorContext);
      boolean contentOk =
          BenefitCheckerHelper.validateContent(benefitCheckRequestBody, constraintValidatorContext);
      return contentOk && credentialsOk;
    }

    /**
     * Validate the request Credentials
     * Note: This is a tactical impl. solution.
     * Security officer have stated that it is sufficient to leverage the firewall
     * with IP filtering and HTTPS certification to provide adequate authenicatation security.
     *
     * @param inboundWsRequest BenefitCheckerRequest
     * @return boolean
     */
    private boolean validateCredentials(BenefitCheckRequestBody inboundWsRequest,
                                        ConstraintValidatorContext constraintValidatorContext) {
      boolean credentialsOk = true;

      // request credentials.
      // SERVICE Context (Case sensitive!)
      String requestServiceName = inboundWsRequest.getLscServiceName();        // e.g. "xx_xxxx"
      credentialsOk &=
          BenefitCheckerHelper.validateString(constraintValidatorContext, requestServiceName,
              "LSCServiceName", BenefiteCheckerConstants.MAX_LEN_SERVICE_CONTEXT,
              BenefiteCheckerConstants.MAX_LEN_SERVICE_CONTEXT,
              !BenefiteCheckerConstants.TOLERATE_NULLS);


      // CLIENT GROUP (aka CLIENT ID Case sensitive!)
      String requestGroupId =
          inboundWsRequest.getClientOrgId();                // e.g. "xx_xxxx_xx_xx"
      credentialsOk &=
          BenefitCheckerHelper.validateString(constraintValidatorContext, requestGroupId,
              "ClientOrgId", BenefiteCheckerConstants.MAX_LEN_CLIENT_ORG_ID,
              BenefiteCheckerConstants.MAX_LEN_CLIENT_ORG_ID,
              !BenefiteCheckerConstants.TOLERATE_NULLS);


      // USERID (Case sensitive!)
      String requestUserId =
          inboundWsRequest.getClientUserId();                // e.g. "xx_xxxx_xx_xxxx"
      credentialsOk &=
          BenefitCheckerHelper.validateString(constraintValidatorContext, requestUserId,
              "ClientUserId", BenefiteCheckerConstants.MAX_LEN_CLIENT_USER_ID,
              BenefiteCheckerConstants.MAX_LEN_CLIENT_USER_ID,
              !BenefiteCheckerConstants.TOLERATE_NULLS);


      // What law of Demeter violation? Yes, this is horrible.
      //Configuration config = (Configuration) ((HttpServlet)MessageContext.getCurrentContext().
      // getProperty(HTTPConstants.MC_HTTP_SERVLET))
      // .getServletContext().getAttribute(Configuration.KEY);

      // We have the credentials, know perform the back end security check using them
      //credentialsOk = performSecurityCheck(inboundWSRequest, config);

      return credentialsOk;
    }
  }
}
