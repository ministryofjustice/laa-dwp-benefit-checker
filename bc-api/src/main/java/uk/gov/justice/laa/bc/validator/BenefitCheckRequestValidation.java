package uk.gov.justice.laa.bc.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.Configuration;
import uk.gov.justice.laa.bc.service.ConfigurationService;

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

    @Autowired
    private ConfigurationService configurationService;

    public static final boolean TOLERATE_NULLS = true;
    public static final int MIN_LEN = 3;
    public static final int MAX_LEN_NINO = 9;
    public static final int MIN_LEN_SURNAME = 2;
    public static final int MAX_LEN_SURNAME = 50;
    public static final int MAX_LEN_DATE = 8;

    public static final int MAX_LEN_SERVICE_CONTEXT = 7;
    public static final int MAX_LEN_CLIENT_ORG_ID = 13;
    public static final int MAX_LEN_CLIENT_USER_ID = 15;
    public static final int MAX_LEN_CLIENT_REF = 50;
    
    @Override
    public boolean isValid(BenefitCheckRequestBody benefitCheckRequestBody,
                           ConstraintValidatorContext constraintValidatorContext) {
      boolean credentialsOk =
          validateCredentials(benefitCheckRequestBody, constraintValidatorContext);
      boolean contentOk =
          validateContent(benefitCheckRequestBody, constraintValidatorContext);
      return contentOk && credentialsOk;
    }

    /**
     * Validate the request Credentials
     * Note: This is a tactical impl. solution.
     * Security officer have stated that it is sufficient to leverage the firewall
     * with IP filtering and HTTPS certification to provide adequate authenicatation security.
     *
     * @param inboundWsRequest BenefitCheckerRequest
     * @param constraintValidatorContext ConstraintValidatorContext
     * @return boolean
     */
    private boolean validateCredentials(BenefitCheckRequestBody inboundWsRequest,
                                        ConstraintValidatorContext constraintValidatorContext) {
      boolean credentialsOk = true;

      // request credentials.
      // SERVICE Context (Case sensitive!)
      String requestServiceName = inboundWsRequest.getLscServiceName();        // e.g. "xx_xxxx"
      credentialsOk &= validateString(constraintValidatorContext, requestServiceName,
              "LSCServiceName", MAX_LEN_SERVICE_CONTEXT,
              MAX_LEN_SERVICE_CONTEXT,
              !TOLERATE_NULLS);


      // CLIENT GROUP (aka CLIENT ID Case sensitive!)
      String requestGroupId =
          inboundWsRequest.getClientOrgId();                // e.g. "xx_xxxx_xx_xx"
      credentialsOk &= validateString(constraintValidatorContext, requestGroupId,
              "ClientOrgId", MAX_LEN_CLIENT_ORG_ID,
              MAX_LEN_CLIENT_ORG_ID,
              !TOLERATE_NULLS);


      // USERID (Case sensitive!)
      String requestUserId =
          inboundWsRequest.getClientUserId();                // e.g. "xx_xxxx_xx_xxxx"
      credentialsOk &= validateString(constraintValidatorContext, requestUserId,
              "ClientUserId", MAX_LEN_CLIENT_USER_ID,
              MAX_LEN_CLIENT_USER_ID,
              !TOLERATE_NULLS);


      // What law of Demeter violation? Yes, this is horrible.
      //Configuration config = (Configuration) ((HttpServlet)MessageContext.getCurrentContext().
      // getProperty(HTTPConstants.MC_HTTP_SERVLET))
      // .getServletContext().getAttribute(Configuration.KEY);

      // We have the credentials, know perform the back end security check using them
      credentialsOk &= performSecurityCheck(constraintValidatorContext, inboundWsRequest);

      return credentialsOk;
    }

    /**
     * Validate the request Content.
     *
     * @param inboundWsRequest BenefitCheckerRequest
     * @return boolean
     */
    protected static boolean validateContent(BenefitCheckRequestBody inboundWsRequest,
                                          ConstraintValidatorContext constraintValidatorContext) {

      boolean retVal = false;

      // Main client request params
      boolean nameOk =
          validateString(constraintValidatorContext, inboundWsRequest.getSurname(), "Surname",
              MIN_LEN_SURNAME, MAX_LEN_SURNAME,
              !TOLERATE_NULLS);
      boolean refOk =
          validateString(constraintValidatorContext, inboundWsRequest.getClientReference(),
              "Reference", MIN_LEN,
              MAX_LEN_CLIENT_REF,
              !TOLERATE_NULLS);
      boolean ninoOk = validateNino(constraintValidatorContext, inboundWsRequest.getNino());
      boolean dobOk =
          validateDate(constraintValidatorContext, inboundWsRequest.getDateOfBirth(), "DateOfBirth",
              !TOLERATE_NULLS);
      boolean doaOk =
          validateDate(constraintValidatorContext, inboundWsRequest.getDateOfAward(), "DateOfAward",
              TOLERATE_NULLS);


      retVal = nameOk && ninoOk && dobOk && doaOk && refOk;

      return retVal;
    }

    /**
     * Validate String property.
     * check Max length=8 chars
     *
     * @param propertyValue propertyValue
     * @param propertyName propertyName
     * @param maxLength     where value is -1 do not impose length check
     * @param tolerateNull tolerateNull
     * @return boolean
     */
    protected static boolean validateString(ConstraintValidatorContext constraintValidatorContext,
                                         String propertyValue, String propertyName, int minLength,
                                         int maxLength, boolean tolerateNull) {
      boolean retVal = false;

      // check for blank
      if (StringUtils.isEmpty(propertyValue) && !tolerateNull) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                ReturnCodes.MSG_CODE_VALIDATION_BLANK
                    + " Missing '" + propertyName + "'")
            .addConstraintViolation();
      } else {
        // check for max length
        if ((minLength != -1 & maxLength != -1)
            && (propertyValue.length() < minLength || propertyValue.length() > maxLength)) {
          constraintValidatorContext.buildConstraintViolationWithTemplate(
              ReturnCodes.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '"
                  + propertyName + "'");
        } else {
          retVal = true;
        }
      }
      return retVal;
    }

    /**
     * Validate Date property
     * Format expected to be CCYYMMDD. Max length=8 chars
     *
     * @param dateString dateString
     * @param propertyName propertyName
     * @param tolerateNull - Enforce Null check.
     * @return boolean
     */
    protected static boolean validateDate(ConstraintValidatorContext constraintValidatorContext,
                                       String dateString, String propertyName,
                                       boolean tolerateNull) {
      //logger.debug("validateDate() start");
      boolean retVal = true;

      // check for blank
      if (StringUtils.isBlank(dateString) && !tolerateNull) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            ReturnCodes.MSG_CODE_VALIDATION_BLANK + " Missing '" + propertyName + "'");
        retVal = false;
      } else {

        if (StringUtils.isBlank(dateString)) {
          if (!TOLERATE_NULLS) {
            retVal = false;
          }
        } else {
          // check for max length
          if (dateString.length() == MAX_LEN_DATE) {
            //Potentially could check Date range against known datum < todays date > 20000101
            // if invalid :
            // throw new ApplicationException(MSG_CODE_VALIDATION_FORMAT,
            // "Error in request parameter '"+propertyName+"'");

          } else {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                ReturnCodes.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '"
                    + propertyName + "'");
            retVal = false;
          }
        }
      }
      //logger.debug("validateDate() end '"+propertyName+"'["+retVal+"]");
      return retVal;
    }

    /**
     * Validate the 'nino'
     * Simply check for blanks in a mandatory field.. or length to not exceed
     * Note:
     * Could use RegEx to check for structure....
     *
     * @param nationalInsuranceNumber nationalInsuranceNumber
     * @return boolean
     */
    protected static boolean validateNino(ConstraintValidatorContext constraintValidatorContext,
                                       String nationalInsuranceNumber) {
      boolean retVal = false;

      // check for blank
      if (StringUtils.isBlank(nationalInsuranceNumber)) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            ReturnCodes.MSG_CODE_VALIDATION_BLANK + " Missing 'NI' no.");

      } else {

        // check for max length
        if (nationalInsuranceNumber.length() == MAX_LEN_NINO) {
          // Potentially could check RegEx expression - e.g. "[a-zA-Z]{2}[0-9]{6}[a-dA-D]?"
          retVal = true;

        } else {
          constraintValidatorContext.buildConstraintViolationWithTemplate(
              ReturnCodes.MSG_CODE_VALIDATION_SIZE
                  + " Error in 'NI' no. request parameter.");
        }
      }
      //logger.debug("validateNino() end ["+retVal+"]");
      return retVal;
    }

    /**
     * Check authorisation for inbound Benefit Check Request.
     * Use the Clients credentials embedded in the request message.
     * Note: The credentials are compared against those
     * defined in the configuration of this service.
     * At present this is an application properties file.
     * In future releases this will be changed to a
     * better more extensible external solution (LDAP via the common code).
     *
     * @param inboundWsRequest inboundWsRequest
     * @return boolean
     */
    private boolean performSecurityCheck(ConstraintValidatorContext constraintValidatorContext,
                                         BenefitCheckRequestBody inboundWsRequest) {

      // i.g. "xx_xxxx_xx_xx"
      String requestGroupId = inboundWsRequest.getClientOrgId();
      // i.g. "xx_xxxx"
      String requestServiceName = inboundWsRequest.getLscServiceName();
      // i.g. "xx_xxxx_xx_xxxx"
      String requestUserId = inboundWsRequest.getClientUserId();

      return configurationService.containsScopedPrincipal(
          constraintValidatorContext, requestServiceName, requestGroupId, requestUserId);
    }
  }
}
