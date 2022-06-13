package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.service.PiattoService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PiattoValidator implements Validator{

    @Autowired
    private PiattoService piattoService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Piatto.class.equals(clazz);
    }

    @Override
    public void validate(Object other, Errors errors) {
        if(piattoService.alreadyExists((Piatto)other)){
            errors.reject("piatto.duplicato");
        }
        
    }
    
}
