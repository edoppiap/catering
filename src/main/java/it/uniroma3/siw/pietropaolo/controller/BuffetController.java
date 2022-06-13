package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
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

import it.uniroma3.siw.pietropaolo.controller.validator.BuffetValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;
import it.uniroma3.siw.pietropaolo.service.BuffetService;
import it.uniroma3.siw.pietropaolo.service.ChefService;
import it.uniroma3.siw.pietropaolo.service.PiattoService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;

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
	public String deleteBuffet(@PathVariable("id") Long id, Model model) throws IOException{
		FileUploadUtil.deleteFile(buffetService.findById(id).getImmaginePath());
		this.buffetService.deleteBuffetById(id);
		model.addAttribute("listaBuffet", this.buffetService.findAll());
		model.addAttribute("listaChef", chefService.findAll());
		return "listaBuffet";

	}
	
	@GetMapping("/admin/buffetForm")
	public String getBuffetForm(Model model) {
		Buffet buffet = new Buffet();
		buffet.setPiatti(new ArrayList<Piatto>());
		model.addAttribute("buffet", buffet);
		model.addAttribute("listaChef", chefService.findAll());
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

		
		return "buffetForm";
	}
	
	@GetMapping("/users/listaBuffet")
	public String getAllBuffet(Model model) {
		List<Buffet> listaBuffet = buffetService.findAll();
		model.addAttribute("listaBuffet", listaBuffet);
		return "listaBuffet";
	}
	
	@PostMapping("/admin/buffet")
	public String newBuffet(@Valid @ModelAttribute("buffet") Buffet buffet, BindingResult bindingResult, Model model) {
		logger.info("nuovo buffet"+buffet.toString());
		this.buffetValidator.validate(buffet, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			Buffet salvato = this.buffetService.save(buffet);			
			
			model.addAttribute("actionLink", "/admin/uploadImageBuffet/"+salvato.getId());
			model.addAttribute("text", "Carica un immagine del buffet");
			return "uploadImage";
		}else{
			model.addAttribute("listaChef", chefService.findAll());
			model.addAttribute("listaPiatti", piattoService.findAll());
			return "buffetForm";
		}
	}

	@PostMapping("/admin/uploadImageBuffet/{id}")
	public String uploadImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException{
		Buffet buffet = buffetService.findById(id);
		FileUploadUtil.deleteFile(buffet.getImmaginePath());
		if(multipartFile != null){
			String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			buffet.setNomeFoto(nomeFoto);

			buffetService.updateBuffet(buffet);

			String caricaCartella = "fotoBuffet/"+ buffet.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);
		}else{
			buffetService.deleteBuffetById(id);
		}

		model.addAttribute("listaBuffet", buffetService.findAll());
		return "listaBuffet";
	}

}
