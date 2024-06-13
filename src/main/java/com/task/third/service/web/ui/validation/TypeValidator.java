package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.regex.Pattern;

public class TypeValidator implements Validator<String> {

    private final Pattern typePattern = Pattern.compile("^[a-zA-Z]{1,5}$");

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (typePattern.matcher(value).find()) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error("Type must be between 1 and 5 characters");
        }
    }
}
