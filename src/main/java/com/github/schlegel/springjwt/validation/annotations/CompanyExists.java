package com.github.schlegel.springjwt.validation.annotations;

import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CompanyExists.CompanyExistsValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface CompanyExists {
    String message() default "not available"; // message that will be used in the ViolationErrorOutputDTO
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    class CompanyExistsValidator implements ConstraintValidator<CompanyExists, UUID> {

        @Autowired
        private CompanyRepository companyRepository;

        @Override
        public void initialize(CompanyExists constraintAnnotation) {
        }

        @Override
        public boolean isValid(UUID field, ConstraintValidatorContext context) {
           return field != null && companyRepository.exists(field);
        }
    }
}
