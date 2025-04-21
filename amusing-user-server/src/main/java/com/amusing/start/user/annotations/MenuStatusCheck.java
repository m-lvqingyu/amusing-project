package com.amusing.start.user.annotations;

import com.amusing.start.user.validator.MenuStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MenuStatusValidator.class)
public @interface MenuStatusCheck {

    String message() default "{status.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
