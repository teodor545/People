package com.task.third.service.web.ui.validation;

import com.vaadin.flow.data.binder.Validator;

public class ValidatorFactory {
    public static Validator<String> getStringValidator(String validatorName) {
        Validator<String> validator = null;
        if ("full_name".equalsIgnoreCase(validatorName)) {
            validator = new NameValidator();
        } else if ("pin".equalsIgnoreCase(validatorName)) {
            validator =  new PinValidator();
        } else if("email".equalsIgnoreCase(validatorName)) {
            validator = new MailValidator();
        } else if ("type".equalsIgnoreCase(validatorName)) {
            validator =  new TypeValidator();
        }
        return validator;
    }
}
