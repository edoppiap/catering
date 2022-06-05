package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
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

    public void validate(Credentials credentials, BindingResult credentialsBindingResult) {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        String username = credentials.getUsername().trim();
        String password = credentials.getPassword().trim();

        if(username.isEmpty()){
            errors.rejectValue("username", "required");
        }
        else if(username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH){
            errors.rejectValue("username", "size");
        }
        else if(credentialsService.getCredentials(username) != null){
            errors.rejectValue("username", "duplicate");
        }

        if(password.isEmpty()){
            errors.rejectValue("password", "required");
        }
        else if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH){
            errors.rejectValue("password", "size");
        }
        
    }
    
}
