package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class TypeValidator implements Validator<String> {

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value!= null && value.length() > 0 && value.length() <= 5) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error("Type must be between 1 and 5 characters");
        }
    }
}
