package io.wegetit.documently.validation;

import io.wegetit.documently.domain.template.TemplateService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
class ValidTemplateConstraintValidator implements ConstraintValidator<ValidTemplate, String> {
    private final TemplateService templateService;

    @Override
    public boolean isValid(String template, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isNoneEmpty(template) ? templateService.exists(template) : true;
    }
}
