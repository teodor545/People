package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinValidator implements Validator<String> {

    private final Pattern pinPattern = Pattern.compile("^\\d+$");

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value != null && !value.isEmpty()) {
            if (value.length() != 10) {
                return ValidationResult.error("Pin must be exactly 10 numbers");
            } else {
                if (!pinPattern.matcher(value).find()) {
                    return ValidationResult.error("Pin must contain only numbers");
                }
            }
        }
        return ValidationResult.ok();
    }
}
