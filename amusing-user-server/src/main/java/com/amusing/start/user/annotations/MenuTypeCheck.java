package com.amusing.start.user.annotations;

import com.amusing.start.user.validator.MenuTypeValidator;

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
@Constraint(validatedBy = MenuTypeValidator.class)
public @interface MenuTypeCheck {

    String message() default "{type.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
