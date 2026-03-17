package uk.gov.justice.laa.bc.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

public class BenefitCheckerHelper {
  /**
   * Validate the request Content.
   *
   * @param inboundWSRequest BenefitCheckerRequest
   * @return boolean
   */
  public static boolean validateContent(BenefitCheckRequestBody inboundWSRequest,
                                        ConstraintValidatorContext constraintValidatorContext) {

    boolean retVal = false;

    // Main client request params
    boolean nameOk =
        validateString(constraintValidatorContext, inboundWSRequest.getSurname(), "Surname",
            BenefiteCheckerConstants.MIN_LEN_SURNAME, BenefiteCheckerConstants.MAX_LEN_SURNAME,
            !BenefiteCheckerConstants.TOLERATE_NULLS);
    boolean refOk =
        validateString(constraintValidatorContext, inboundWSRequest.getClientReference(),
            "Reference", BenefiteCheckerConstants.MIN_LEN,
            BenefiteCheckerConstants.MAX_LEN_CLIENT_REF, !BenefiteCheckerConstants.TOLERATE_NULLS);
    boolean ninoOk = validateNino(constraintValidatorContext, inboundWSRequest.getNino());
    boolean dobOk =
        validateDate(constraintValidatorContext, inboundWSRequest.getDateOfBirth(), "DateOfBirth",
            !BenefiteCheckerConstants.TOLERATE_NULLS);
    boolean doaOk =
        validateDate(constraintValidatorContext, inboundWSRequest.getDateOfAward(), "DateOfAward",
            BenefiteCheckerConstants.TOLERATE_NULLS);


    retVal = nameOk && ninoOk && dobOk && doaOk && refOk;

    return retVal;
  }

  /**
   * Validate String property
   * <p>
   * check Max length=8 chars
   *
   * @param propertyValue
   * @param propertyName
   * @param maxLength     where value is -1 do not impose length check
   * @param tolerateNull
   * @return boolean
   */
  public static boolean validateString(ConstraintValidatorContext constraintValidatorContext,
                                       String propertyValue, String propertyName, int minLength,
                                       int maxLength, boolean tolerateNull) {
    boolean retVal = false;

    // check for blank
    if (StringUtils.isEmpty(propertyValue) && !tolerateNull) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
              BenefiteCheckerConstants.MSG_CODE_VALIDATION_BLANK + " Missing '" + propertyName + "'")
          .addConstraintViolation();
    } else {
      // check for max length
      if ((minLength != -1 & maxLength != -1) &&
          (propertyValue.length() < minLength || propertyValue.length() > maxLength)) {
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            BenefiteCheckerConstants.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '" +
                propertyName + "'");
      } else {
        retVal = true;
      }
    }
    return retVal;
  }

  /**
   * Validate Date property
   * <p>
   * Format expected to be CCYYMMDD. Max length=8 chars
   *
   * @param dateString
   * @param propertyName
   * @param tolerateNull - Enforce Null check.
   * @return boolean
   */
  public static boolean validateDate(ConstraintValidatorContext constraintValidatorContext,
                                     String dateString, String propertyName, boolean tolerateNull) {
//		logger.debug("validateDate()	start");
    boolean retVal = true;

    // check for blank
    if (StringUtils.isBlank(dateString) && !tolerateNull) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          BenefiteCheckerConstants.MSG_CODE_VALIDATION_BLANK + " Missing '" + propertyName + "'");
      retVal = false;
    } else {

      if (StringUtils.isBlank(dateString)) {
        if (!BenefiteCheckerConstants.TOLERATE_NULLS) {
          retVal = false;
        }
      } else {
        // check for max length
        if (dateString.length() == BenefiteCheckerConstants.MAX_LEN_DATE) {
          //Potentially could check Date range against known datum < todays date > 20000101
          // if invalid : throw new ApplicationException(BenefiteCheckerConstants.MSG_CODE_VALIDATION_FORMAT, "Error in request parameter '"+propertyName+"'");

        } else {
          constraintValidatorContext.buildConstraintViolationWithTemplate(
              BenefiteCheckerConstants.MSG_CODE_VALIDATION_SIZE + " Error in request parameter '" +
                  propertyName + "'");
          retVal = false;
        }
      }
    }

//		logger.debug("validateDate()	end '"+propertyName+"'["+retVal+"]");
    return retVal;

  }

  /**
   * Validate the 'nino'
   * Simply check for blanks in a mandatory field.. or length to not exceed
   * <p>
   * Note:
   * Could use RegEx to check for structure....
   *
   * @param nationalInsuranceNumber
   * @return boolean
   */
  public static boolean validateNino(ConstraintValidatorContext constraintValidatorContext,
                                     String nationalInsuranceNumber) {
    boolean retVal = false;

    // check for blank
    if (StringUtils.isBlank(nationalInsuranceNumber)) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          BenefiteCheckerConstants.MSG_CODE_VALIDATION_BLANK + " Missing 'NI' no.");

    } else {

      // check for max length
      if (nationalInsuranceNumber.length() == BenefiteCheckerConstants.MAX_LEN_NINO) {
        // Potentially could check RegEx expression - e.g. "[a-zA-Z]{2}[0-9]{6}[a-dA-D]?"
        retVal = true;

      } else {
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            BenefiteCheckerConstants.MSG_CODE_VALIDATION_SIZE +
                " Error in 'NI' no. request parameter.");
      }
    }

//		logger.debug("validateNino()	end ["+retVal+"]");
    return retVal;
  }
}
