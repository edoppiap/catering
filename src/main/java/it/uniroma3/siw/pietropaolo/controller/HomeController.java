package it.uniroma3.siw.pietropaolo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    private String getHome(Model model){
        return "home";
    }

    @GetMapping("/Operazioni")
    private String getOperazioni(Model model){
        return "index";
    }

    @GetMapping("/about")
    private String getAbout(Model model){
        return "about";
    }

    @GetMapping("/contactUs")
    private String getContact(Model model){
        return "contattaci";
    }


}
