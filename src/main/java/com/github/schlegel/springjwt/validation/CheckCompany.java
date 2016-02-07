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

@Documented
@Constraint(validatedBy = CheckCompany.CheckCompanyValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface CheckCompany {
    String message() default "company invalid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };



    class CheckCompanyValidator  implements ConstraintValidator<CheckCompany, CompanyCreateDto> {

        @Autowired
        private CompanyRepository companyRepository;

        @Override
        public void initialize(CheckCompany constraintAnnotation) {

        }

        @Override
        public boolean isValid(CompanyCreateDto value, ConstraintValidatorContext context) {

            System.out.println(companyRepository.findAll().size());

            return false;
        }
    }
}
