package it.uniroma3.siw.pietropaolo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.User;

public interface UserRepository extends CrudRepository<User, Long>{

    public boolean existsByNomeAndCognome(String nome, String cognome);

    public User findByNomeAndCognome(String nome, String cognome);
    
}
