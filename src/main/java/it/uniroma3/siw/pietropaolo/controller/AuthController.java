package it.uniroma3.siw.pietropaolo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
import org.springframework.web.client.RestTemplate;
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
    
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/users/profilo/{username}")
    public String getProfilo(@PathVariable("username") String username, Model model){
        Credentials credentials = credentialsService.findByUsername(username);
        model.addAttribute("user", credentials.getUser());
        model.addAttribute("credentials", credentials);
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
            return "index";
        }
        return "home";
    }

    @GetMapping("/oauthDefault")
    public String defaultAfterOAuthLogin(Model model, OAuth2AuthenticationToken authentication){
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        if(!userInfoEndpointUri.isEmpty()){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+client.getAccessToken().getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();

            Credentials userCredentials;
            String username = ((String) userAttributes.get("given_name"));
            if(username != null){
                userCredentials = credentialsService.findByUsername((String) userAttributes.get("given_name"));
            }else{
                userCredentials = credentialsService.findByUsername((String) userAttributes.get("login"));
            }
            
            if(userCredentials == null){
                Credentials oauthCredentials = new Credentials();
                User oauthUser = new User();
                
                String[] nomeCompleto;
                String nome = ((String) userAttributes.get("name"));
                if(nome!=null){
                    nomeCompleto = nome.split((" "));
                    oauthUser.setNome(nomeCompleto[0]);
                    oauthUser.setCognome(nomeCompleto[1]);
                }else{
                    Integer id = (Integer) userAttributes.get("id");
                    oauthUser.setNome("nomeGitHub"+id);
                    oauthUser.setCognome("cognomeGitHub"+id);
                }

                String email = ((String) userAttributes.get("email"));
                if(email!=null){
                    oauthUser.setEmail(email);
                }else{
                    Integer id = (Integer) userAttributes.get("id");
                    oauthUser.setEmail("email"+id+"@github.com");
                }
                oauthCredentials.setUser(oauthUser);

                if(((String) userAttributes.get("given_name")) != null){
                    oauthCredentials.setUsername((String) userAttributes.get("given_name"));
                }else{
                    oauthCredentials.setUsername((String) userAttributes.get("login"));
                }
                credentialsService.saveCredentials(oauthCredentials);
            }
        }


        return "home";
    }

    @PostMapping("/users/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        userValidator.validate(user, bindingResult);

        if(!bindingResult.hasErrors()){
            userService.saveUser(user); //questo si usa anche per aggiornare

            model.addAttribute("user", user);
            model.addAttribute("credentials", credentialsService.findByUser(user));
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
            model.addAttribute("userErrors", messaggi);
        }
        if(credentialsBindingResult.getGlobalErrorCount() != 0){
            List<String> messaggi = new ArrayList<String>();
            for(ObjectError errore : credentialsBindingResult.getGlobalErrors()){
                messaggi.add(errore.getCode());
            }
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
        model.addAttribute("credentials", credentialsService.findByUser(user));
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
        model.addAttribute("credentials", credentialsService.findByUser(user));
		return "profilo";
	}
    
}
