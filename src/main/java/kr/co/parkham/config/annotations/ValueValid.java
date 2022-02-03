package kr.co.parkham.config.annotations;

import kr.co.parkham.config.validator.ValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValueValidator.class)
public @interface ValueValid {
	boolean isNull() default true;

	long minValue() default Long.MIN_VALUE;

	long maxValue() default Long.MAX_VALUE;

	int minLength() default 0;

	int maxLength() default Integer.MAX_VALUE;

	int minSize() default 0;

	int maxSize() default Integer.MAX_VALUE;

	String message() default "test";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}