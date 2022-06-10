package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.pietropaolo.controller.validator.ChefValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.service.BuffetService;
import it.uniroma3.siw.pietropaolo.service.ChefService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;

@Controller
public class ChefController {

	Logger logger = LoggerFactory.getLogger(ChefController.class);
	
	@Autowired 
	private ChefService chefService;
	
	@Autowired 
	private ChefValidator chefValidator;

	@Autowired
	private BuffetService buffetService;
	
	@GetMapping("/users/chef/{id}")
	private String getBuffet(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);
		model.addAttribute("listaBuffet", chef.getListaBuffet().parallelStream().sorted().toList());
		return "chef";
	}

	@GetMapping("/admin/deleteChef/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model){
		this.chefService.deleteBuffetById(id);
		model.addAttribute("listaChef", this.chefService.findAll());
		return "listaChef";
	}
	
	@GetMapping("/admin/chefForm")
	public String getBuffetForm(Model model) {
		model.addAttribute("chef", new Chef());
		return "chefForm";
	}

	@GetMapping("/admin/editChef/{id}")
	public String editChef(@PathVariable("id") Long id, Model model){
		model.addAttribute("chef", chefService.findById(id));
		return "chefForm";
	}
	
	@GetMapping("/users/listaChef")
	public String getAllChef(Model model) {
		List<Chef> listaChef = this.chefService.findAll();
		model.addAttribute("listaChef", listaChef);
		return "listaChef";
	}

	@PostMapping("/admin/chefFromBuffetForm")
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

	@PostMapping("/admin/updateChef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef, @PathVariable Long id, BindingResult bindingResult, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			model.addAttribute("listaBuffet", chef.getListaBuffet());
			return "chef";
		}else {
			return "chefForm";
		}
	}
	
	@PostMapping("/admin/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef,@RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException {
		this.chefValidator.validate(chef, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			chef.setNomeFoto(nomeFoto);

			Chef chefSalvato = this.chefService.save(chef);
			String caricaCartella = "fotoChef/"+ chefSalvato.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);
			model.addAttribute("chef", chef);
			logger.info("Lista buffet da chef: "+ chef.toString());
			model.addAttribute("listaBuffet", buffetService.findByChefId(chef.getId()));
			return "chef";
		}else {
			return "chefForm";
		}
	}
}
