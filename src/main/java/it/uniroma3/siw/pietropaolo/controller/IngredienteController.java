package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;
import java.util.List;

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

import it.uniroma3.siw.pietropaolo.controller.validator.IngredienteValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Ingrediente;
import it.uniroma3.siw.pietropaolo.service.IngredienteService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;

@Controller
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private IngredienteValidator ingredienteValidator;

    @GetMapping("/users/ingrediente/{id}")
    public String getIngredienteById(@PathVariable("id") Long id, Model model){
        Ingrediente ingrediente = ingredienteService.findById(id);
        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("listaPiatti", ingrediente.getPiatti());
        return "ingrediente";
    }

    @GetMapping("/admin/deleteIngrediente/{id}")
    public String deleteIngrediente(@PathVariable("id") Long id, Model model){
        this.ingredienteService.deleteIngredienteById(id);
        model.addAttribute("listaIngredienti", ingredienteService.findAll());
        return "listaIngredienti";
    }

    @GetMapping("/admin/ingredienteForm")
    public String getIngredienteForm(Model model){
        model.addAttribute("ingrediente", new Ingrediente());
        return "ingredienteForm";
    }

    @GetMapping("/admin/editIngrediente/{id}")
    public String editIngrediente(@PathVariable("id") Long id, Model model){
        Ingrediente ingrediente = ingredienteService.findById(id);
        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("image", ingrediente.getImmaginePath());
        return "ingredienteForm";
    }

    @GetMapping("/users/listaIngredienti")
    public String getAllIngredienti(Model model){
        List<Ingrediente> listaIngredienti = ingredienteService.findAll();
        model.addAttribute("listaIngredienti", listaIngredienti);
        return "listaIngredienti";
    }

    @PostMapping("/admin/ingrediente")
    public String newIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException{
        ingredienteValidator.validate(ingrediente, bindingResult);

        if(!bindingResult.hasErrors()){
            String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			ingrediente.setNomeFoto(nomeFoto);

            Ingrediente ingredienteSalvato = ingredienteService.save(ingrediente);
			String caricaCartella = "fotoIngrediente/"+ ingredienteSalvato.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);

            model.addAttribute("ingrediente", ingredienteSalvato);
            return "ingrediente";
        }else{
            return "ingredienteForm";
        }
    }
    
}
