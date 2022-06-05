package it.uniroma3.siw.pietropaolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.User;
import it.uniroma3.siw.pietropaolo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User utente){
        return userRepository.save(utente);
    }
}
