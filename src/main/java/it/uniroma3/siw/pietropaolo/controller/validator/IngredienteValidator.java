package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.pietropaolo.model.pojo.Ingrediente;
import it.uniroma3.siw.pietropaolo.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator{

    @Autowired
    private IngredienteService ingredienteService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Ingrediente.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ingrediente ingrediente = (Ingrediente)target;
        if(ingredienteService.alreadyExists(ingrediente)){
            errors.reject("ingrediente.duplicato");
        }
        
    }
    
}
