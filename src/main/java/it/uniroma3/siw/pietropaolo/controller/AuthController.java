package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.pietropaolo.controller.validator.CredentialsValidator;
import it.uniroma3.siw.pietropaolo.service.CredentialsService;
import it.uniroma3.siw.pietropaolo.service.UserService;
import it.uniroma3.siw.pietropaolo.upload.FileUploadUtil;
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
        model.addAttribute("user", credentialsService.findByUsername(username).getUser());
        return "profilo";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "registerForm";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model){
        return "loginForm";
    }

    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        return "home";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(Model model){
        UserDetails userDetails =(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
        if(credentials.getRole().equals(Credentials.ADMIN_ROLE)){
            return "home";
        }
        return "home";
    }

    @PostMapping("/users/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        userValidator.validate(user, bindingResult);

        if(!bindingResult.hasErrors()){
            userService.saveUser(user); //questo si usa anche per aggiornare

            model.addAttribute("user", user);
            return "profilo";
        }else{
            return "editUser";
        }
    }

    @PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {
        

		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
        
		// if neither of them had invalid contents, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the User, thanks to Cascade.ALL policy
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);

            model.addAttribute("user", user);
			return "registerSuccess";
		}
        
        if(userBindingResult.getGlobalErrorCount() != 0){
            List<String> messaggi = new ArrayList<String>();
            for(ObjectError errore : userBindingResult.getGlobalErrors()){
                messaggi.add(errore.getCode());
            }
            logger.info("Ecco gli errori: "+messaggi);
            model.addAttribute("userErrors", messaggi);
        }
        if(credentialsBindingResult.getGlobalErrorCount() != 0){
            List<String> messaggi = new ArrayList<String>();
            for(ObjectError errore : credentialsBindingResult.getGlobalErrors()){
                messaggi.add(errore.getCode());
            }
            logger.info("Ecco gli errori: "+messaggi);
            model.addAttribute("credentialsErrors", messaggi);
        }
		return "registerForm";
	}

    @GetMapping("/users/deleteImageUser/{id}")
    public String deleteImage(@PathVariable("id") Long id, Model model) throws IOException{
        User user = userService.findUserById(id);
        FileUploadUtil.deleteFile(user.getImmaginePath());

        user.setNomeFoto(null);
        userService.saveUser(user);

        model.addAttribute("user", user);
        return "profilo";
    }

    @GetMapping("/users/editUser/{username}")
    public String updateUser(@PathVariable("username") String username, Model model){
        Credentials credentials = credentialsService.findByUsername(username);
        model.addAttribute("user", credentials.getUser());
        return "editUser";
    }

    @GetMapping("/users/uploadImageUser/{id}")
    public String getUploadImage(@PathVariable("id") Long id, Model model){
        model.addAttribute("actionLink", "/users/uploadImageUser/"+id);
		model.addAttribute("text", "Carica un immagine del profilo");
		return "uploadImage";
    }

    @PostMapping("/users/uploadImageUser/{id}")
	public String uploadImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException{
		User user = userService.findUserById(id);
        FileUploadUtil.deleteFile(user.getImmaginePath());
		if(multipartFile != null){
			String nomeFoto = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setNomeFoto(nomeFoto);

			userService.saveUser(user);

			String caricaCartella = "fotoUtente/"+ user.getId();
			FileUploadUtil.saveFile(caricaCartella, nomeFoto, multipartFile);
		}

        model.addAttribute("user", user);
		return "profilo";
	}
    
}
