package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.pietropaolo.model.pojo.User;
import it.uniroma3.siw.pietropaolo.service.UserService;

@Component
public class UserValidator implements Validator{

    @Autowired
    private UserService userService;

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user =(User) target;
        if(userService.alreadyExists(user)){
            errors.reject("user.duplicato");
        }
        
    }
    
}
