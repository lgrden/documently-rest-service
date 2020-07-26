package io.wegetit.documently.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidatorService {

	private final Validator validator;
	
	public <T> void validate(T t) {
		Set<ConstraintViolation<T>> errors = validator.validate(t);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
	}
	
	public <T> Set<ConstraintViolation<T>> collect(T t) {
		return validator.validate(t);
	}
}  