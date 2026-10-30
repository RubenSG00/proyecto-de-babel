package es.us.dp1.chess.tournament.practice.x.customvalidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// A Bean Validation constraint: validated by MultipleOfValidator; RUNTIME retention, otherwise
// the annotation disappears from the compiled class and is silently ignored
@Constraint(validatedBy = MultipleOfValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipleOf {

    int value();

    String message() default "must be a multiple of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
