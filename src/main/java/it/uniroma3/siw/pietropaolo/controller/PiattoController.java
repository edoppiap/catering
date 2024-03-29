package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;

import javax.validation.Valid;

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

import it.uniroma3.siw.pietropaolo.service.BuffetService;
import it.uniroma3.siw.pietropaolo.service.IngredienteService;
import it.uniroma3.siw.pietropaolo.service.PiattoService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;
import it.uniroma3.siw.pietropaolo.controller.validator.PiattoValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;

@Controller
public class PiattoController {

    @Autowired
    private PiattoService piattoService;

    @Autowired
    private PiattoValidator PiattoValidator;

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private BuffetService buffetService;

    @GetMapping("/users/piatto/{id}")
    public String getPiatto(@PathVariable("id") Long id, Model model){
        Piatto piatto = piattoService.findById(id);
        model.addAttribute("piatto", piatto);
        model.addAttribute("listaIngredienti", piatto.getIngredienti());
        return "piatto";
    }

    @GetMapping("/admin/deletePiatto/{id}")
    public String deletePiatto(@PathVariable("id") Long id, Model model) throws IOException{
        Piatto piatto = piattoService.findById(id);
		FileUploadUtil.deleteFile(piatto.getImmaginePath());

        /**
         * Questa logica è obbligata in quanto buffet non è la owned side
         */
        for(Buffet buffet : piatto.getBuffets()){
            buffet.getPiatti().remove(piatto);
            
            if(buffet.getPiatti().size() == 0){ //se il buffet rimane senza piatti viene eliminato
                FileUploadUtil.deleteFile(buffet.getImmaginePath());
                buffetService.deleteById(buffet.getId());
            }
        }
        piattoService.deletePiattoById(piatto.getId());

        model.addAttribute("listaPiatti", piattoService.findAll());
        return "listaPiatti";
    }

    @GetMapping("/admin/editPiatto/{id}")
    public String editPiatto(@PathVariable("id") Long id, Model model){
        model.addAttribute("piatto", piattoService.findById(id));
        model.addAttribute("listaIngredienti", ingredienteService.findAll());
        return "piattoForm";
    }

    @GetMapping("/admin/piattoForm")
    public String getPiattoForm(Model model){
        model.addAttribute("piatto", new Piatto());
        model.addAttribute("listaIngredienti", ingredienteService.findAll());
        return "piattoForm";
    }

    @GetMapping("/users/listaPiatti")
    public String getAll(Model model){
        model.addAttribute("listaPiatti", piattoService.findAll());
        return "listaPiatti";
    }

    @PostMapping("/admin/piatto")
    public String newPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model) throws IOException{
        PiattoValidator.validate(piatto, bindingResult);

        if(!bindingResult.hasErrors()){
            Piatto salvato = piattoService.save(piatto);

            model.addAttribute("actionLink", "/admin/uploadImagePiatto/"+salvato.getId());
			model.addAttribute("text", "Carica un immagine del piatto "+salvato.getNome());
			return "uploadImage";
        }else{
            model.addAttribute("listaIngredienti", ingredienteService.findAll());
            return "piattoForm";
        }
    }

    @PostMapping("/admin/uploadImagePiatto/{id}")
	public String uploadImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException{
		Piatto piatto = piattoService.findById(id);
        FileUploadUtil.deleteFile(piatto.getImmaginePath());
		if(multipartFile != null){
			String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			piatto.setNomeFoto(nomeFoto);

            piattoService.updatePiatto(piatto);

			String caricaCartella = "fotoPiatto/"+ piatto.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);
		}else{
			ingredienteService.deleteById(id);
		}

		model.addAttribute("listaPiatti", piattoService.findAll());
        return "listaPiatti";
	}
    
}
