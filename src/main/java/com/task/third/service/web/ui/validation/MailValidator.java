package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;

import java.util.regex.Pattern;

public class MailValidator implements Validator<String> {

    private final Pattern pinPattern = Pattern.compile(EmailValidator.PATTERN);

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.isEmpty()) {
            return ValidationResult.ok();
        } else {
            if (pinPattern.matcher(value).find()) {
                return ValidationResult.ok();
            }
        }
        return ValidationResult.error("Not a valid email address");
    }
}
