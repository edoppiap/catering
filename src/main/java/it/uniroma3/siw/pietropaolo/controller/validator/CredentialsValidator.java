package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;
import it.uniroma3.siw.pietropaolo.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator{

    final Integer MAX_USERNAME_LENGTH = 40;
    final Integer MIN_USERNAME_LENGTH = 4;
    final Integer MAX_PASSWORD_LENGTH = 20;
    final Integer MIN_PASSWORD_LENGTH = 6;

    @Autowired
    private CredentialsService credentialsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        if(credentialsService.alreadyExists(credentials)){
            errors.reject("credentials.duplicato");
        }

        if(!credentials.getPassword().equals(credentials.getConfirmPassword())){
            errors.reject("credentials.passwordConfirm.wrong");
        }
    }
    
}
