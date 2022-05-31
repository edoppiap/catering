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
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.service.ChefService;

@Controller
public class ChefController {
	
	@Autowired 
	private ChefService chefService;
	
	@Autowired 
	private ChefValidator chefValidator;
	
	@GetMapping("/chef/{id}")
	private String getBuffet(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);
		model.addAttribute("listaBuffet", chef.getListaBuffet().parallelStream().sorted().toList());
		return "chef";
	}

	@GetMapping("/deleteChef/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model){
		this.chefService.deleteBuffetById(id);
		model.addAttribute("listaChef", this.chefService.findAll());
		return "listaChef";
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

	@PostMapping("/chefFromBuffetForm")
	public String newChefFromBuffetForm(@Valid @ModelAttribute("chef") Chef chef, @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model){
		this.chefValidator.validate(chef, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("buffet", buffet);
			model.addAttribute("piatto", new Piatto());
			model.addAttribute("listaChef", chefService.findAll());
			model.addAttribute("chef", new Chef());
			return "buffetForm";
		}else {
			return "buffetForm";
		}
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
