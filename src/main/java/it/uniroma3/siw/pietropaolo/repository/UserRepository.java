package it.uniroma3.siw.pietropaolo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.pietropaolo.model.pojo.User;

public interface UserRepository extends CrudRepository<User, Long>{

    public boolean existsByNomeAndCognome(String nome, String cognome);

    public Optional<User> findByNomeAndCognome(String nome, String cognome);

    public boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);
    
}
