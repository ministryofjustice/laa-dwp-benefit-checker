package uk.gov.justice.laa.bc.validator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.exception.BadRequestException;
import uk.gov.justice.laa.bc.service.AuthorisationService;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;

/**
 * Validates requests.
 */
@RequiredArgsConstructor
@Service
public class BenefitCheckRequestValidator {

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


  private final AuthorisationService authorisationService;

  public void validate(BenefitCheckerRequest benefitCheckRequestBody) {
    validateCredentials(benefitCheckRequestBody);
    validateContent(benefitCheckRequestBody);
  }

  /**
   * Validate the request Credentials
   * Note: This is a tactical impl. solution.
   * Security officer have stated that it is sufficient to leverage the firewall
   * with IP filtering and HTTPS certification to provide adequate authenicatation security.
   *
   * @param inboundWsRequest BenefitCheckerRequest
   */
  private void validateCredentials(BenefitCheckerRequest inboundWsRequest) {

    // request credentials.
    // SERVICE Context (Case sensitive!)
    String requestServiceName = inboundWsRequest.getLscServiceName();        // e.g. "xx_xxxx"

    validateString(requestServiceName,
            "LSCServiceName", MAX_LEN_SERVICE_CONTEXT,
            MAX_LEN_SERVICE_CONTEXT);


    // CLIENT GROUP (aka CLIENT ID Case sensitive!)
    String requestGroupId =
            inboundWsRequest.getClientOrgId();                // e.g. "xx_xxxx_xx_xx"

    validateString(requestGroupId,
            "ClientOrgId", MAX_LEN_CLIENT_ORG_ID,
            MAX_LEN_CLIENT_ORG_ID);


    // USERID (Case sensitive!)
    String requestUserId =
            inboundWsRequest.getClientUserId();                // e.g. "xx_xxxx_xx_xxxx"

    validateString(requestUserId,
            "ClientUserId", MAX_LEN_CLIENT_USER_ID,
            MAX_LEN_CLIENT_USER_ID);


    // What law of Demeter violation? Yes, this is horrible.
    //Configuration config = (Configuration) ((HttpServlet)MessageContext.getCurrentContext().
    // getProperty(HTTPConstants.MC_HTTP_SERVLET))
    // .getServletContext().getAttribute(Configuration.KEY);

    // We have the credentials, know perform the back end security check using them
    authorisationService.performSecurityCheck(inboundWsRequest);
  }

  /**
   * Validate the request Content.
   *
   * @param inboundWsRequest BenefitCheckerRequest
   */
  protected static void validateContent(BenefitCheckerRequest inboundWsRequest) {

    // Main client request params
    validateString(inboundWsRequest.getSurname(), "Surname",
            MIN_LEN_SURNAME, MAX_LEN_SURNAME);
    validateString(inboundWsRequest.getClientReference(),
            "Reference", MIN_LEN,
            MAX_LEN_CLIENT_REF);

    validateNino(inboundWsRequest.getNino());
    validateDate(inboundWsRequest.getDateOfBirth(), "DateOfBirth",
            !TOLERATE_NULLS);

    validateDate(inboundWsRequest.getDateOfAward(), "DateOfAward",
            TOLERATE_NULLS);
  }

  /**
   * Validate String property.
   * check Max length=8 chars
   *
   * @param propertyValue propertyValue
   * @param propertyName  propertyName
   * @param maxLength     where value is -1 do not impose length check
   */
  protected static void validateString(
          String propertyValue, String propertyName, int minLength,
          int maxLength) {

    // check for blank
    if (StringUtils.isEmpty(propertyValue)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_BLANK
                      + " Missing '" + propertyName + "'");
    } else {
      // check for max length
      if ((minLength != -1 & maxLength != -1)
              && (propertyValue.length() < minLength || propertyValue.length() > maxLength)) {
        throw new BadRequestException(
                ReturnCodes.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '"
                        + propertyName + "'");
      }
    }
  }

  /**
   * Validate Date property
   * Format expected to be CCYYMMDD. Max length=8 chars
   *
   * @param dateString   dateString
   * @param propertyName propertyName
   * @param tolerateNull - Enforce Null check.
   */
  protected static void validateDate(
          String dateString, String propertyName,
          boolean tolerateNull) {
    //logger.debug("validateDate() start");

    // check for blank
    if (StringUtils.isBlank(dateString) && !tolerateNull) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_BLANK + " Missing '" + propertyName + "'");
    } else {
      if (!StringUtils.isBlank(dateString)) {
        // check for max length
        if (dateString.length() != MAX_LEN_DATE) {
          throw new BadRequestException(
                  ReturnCodes.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '"
                          + propertyName + "'");
        }
      }
    }
  }

  /**
   * Validate the 'nino'
   * Simply check for blanks in a mandatory field.. or length to not exceed
   * Note:
   * Could use RegEx to check for structure....
   *
   * @param nationalInsuranceNumber nationalInsuranceNumber
   */
  protected static void validateNino(String nationalInsuranceNumber) {

    // check for blank
    if (StringUtils.isBlank(nationalInsuranceNumber)) {
      throw new BadRequestException(
              ReturnCodes.MSG_CODE_VALIDATION_BLANK + " Missing 'NI' no.");

    } else {

      // check for max length
      if (nationalInsuranceNumber.length() == MAX_LEN_NINO) {
        // Potentially could check RegEx expression - e.g. "[a-zA-Z]{2}[0-9]{6}[a-dA-D]?"

      } else {
        throw new BadRequestException(
                ReturnCodes.MSG_CODE_VALIDATION_SIZE
                        + " Error in 'NI' no. request parameter.");
      }
    }
  }


}
