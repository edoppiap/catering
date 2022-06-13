package it.uniroma3.siw.pietropaolo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.service.BuffetService;

@Component
public class BuffetValidator implements Validator{
	
	@Autowired 
	private BuffetService buffetService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Buffet.class.equals(clazz);
	}

	@Override
	public void validate(Object other, Errors errors) {
		if(this.buffetService.alreadyExists((Buffet)other)){
			errors.reject("buffet.duplicato");
		}		
	}

}
