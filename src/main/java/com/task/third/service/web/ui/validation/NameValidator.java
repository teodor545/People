package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.regex.Pattern;

public class NameValidator implements Validator<String> {

    private final Pattern namePattern = Pattern.compile("^[a-zA-Zа-яА-Я\\s-]+$");

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.isEmpty()) {
            return ValidationResult.error("Name must not be empty");
        }
        if (!namePattern.matcher(value).find()) {
            return ValidationResult.error("Name must contain latin or cyrillic letters hyphen or whitespace");
        }
        return ValidationResult.ok();
    }
}
