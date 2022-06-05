package it.uniroma3.siw.pietropaolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;
import it.uniroma3.siw.pietropaolo.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    /**
     * 
     * @param id
     * @return Credenziali or null
     */
    public Credentials getCredentials(Long id){
        return credentialsRepository.findById(id).orElse(null);
    }

    /**
     * 
     * @param username
     * @return Credenziali or null
     */
    public Credentials getCredentials(String username){
        return credentialsRepository.findByUsername(username).orElse(null);
    }

    public Credentials saveCredentials(Credentials credentials){
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        return credentialsRepository.save(credentials);
    }
}