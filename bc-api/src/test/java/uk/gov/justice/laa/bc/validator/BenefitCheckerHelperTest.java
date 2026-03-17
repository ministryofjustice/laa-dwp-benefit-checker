package uk.gov.justice.laa.bc.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

/**
 * BenefitCheckerHelperTest.
 */
@ExtendWith(MockitoExtension.class)
public class BenefitCheckerHelperTest {

  private ConstraintValidatorContext context;
  private ConstraintViolationBuilder builder;

  @BeforeEach
  void setup() {
    context = mock(ConstraintValidatorContext.class);
    builder = mock(ConstraintViolationBuilder.class);
    lenient().when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    lenient().when(builder.addConstraintViolation()).thenReturn(context);
  }

  // -------------------------------------------------------
  // validateString()
  // -------------------------------------------------------

  @Test
  void validateString_shouldFailWhenBlankAndNotTolerated() {
    boolean result = BenefitCheckerHelper.validateString(
        context, "", "Surname", 1, 10, false);

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(contains("Missing 'Surname'"));
  }

  @Test
  void validateString_shouldFailWhenTooShort() {
    boolean result = BenefitCheckerHelper.validateString(
        context, "A", "Surname", 2, 10, true);

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'Surname'"));
  }

  @Test
  void validateString_shouldFailWhenTooLong() {
    boolean result = BenefitCheckerHelper.validateString(
        context, "ABCDEFGHIJK", "Surname", 1, 5, true);

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'Surname'"));
  }

  @Test
  void validateString_shouldPassValidLength() {
    boolean result = BenefitCheckerHelper.validateString(
        context, "Smith", "Surname", 1, 10, false);

    assertTrue(result);
    verify(context, never()).buildConstraintViolationWithTemplate(anyString());
  }

  // -------------------------------------------------------
  // validateDate()
  // -------------------------------------------------------

  @Test
  void validateDate_shouldFailWhenBlankAndNotTolerated() {
    boolean result = BenefitCheckerHelper.validateDate(
        context, "", "DateOfBirth", false);

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(contains("Missing 'DateOfBirth'"));
  }

  @Test
  void validateDate_shouldPassWhenBlankAndTolerated() {
    boolean result = BenefitCheckerHelper.validateDate(
        context, "", "DateOfAward", true);

    assertTrue(result);
    verify(context, never()).buildConstraintViolationWithTemplate(anyString());
  }

  @Test
  void validateDate_shouldFailWhenWrongLength() {
    boolean result = BenefitCheckerHelper.validateDate(
        context, "2020010", "DateOfBirth", true);

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(
        contains("Error in request parameter 'DateOfBirth'"));
  }

  @Test
  void validateDate_shouldPassWhenLengthCorrect() {
    boolean result = BenefitCheckerHelper.validateDate(
        context, "20200101", "DateOfBirth", true);

    assertTrue(result);
  }

  // -------------------------------------------------------
  // validateNino()
  // -------------------------------------------------------

  @Test
  void validateNino_shouldFailWhenBlank() {
    boolean result = BenefitCheckerHelper.validateNino(context, "");

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(contains("Missing 'NI'"));
  }

  @Test
  void validateNino_shouldFailWhenLengthIncorrect() {
    boolean result = BenefitCheckerHelper.validateNino(context, "AB123");

    assertFalse(result);
    verify(context).buildConstraintViolationWithTemplate(contains("Error in 'NI'"));
  }

  @Test
  void validateNino_shouldPassWhenLengthCorrect() {
    // MAX_LEN_NINO is assumed to be 9
    boolean result = BenefitCheckerHelper.validateNino(context, "AB123456C");

    assertTrue(result);
  }

  // -------------------------------------------------------
  // validateContent()
  // -------------------------------------------------------

  @Test
  void validateContent_shouldPassWhenAllFieldsValid() {
    BenefitCheckRequestBody req = new BenefitCheckRequestBody();
    req.setSurname("Smith");
    req.setClientReference("ABC12345");
    req.setNino("AB123456C");
    req.setDateOfBirth("19900101");
    req.setDateOfAward("20200101");

    boolean result = BenefitCheckerHelper.validateContent(req, context);

    assertTrue(result);
  }

  @Test
  void validateContent_shouldFailWhenSurnameInvalid() {
    BenefitCheckRequestBody req = new BenefitCheckRequestBody();
    req.setSurname(""); // invalid
    req.setClientReference("ABC123");
    req.setNino("AB123456C");
    req.setDateOfBirth("19900101");
    req.setDateOfAward("20200101");

    boolean result = BenefitCheckerHelper.validateContent(req, context);

    assertFalse(result);
  }
}