package uk.gov.justice.laa.bc.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

/**
 * BenefitCheckRequestValidatorTest.
 */
@ExtendWith(MockitoExtension.class)
public class BenefitCheckRequestValidatorTest {

  private BenefitCheckRequestValidation.BenefitCheckRequestValidator validator;
  private ConstraintValidatorContext ctx;
  private ConstraintValidatorContext.ConstraintViolationBuilder builder;

  @BeforeEach
  void setup() {
    validator = new BenefitCheckRequestValidation.BenefitCheckRequestValidator();
    ctx = mock(ConstraintValidatorContext.class);
    builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    // Default behaviour for any violation
    lenient().when(ctx.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
  }

  private BenefitCheckRequestBody validRequest() {
    BenefitCheckRequestBody request = new BenefitCheckRequestBody();
    request.setLscServiceName("SERVICE");
    request.setClientOrgId("ab_orgc_12_34");
    request.setClientUserId("cl_user_id_1234");
    request.setSurname("Doe");
    request.setClientReference("ABC123");
    request.setNino("AB123456C");
    request.setDateOfBirth("19900101");
    request.setDateOfAward("20200101");
    return request;
  }

  // ---------------------------------------------------------------------
  // TOP-LEVEL VALIDATOR TESTS
  // ---------------------------------------------------------------------

  @Test
  void validRequest_returnsTrue() {
    BenefitCheckRequestBody req = validRequest();

    boolean result = validator.isValid(req, ctx);
    assertTrue(result);
  }

  @Test
  void missingSurname_makesContentInvalid() {
    BenefitCheckRequestBody req = validRequest();
    req.setSurname("");

    boolean result = validator.isValid(req, ctx);

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(contains("Missing 'Surname'"));
  }

  @Test
  void missingServiceName_makesCredentialsInvalid() {
    BenefitCheckRequestBody req = validRequest();
    req.setLscServiceName("");

    boolean result = validator.isValid(req, ctx);

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(contains("LSCServiceName"));
  }

  // ---------------------------------------------------------------------
  // validateString()
  // ---------------------------------------------------------------------

  @Test
  void validateString_validValue_returnsTrue() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateString(
        ctx, "ABC", "TestField", 1, 5, false
    );

    assertTrue(result);
  }

  @Test
  void validateString_blankWhenNotAllowed_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateString(
        ctx, "", "TestField", 1, 5, false
    );

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(contains("Missing 'TestField'"));
  }

  @Test
  void validateString_lengthTooShort_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateString(
        ctx, "A", "TestField", 3, 5, false
    );

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'TestField'"));
  }

  @Test
  void validateString_lengthTooLong_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateString(
        ctx, "TOO_LONG_VALUE", "TestField", 1, 5, false
    );

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'TestField'"));
  }

  // ---------------------------------------------------------------------
  // validateDate()
  // ---------------------------------------------------------------------

  @Test
  void validateDate_validLength_returnsTrue() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateDate(
        ctx, "20240101", "DateOfBirth", false
    );

    assertTrue(result);
  }

  @Test
  void validateDate_missingWhenRequired_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateDate(
        ctx, "", "DateOfBirth", false
    );

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(contains("Missing 'DateOfBirth'"));
  }

  @Test
  void validateDate_wrongLength_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateDate(
        ctx, "20241", "DateOfBirth", false
    );

    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'DateOfBirth'"));
  }

  // ---------------------------------------------------------------------
  // validateNino()
  // ---------------------------------------------------------------------

  @Test
  void validateNino_valid_returnsTrue() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateNino(
        ctx, "AB123456C"
    );
    assertTrue(result);
  }

  @Test
  void validateNino_blank_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateNino(
        ctx, ""
    );
    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(contains("Missing 'NI' no."));
  }

  @Test
  void validateNino_wrongLength_returnsFalse() {
    boolean result = BenefitCheckRequestValidation.BenefitCheckRequestValidator.validateNino(
        ctx, "SHORT"
    );
    assertFalse(result);
    verify(ctx).buildConstraintViolationWithTemplate(
        contains("Error in 'NI' no. request parameter."));
  }
}
