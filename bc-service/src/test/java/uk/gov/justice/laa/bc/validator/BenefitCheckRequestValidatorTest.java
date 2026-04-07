package uk.gov.justice.laa.bc.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.exception.ValidationException;
import uk.gov.justice.laa.bc.service.AuthorisationService;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * BenefitCheckRequestValidatorTest.
 */
@ExtendWith(MockitoExtension.class)
public class BenefitCheckRequestValidatorTest {

  private BenefitCheckRequestValidator validator;
  @Mock
  private AuthorisationService authorisationService;

  @BeforeEach
  void setup() {
    validator = new BenefitCheckRequestValidator(authorisationService);
  }

  private BenefitCheckerRequest validRequest() {
    BenefitCheckerRequest request = new BenefitCheckerRequest();
    request.setLscServiceName("bc1-api");
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
    BenefitCheckerRequest req = validRequest();
    validator.validate(req);
  }

  @Test
  void missingSurname_makesContentInvalid() {
    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckerRequest req = validRequest();
      req.setSurname("");

      validator.validate(req);
    });
  }

  @Test
  void missingServiceName_makesCredentialsInvalid() {

    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckerRequest req = validRequest();
      req.setLscServiceName("");
      validator.validate(req);
    });
  }

  // ---------------------------------------------------------------------
  // validateString()
  // ---------------------------------------------------------------------

  @Test
  void validateString_validValue_returnsTrue() {
    BenefitCheckRequestValidator.validateString(
            "ABC", "TestField", 1, 5
    );
  }

  @Test
  void validateString_blankWhenNotAllowed_returnsFalse() {
    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateString(
              "", "TestField", 1, 5
      );
    });
  }

  @Test
  void validateString_lengthTooShort_returnsFalse() {
    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateString(
              "A", "TestField", 3, 5
      );
    });
  }

  @Test
  void validateString_lengthTooLong_returnsFalse() {
    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateString(
              "TOO_LONG_VALUE", "TestField", 1, 5
      );
    });
  }

  // ---------------------------------------------------------------------
  // validateDate()
  // ---------------------------------------------------------------------

  @Test
  void validateDate_validLength_returnsTrue() {
    BenefitCheckRequestValidator.validateDate(
            "20240101", "DateOfBirth", false
    );

  }

  @Test
  void validateDate_missingWhenRequired_returnsFalse() {
    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateDate(
              "", "DateOfBirth", false
      );
    });
  }

  @Test
  void validateDate_wrongLength_returnsFalse() {

    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateDate(
              "20241", "DateOfBirth", false
      );
    });
  }

  // ---------------------------------------------------------------------
  // validateNino()
  // ---------------------------------------------------------------------

  @Test
  void validateNino_valid_returnsTrue() {
    BenefitCheckRequestValidator.validateNino(
            "AB123456C"
    );
  }

  @Test
  void validateNino_blank_returnsFalse() {

    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateNino(
              ""
      );
    });


  }

  @Test
  void validateNino_wrongLength_returnsFalse() {

    Exception exception = assertThrows(ValidationException.class, () -> {
      BenefitCheckRequestValidator.validateNino(
              "SHORT"
      );
    });


  }
}
