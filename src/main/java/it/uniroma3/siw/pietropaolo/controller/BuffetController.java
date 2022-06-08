package it.uniroma3.siw.pietropaolo.controller;

import java.util.ArrayList;
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
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.service.BuffetService;
import it.uniroma3.siw.pietropaolo.service.ChefService;
import it.uniroma3.siw.pietropaolo.service.PiattoService;

@Controller
public class BuffetController {

	Logger logger = LoggerFactory.logger(BuffetController.class);
	
	@Autowired 
	private BuffetService buffetService;
	
	@Autowired 
	private BuffetValidator buffetValidator;

	@Autowired
	private ChefService chefService;

	@Autowired
	private PiattoService piattoService;
	
	@GetMapping("/users/buffet/{id}")
	public String getBuffet(@PathVariable("id") Long id, Model model) {
		Buffet buffet = buffetService.findById(id);
		model.addAttribute("buffet", buffet);
		model.addAttribute("piattiDelBuffet", buffet.getPiatti());
		return "buffet";
	}

	@GetMapping("/admin/deleteBuffet/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model){
		this.buffetService.deleteBuffetById(id);
		model.addAttribute("listaBuffet", this.buffetService.findAll());
		model.addAttribute("listaChef", chefService.findAll());
		return "listaBuffet";

	}

	@GetMapping("/admin/deleteAllBuffet")
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
	
	@GetMapping("/admin/buffetForm")
	public String getBuffetForm(Model model) {
		Buffet buffet = new Buffet();
		buffet.setPiatti(new ArrayList<Piatto>());
		model.addAttribute("buffet", buffet);
		model.addAttribute("piatto", new Piatto());
		model.addAttribute("listaChef", chefService.findAll());
		model.addAttribute("chef", new Chef());
		model.addAttribute("listaPiatti", piattoService.findAll());
		return "buffetForm";
	}

	@GetMapping("/admin/editBuffet/{id}")
	public String editBuffet(@PathVariable("id") Long id, Model model){
		Buffet buffet = buffetService.findById(id);
		logger.info("Prendo buffet da modificare: "+buffet.toString());
		model.addAttribute("buffet", buffet);
		model.addAttribute("listaChef", chefService.findAll());
		model.addAttribute("listaPiatti", piattoService.findAll());
		
		/**
		 * Attributi per le due modali di inserimento all'interno della form
		 */
		model.addAttribute("chef", new Chef());
		model.addAttribute("piatto", new Piatto());
		return "editBuffet";
	}
	
	@GetMapping("/users/listaBuffet")
	public String getAllBuffet(Model model) {
		List<Buffet> listaBuffet = buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		return "listaBuffet";
	}

	@PostMapping("/admin/editBuffet")
	public String editBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		this.buffetValidator.validate(buffet, bindingResult);

		logger.info("Prendo buffet aggiornato: "+buffet.toString());

		if(!bindingResult.hasErrors()){
			this.buffetService.updateBuffet(buffet);
			model.addAttribute("listaBuffet", buffetService.findAll());
			model.addAttribute("buffet", buffet);
			return "listaBuffet";
		}else{
			model.addAttribute("listaChef", chefService.findAll());
			model.addAttribute("piatto", new Piatto());
			model.addAttribute("chef", new Chef());
			model.addAttribute("listaPiatti", piattoService.findAll());
			return "editBuffet";
		}
		
	}
	
	@PostMapping("/admin/buffet")
	public String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		logger.info("nuovo buffet"+buffet.toString());
		this.buffetValidator.validate(buffet, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.buffetService.save(buffet);
			model.addAttribute("listaBuffet", buffetService.findAll());
			return "listaBuffet";
		}else{
			model.addAttribute("listaChef", chefService.findAll());
			model.addAttribute("piatto", new Piatto());
			model.addAttribute("chef", new Chef());
			model.addAttribute("listaPiatti", piattoService.findAll());
			return "buffetForm";
		}
	}

}
