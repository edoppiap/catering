package it.uniroma3.siw.pietropaolo.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.Credentials;
import it.uniroma3.siw.pietropaolo.model.pojo.User;
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
    public Credentials findById(Long id){
        return credentialsRepository.findById(id).orElse(null);
    }

    /**
     * 
     * @param username
     * @return Credenziali or null
     */
    public Credentials findByUsername(String username){
        return credentialsRepository.findByUsername(username).orElse(null);
    }

    public Credentials findByUser(User user){
        return credentialsRepository.findByUser(user).orElse(null);
    }

    public Credentials saveCredentials(Credentials credentials){
        credentials.setRole(Credentials.DEFAULT_ROLE);
        if(credentials.getPassword()==null){
            credentials.setPassword(this.passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(10)));
        }else{
            credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        }
        return credentialsRepository.save(credentials);
    }

    public boolean alreadyExists(Credentials credentials) {
        return credentialsRepository.existsByUsername(credentials.getUsername());
    }
}
