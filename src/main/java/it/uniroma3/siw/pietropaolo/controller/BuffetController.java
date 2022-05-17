package it.uniroma3.siw.pietropaolo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.pietropaolo.controller.validator.BuffetValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.service.BuffetService;

@Controller
public class BuffetController {
	
	@Autowired BuffetService buffetService;
	@Autowired BuffetValidator buffetValidator;
	
	@GetMapping("/buffet/{id}")
	private String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", buffetService.findById(id));
		return "buffet.html";
	}
	
	@GetMapping("/buffetForm")
	public String getBuffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		return "buffetForm";
	}
	
	@GetMapping("/listaBuffet")
	public String getAllBuffet(Model model) {
		List<Buffet> listaBuffet = buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		return "listaBuffet";
	}
	
	@PostMapping("/buffet")
	private String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		this.buffetValidator.validate(buffet, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.buffetService.save(buffet);
			model.addAttribute("buffet", buffet);
			return "buffet";
		}else
			return "buffetForm";
	}

}
