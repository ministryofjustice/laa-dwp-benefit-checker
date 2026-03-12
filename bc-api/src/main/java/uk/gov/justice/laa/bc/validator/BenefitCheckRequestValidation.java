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

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BenefitCheckRequestValidation.BenefitCheckRequestValidator.class)
public @interface BenefitCheckRequestValidation {
  String message() default "This isn't correct";
  Class[] groups() default {};
  Class[] payload() default {};
  class BenefitCheckRequestValidator implements
      ConstraintValidator<BenefitCheckRequestValidation, BenefitCheckRequestBody> {
    @Override
    public boolean isValid(BenefitCheckRequestBody benefitCheckRequestBody, ConstraintValidatorContext constraintValidatorContext) {

      return true;
    }
  }
}
