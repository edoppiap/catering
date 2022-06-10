package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;

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

import it.uniroma3.siw.pietropaolo.service.IngredienteService;
import it.uniroma3.siw.pietropaolo.service.PiattoService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;
import it.uniroma3.siw.pietropaolo.controller.validator.PiattoValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;

@Controller
public class PiattoController {

    Logger logger = LoggerFactory.logger(PiattoController.class);

    @Autowired
    private PiattoService piattoService;

    @Autowired
    private PiattoValidator PiattoValidator;

    @Autowired
    private IngredienteService ingredienteService;

    @GetMapping("/users/piatto/{id}")
    public String getPiatto(@PathVariable("id") Long id, Model model){
        Piatto piatto = piattoService.findById(id);
        model.addAttribute("piatto", piatto);
        model.addAttribute("listaIngredienti", piatto.getIngredienti());
        model.addAttribute("listaBuffet", piatto.getListaBuffet());
        return "piatto";
    }

    @GetMapping("/admin/deletePiatto/{id}")
    public String deletePiatto(@PathVariable("id") Long id, Model model){
        piattoService.deletePiattoById(id);
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
    public String newPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException{
        PiattoValidator.validate(piatto, bindingResult);

        if(!bindingResult.hasErrors()){
            String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			piatto.setNomeFoto(nomeFoto);

            Piatto piattoSalvato = piattoService.save(piatto);
			String caricaCartella = "fotoPiatto/"+ piattoSalvato.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);

            model.addAttribute("listaPiatti", piattoService.findAll());
            return "listaPiatti";
        }else{
            model.addAttribute("listaIngredienti", ingredienteService.findAll());
            return "piattoForm";
        }
    }
    
}
