package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.Validator;

public class ValidatorFactory {

    @SuppressWarnings("unchecked")
    public static <T> Validator<T> getValidator(Class<T> tClass, String validatorName) {
        if (tClass == String.class) {
            if ("full_name".equalsIgnoreCase(validatorName)) {
                return (Validator<T>) new NameValidator();
            } else if ("pin".equalsIgnoreCase(validatorName)) {
                return (Validator<T>) new PinValidator();
            } else if("email".equalsIgnoreCase(validatorName)) {
                return (Validator<T>) new MailValidator();
            } else if ("type".equalsIgnoreCase(validatorName)) {
                return (Validator<T>) new TypeValidator();
            }
        }
        return null;
    }
}
