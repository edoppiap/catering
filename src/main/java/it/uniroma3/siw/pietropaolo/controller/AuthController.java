package it.uniroma3.siw.pietropaolo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.pietropaolo.controller.validator.CredentialsValidator;
import it.uniroma3.siw.pietropaolo.service.CredentialsService;
import it.uniroma3.siw.pietropaolo.service.UserService;
import it.uniroma3.siw.pietropaolo.controller.validator.UserValidator;
import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;
import it.uniroma3.siw.pietropaolo.model.pojo.User;

@Controller
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @Autowired
    private UserService userService;

    @GetMapping("/users/profilo/{username}")
    public String getProfilo(@PathVariable("username") String username, Model model){
        model.addAttribute("user", credentialsService.getCredentials(username).getUser());
        return "profilo";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "registerForm";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        return "home";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(Model model){
        UserDetails userDetails =(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if(credentials.getRole().equals(Credentials.ADMIN_ROLE)){
            return "home";
        }
        return "home";
    }

    @PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
			@ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {
        
        logger.info("Sono qui");

		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

		// if neither of them had invalid contents, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the User, thanks to Cascade.ALL policy
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			return "registerSuccess";
		}
		return "registerForm";
	}
    
}
