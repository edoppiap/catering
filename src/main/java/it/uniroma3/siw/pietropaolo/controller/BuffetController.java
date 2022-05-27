package it.uniroma3.siw.pietropaolo.controller;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
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
import it.uniroma3.siw.pietropaolo.service.ChefService;

@Controller
public class BuffetController {

	Logger logger = LoggerFactory.logger(BuffetController.class);
	
	@Autowired 
	private BuffetService buffetService;
	
	@Autowired 
	private BuffetValidator buffetValidator;

	@Autowired
	private ChefService chefService;
	
	@GetMapping("/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("buffet", buffetService.findById(id));
		return "buffet";
	}

	@GetMapping("/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model){
		this.buffetService.deleteBuffetById(id);
		model.addAttribute("listaBuffet", this.buffetService.findAll());
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listaChef", chefService.findAll());
		return "listaBuffet";
	}

	@GetMapping("/deleteAllBuffet")
	public String deleteAllBuffet(Model model){
		this.buffetService.deleteAll();
		model.addAttribute("listaBuffet", this.buffetService.findAll());
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listaChef", chefService.findAll());
		return "listaBuffet";
	}

	/*@PostMapping("/deleteBuffet")
	public String deleteBuffet(@ModelAttribute("buffet") Buffet buffet, Model model){
		this.buffetService.deleteBuffet(buffet);
		return "listaBuffet";
	}*/
	
	@GetMapping("/buffetForm")
	public String getBuffetForm(Model model) {
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listaChef", chefService.findAll());
		return "buffetForm";
	}

	@GetMapping("/editBuffet/{id}")
	public String editBuffet(@PathVariable("id") Long id, Model model){
		model.addAttribute("buffet", buffetService.findById(id));
		model.addAttribute("listaChef", chefService.findAll());
		return "editBuffet";
	}
	
	@GetMapping("/listaBuffet")
	public String getAllBuffet(Model model) {
		List<Buffet> listaBuffet = buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		model.addAttribute("buffet", new Buffet());
		model.addAttribute("listaChef", chefService.findAll());
		return "listaBuffet";
	}

	@PostMapping("/editBuffet/{id}")
	public String editBuffet(@PathVariable("id") Long id,@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		this.buffetValidator.validate(buffet, bindingResult);

		if(!bindingResult.hasErrors()){
			this.buffetService.updateBuffet(buffet);
			model.addAttribute("listaBuffet", buffetService.findAll());
			model.addAttribute("buffet", buffet);
			model.addAttribute("listaChef", chefService.findAll());
			return "listaBuffet";
		}else{
			model.addAttribute("listaChef", chefService.findAll());
			return "editBuffet";
		}
		
	}
	
	@PostMapping("/buffet")
	public String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		logger.info(buffet.toString());
		this.buffetValidator.validate(buffet, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.buffetService.save(buffet);
			model.addAttribute("listaBuffet", buffetService.findAll());
			model.addAttribute("buffet", new Buffet());
			model.addAttribute("listaChef", chefService.findAll());
			return "listaBuffet";
		}else{
			model.addAttribute("listaChef", chefService.findAll());
			return "buffetForm";
		}
	}

}
