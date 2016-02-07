package com.github.schlegel.springjwt.validation.annotations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = RoleRestriction.RoleRestrictionValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface RoleRestriction {

    String value();
    String message() default "field access not allowed"; // message that will be used in the ViolationErrorOutputDTO
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };


    class RoleRestrictionValidator implements ConstraintValidator<RoleRestriction, Object> {

        private String role;

        @Override
        public void initialize(RoleRestriction constraintAnnotation) {
            Assert.notNull(constraintAnnotation.value());
            this.role = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(Object field, ConstraintValidatorContext context) {
            if(field != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                // check if role is in authorities
                return authentication.getAuthorities().stream().anyMatch(authority -> role.equals(authority.getAuthority()));
            } else {
                return true;
            }
        }
    }
}
