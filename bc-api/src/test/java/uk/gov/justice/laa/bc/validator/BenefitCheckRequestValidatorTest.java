package uk.gov.justice.laa.bc.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

/**
 * BenefitCheckRequestValidatorTest.
 */
@ExtendWith(MockitoExtension.class)
public class BenefitCheckRequestValidatorTest {

  private BenefitCheckRequestValidation.BenefitCheckRequestValidator validator;
  private ConstraintValidatorContext context;
  private ConstraintViolationBuilder builder;

  private BenefitCheckRequestBody request;

  private MockedStatic<BenefitCheckerHelper> helperMock;

  @BeforeEach
  void setup() {
    validator = new BenefitCheckRequestValidation.BenefitCheckRequestValidator();

    context = mock(ConstraintValidatorContext.class);
    builder = mock(ConstraintViolationBuilder.class);
    lenient().when(context.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(builder);
    lenient().when(builder.addConstraintViolation()).thenReturn(context);

    request = new BenefitCheckRequestBody();
    request.setLscServiceName("SERVICE");
    request.setClientOrgId("ORG_123");
    request.setClientUserId("USER_123");
    request.setSurname("Doe");
    request.setClientReference("ABC123");
    request.setNino("AB123456C");
    request.setDateOfBirth("19900101");
    request.setDateOfAward("20200101");

    helperMock = mockStatic(BenefitCheckerHelper.class);
  }

  @AfterEach
  void tearDown() {
    helperMock.close();
  }

  // -------------------------------------------------------
  // isValid()
  // -------------------------------------------------------

  @Test
  void isValid_shouldReturnTrue_whenCredentialsAndContentValid() {

    // simulate content valid
    helperMock.when(() -> BenefitCheckerHelper.validateContent(eq(request), any()))
        .thenReturn(true);

    // simulate each credential field is valid
    helperMock.when(() ->
        BenefitCheckerHelper.validateString(eq(context), anyString(), anyString(), anyInt(),
            anyInt(), anyBoolean())
    ).thenReturn(true);

    boolean result = validator.isValid(request, context);

    assertTrue(result);
    helperMock.verify(() -> BenefitCheckerHelper.validateContent(request, context));
    helperMock.verify(() -> BenefitCheckerHelper.validateString(eq(context), eq("SERVICE"),
        eq("LSCServiceName"), anyInt(), anyInt(), anyBoolean()));
  }

  @Test
  void isValid_shouldReturnFalse_whenContentInvalid() {
    helperMock.when(() -> BenefitCheckerHelper.validateContent(eq(request), any()))
        .thenReturn(false);

    // Credentials valid
    helperMock.when(
            () -> BenefitCheckerHelper.validateString(any(), any(), any(), anyInt(), anyInt(),
                anyBoolean()))
        .thenReturn(true);

    boolean result = validator.isValid(request, context);

    assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalse_whenCredentialsInvalid() {
    helperMock.when(() -> BenefitCheckerHelper.validateContent(eq(request), any()))
        .thenReturn(true);

    // Credentials fail on one field
    helperMock.when(() ->
        BenefitCheckerHelper.validateString(eq(context), eq("SERVICE"), anyString(), anyInt(),
            anyInt(), anyBoolean())
    ).thenReturn(false);

    boolean result = validator.isValid(request, context);

    assertFalse(result);
  }

  @Test
  void isValid_shouldReturnFalse_whenBothContentAndCredentialsInvalid() {
    helperMock.when(() -> BenefitCheckerHelper.validateContent(eq(request), any()))
        .thenReturn(false);

    helperMock.when(() ->
        BenefitCheckerHelper.validateString(any(), any(), any(), anyInt(), anyInt(), anyBoolean())
    ).thenReturn(false);

    boolean result = validator.isValid(request, context);

    assertFalse(result);
  }

  // -------------------------------------------------------
  // validateCredentials() indirectly via static mocks
  // -------------------------------------------------------

  @Test
  void validateCredentials_shouldCallValidateStringForAllCredentialFields() {
    // Make all return true
    helperMock.when(() -> BenefitCheckerHelper.validateContent(request, context))
        .thenReturn(true);

    helperMock.when(() ->
        BenefitCheckerHelper.validateString(any(), any(), any(), anyInt(), anyInt(), anyBoolean())
    ).thenReturn(true);

    validator.isValid(request, context);

    helperMock.verify(() ->
        BenefitCheckerHelper.validateString(eq(context), eq("SERVICE"),
            eq("LSCServiceName"), anyInt(), anyInt(), anyBoolean()));

    helperMock.verify(() ->
        BenefitCheckerHelper.validateString(eq(context), eq("ORG_123"),
            eq("ClientOrgId"), anyInt(), anyInt(), anyBoolean()));

    helperMock.verify(() ->
        BenefitCheckerHelper.validateString(eq(context), eq("USER_123"),
            eq("ClientUserId"), anyInt(), anyInt(), anyBoolean()));
  }
}
