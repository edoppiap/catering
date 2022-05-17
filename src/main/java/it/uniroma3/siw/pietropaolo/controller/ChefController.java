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

import it.uniroma3.siw.pietropaolo.controller.validator.ChefValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.service.ChefService;

@Controller
public class ChefController {
	
	@Autowired ChefService chefService;
	
	@Autowired ChefValidator chefValidator;
	
	@GetMapping("/chef/{id}")
	private String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", chefService.findById(id));
		return "chef";
	}
	
	@GetMapping("/chefForm")
	public String getBuffetForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "chefForm";
	}
	
	@GetMapping("/listaChef")
	public String getAllChef(Model model) {
		List<Chef> listaChef = this.chefService.findAll();
		model.addAttribute("listaChef", listaChef);
		return "listaChef";
	}
	
	@PostMapping("/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			return "chef";
		}else {
			return "chefForm";
		}
	}
}
