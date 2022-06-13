package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.service.ChefService;

@Component
public class ChefValidator implements Validator{
	
	@Autowired
	private ChefService chefService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Buffet.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.chefService.alreadyExists((Chef)target)) {
			errors.reject("chef.duplicato");
		}
		
	}

}
