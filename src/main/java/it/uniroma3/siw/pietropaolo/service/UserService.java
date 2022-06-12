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

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean alreadyExists(User user) {
        if(user.getId() == null){
			return userRepository.existsByNomeAndCognome(user.getNome(), user.getCognome());
		}else if(userRepository.existsByNomeAndCognome(user.getNome(), user.getCognome())){
			User userEsistente = userRepository.findByNomeAndCognome(user.getNome(), user.getCognome()).get();
			return !(userEsistente.getId().equals(user.getId())); //se gli id sono uguali vuol dire che sto modificando lo stesso user
		}else {
			return false;
		}
    }
}
