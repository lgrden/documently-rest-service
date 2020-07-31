package io.wegetit.documently.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTemplateConstraintValidator.class)
public @interface ValidTemplate {
    String message() default "Template is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
