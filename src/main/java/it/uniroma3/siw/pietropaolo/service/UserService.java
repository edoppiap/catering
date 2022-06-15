package it.uniroma3.siw.pietropaolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.pietropaolo.model.pojo.User;
import it.uniroma3.siw.pietropaolo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User utente){
        return userRepository.save(utente);
    }

    public boolean existsByEmail(User user){
        if(user.getId() == null){
            return userRepository.existsByEmail(user.getEmail());
        }else if(userRepository.existsByEmail(user.getEmail())){
            User userEsistente = userRepository.findByEmail(user.getEmail()).get();
            return !(userEsistente.getId().equals(user.getId())); //se gli id sono uguali vuol dire che sto modificando lo stesso user
        }else{
            return false;
        }
    }
    
}
