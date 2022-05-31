package it.uniroma3.siw.pietropaolo.controller;

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

import it.uniroma3.siw.pietropaolo.service.ChefService;
import it.uniroma3.siw.pietropaolo.service.PiattoService;
import it.uniroma3.siw.pietropaolo.controller.validator.PiattoValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Buffet;
import it.uniroma3.siw.pietropaolo.model.pojo.Chef;
import it.uniroma3.siw.pietropaolo.model.pojo.Piatto;

@Controller
public class PiattoController {

    Logger logger = LoggerFactory.logger(PiattoController.class);

    @Autowired
    private PiattoService piattoService;

    @Autowired
    private PiattoValidator PiattoValidator;

    @Autowired
    private ChefService chefService;

    @GetMapping("/piatto/{id}")
    public String getPiatto(@PathVariable("id") Long id, Model model){
        Piatto piatto = piattoService.findById(id);
        model.addAttribute("piatto", piatto);
        model.addAttribute("listaIngredienti", piatto.getIngredienti());
        return "buffet";
    }

    @GetMapping("/deletePiatto/{id}")
    public String deletePiatto(@PathVariable("id") Long id, Model model){
        piattoService.deletePiattoById(id);
        model.addAttribute("listaPiatti", piattoService.findAll());
        return "buffet";
    }

    @GetMapping("/piattoForm")
    public String getPiattoForm(Model model){
        model.addAttribute("piatto", new Piatto());
        return "piattoForm";
    }

    @PostMapping("/piattoFromModals")
    public String newPiattoFromModals(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model){
        PiattoValidator.validate(piatto, bindingResult);

        if(!bindingResult.hasErrors()){
            piattoService.save(piatto);

            Buffet buffet = new Buffet();
            logger.info("ecco gli attribute: "+model.asMap().toString());

            model.addAttribute("buffet", buffet);
            model.addAttribute("listaChef", chefService.findAll());
			model.addAttribute("piatto", new Piatto());
			model.addAttribute("chef", new Chef());
			model.addAttribute("listaPiatti", piattoService.findAll());
            return "buffetForm";
        }else{
            return "buffetForm";
        }
    }

    @PostMapping("/piatto")
    public String newPiatto(@Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResult, Model model){
        PiattoValidator.validate(piatto, bindingResult);

        if(!bindingResult.hasErrors()){
            piattoService.save(piatto);
            return "listaBuffet";
        }else{
            return "piattoForm";
        }
    }
    
}
