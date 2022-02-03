package kr.co.parkham.config.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.parkham.config.annotations.ValueValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class ValueValidator implements ConstraintValidator<ValueValid, Object> {
	private boolean isNull;
	private long minValue;
	private long maxValue;
	private int minLength;
	private int maxLength;
	private int minSize;
	private int maxSize;

	@Override
	public void initialize(ValueValid constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);

		isNull = constraintAnnotation.isNull();
		minValue = constraintAnnotation.minValue();
		maxValue = constraintAnnotation.maxValue();
		minLength = constraintAnnotation.minLength();
		maxLength = constraintAnnotation.maxLength();
		minSize = constraintAnnotation.minSize();
		maxSize = constraintAnnotation.maxSize();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) {
			return isNull;
		}

		if(value instanceof String) {
			String str = String.valueOf(value);

			if(str.length() < minLength || str.length() > maxLength) {
				return false;
			}
		} else if(value instanceof Character) {

		} else if(value instanceof Integer) {
			int n = (int)value;

			if(n < minValue || n > maxValue) {
				return false;
			}
		} else if(value instanceof Long) {
			long n = (long)value;

			if(n < minValue || n > maxValue) {
				return false;
			}
		} else if(value instanceof Short) {
			short n = (short)value;

			if(n < minValue || n > maxValue) {
				return false;
			}
		} else if(value instanceof List) {
			List<?> list = new ArrayList<>();

			if(value.getClass().isArray()) {
				list = Arrays.asList((Object[]) value);
			} else if(value instanceof Collection) {
				list = new ArrayList<>((Collection<?>) value);
			}

			if(list.size() < minSize || list.size() > maxSize) {
				return false;
			}
		} else if(value instanceof Map) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<Object, Object> map = objectMapper.convertValue(value, Map.class);

			if(map.size() < minSize || map.size() > maxSize) {
				return false;
			}
		}

		return true;
	}
}
