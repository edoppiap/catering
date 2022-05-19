package it.uniroma3.siw.pietropaolo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    private String getHome(Principal principal){
        return principal != null ? "homeSignedIn" : "homeNotSignedIn";
    }

    @GetMapping("/Operazioni")
    private String getOperazioni(Model model){
        return "index.html";
    }
}
