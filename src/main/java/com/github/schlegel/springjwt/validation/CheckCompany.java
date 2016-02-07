package com.github.schlegel.springjwt.validation;

import com.github.schlegel.springjwt.domain.company.CompanyRepository;
import com.github.schlegel.springjwt.service.company.transport.CompanyCreateDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation annotation for the CompanyCreateDto object.
 */
@Documented
@Constraint(validatedBy = CheckCompany.CheckCompanyValidator.class)
@Target(TYPE) // Annotation at class level
@Retention(RUNTIME)
public @interface CheckCompany {
    String message() default "company invalid"; // message that will be used in the ViolationErrorOutputDTO
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };


    /**
     * Inner class, that implements the validation logic for the CheckCompany validation annotation.
     */
    class CheckCompanyValidator  implements ConstraintValidator<CheckCompany, CompanyCreateDto> {

        /**
         * Access the database within the current transaction.
         */
        @Autowired
        private CompanyRepository companyRepository;

        @Override
        public void initialize(CheckCompany constraintAnnotation) {
            // nothing to do right now
        }

        /**
         * Validate the current object that is annotated with CheckCompany. Every field of the object can be accessed
         * and validated in context of each other.
         *
         * @param value This is the current object and every field is accessible.
         * @param context
         * @return
         */
        @Override
        public boolean isValid(CompanyCreateDto value, ConstraintValidatorContext context) {

            // Access the DB for proof-of-concept
            System.out.println(companyRepository.findAll().size());

            return true;
        }
    }
}
